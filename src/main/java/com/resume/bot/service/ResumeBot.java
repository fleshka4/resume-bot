package com.resume.bot.service;

import com.resume.bot.config.BotConfig;
import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.handler.CallbackActionFactory;
import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.model.entity.User;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.resume.bot.json.JsonValidator.ValidationType.*;
import static com.resume.bot.json.JsonValidator.checks;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeBot extends TelegramLongPollingBot {
    private final CallbackActionFactory callbackActionFactory;

    private final BotConfig botConfig;
    private final HhConfig hhConfig;

    private final HeadHunterService headHunterService;
    private final UserService userService;

    private final String hhBaseUrl;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            if (!BotUtil.userStates.containsKey(chatId)) {
                BotUtil.userStates.put(chatId, BotState.START);
                userService.saveUser(new User(chatId));
            }

            if (message.hasText()) {
                SendMessage sendMessageRequest = createSendMessageRequest(chatId);
                BotState currentState = BotUtil.userStates.get(chatId);

                if ("/start".equals(message.getText())) {
                    BotUtil.userStates.put(chatId, BotState.START);
                    BotUtil.clientsMap.put(chatId, new Resume());
                    startCommandReceived(message, sendMessageRequest);
                } else if ("/menu".equals(message.getText())) {
                    if (checkClientExists(chatId, sendMessageRequest)) {
                        menuCommandReceived(sendMessageRequest);
                    }
                } else if (currentState == BotState.START_DIALOGUE) {
                    BotUtil.clientsMap.put(chatId, new Resume());
                    startDialogueWithClient(message.getText(), chatId, sendMessageRequest);
                } else if (currentState == BotState.EDIT_CLIENT_RESULT_DATA) {
                    if (checkClientExists(chatId, sendMessageRequest)) {
                        editResultClientData(message.getText(), chatId, sendMessageRequest);
                    }
                } else if ("/login".equals(message.getText())) {
                    loginHandler(chatId, sendMessageRequest);
                } else {
                    sendMessage(EmojiParser.parseToUnicode("Извините, я не понимаю эту команду.:cry:"), sendMessageRequest);
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            Long chatId = callbackQuery.getMessage().getChatId();
            SendMessage sendMessageRequest = createSendMessageRequest(chatId);

            if (callbackData.equals("yes_go_to_menu")) {
                createMenu(sendMessageRequest);
            } else if (callbackData.equals("no_go_to_menu")) {
                sendMessage(EmojiParser.parseToUnicode("Продолжим работу.:wink:"), sendMessageRequest);
            } else {
                CallbackActionHandler callbackHandler = callbackActionFactory.createCallbackActionHandler(this, callbackData);

                if (callbackHandler != null) {
                    callbackHandler.performAction(callbackData, messageId, chatId);
                }
            }
        }
    }

    private void loginHandler(Long chatId, SendMessage sendMessageRequest) {
        long randomValue = BotUtil.generateRandom12DigitNumber(BotUtil.random);
        while (BotUtil.states.containsKey(randomValue)) {
            randomValue = BotUtil.generateRandom12DigitNumber(BotUtil.random);
        }

        BotUtil.states.put(randomValue, chatId);

        String link = "https://hh.ru/oauth/authorize?" +
                "response_type=code&" +
                "client_id=" + hhConfig.getClientId() +
                "&state=" + randomValue
                + "&redirect_uri=http://localhost:5000/hh/auth";
        String msg = "Чтобы авторизоваться, перейдите по [ссылке](" + link + ") и следуйте инструкциям";

        sendMessage(msg, sendMessageRequest);
    }

    private void startCommandReceived(Message message, SendMessage sendMessageRequest) {
        String startMessage = "Привет " + message.getChat().getFirstName() + " !:wave:\nЯ бот для создания резюме." +
                "Давай вместе составим профессиональное резюме для твоего будущего успеха!:star2:\n" +
                "Просто следуй моим инструкциям.";

        log.info("Replied to user: " + message.getChat().getFirstName());

        sendMessage(EmojiParser.parseToUnicode(startMessage), sendMessageRequest);
        createMenu(sendMessageRequest);
    }

    private void menuCommandReceived(SendMessage sendMessageRequest) {
        List<String> buttonLabels = Arrays.asList("Да", "Нет");
        List<String> callbackData = Arrays.asList("yes_go_to_menu", "no_go_to_menu");

        sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
        sendMessage(EmojiParser.parseToUnicode("Вы уверены, что хотите вернуться в главное меню?"), sendMessageRequest);
    }

    // todo Дописать диалог
    private void startDialogueWithClient(String receivedText, Long chatId, SendMessage sendMessageRequest) {
        BotState currentDialogueState = BotUtil.dialogueStates.get(chatId);
        Map<String, String> resumeFields = checkAvailabilityResumeFields(chatId);

        receivedText = receivedText.toLowerCase().trim();

        switch (currentDialogueState) {
            case ENTER_NAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    resumeFields.put("имя", receivedText);

                    sendMessage("Введите фамилию:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_SURNAME);
                }
            }
            case ENTER_SURNAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    resumeFields.put("фамилия", receivedText);

                    sendMessage("Введите отчество:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_PATRONYMIC);
                }
            }
            case ENTER_PATRONYMIC -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    resumeFields.put("отчество", receivedText);

                    sendMessage("Введите дату рождения в формате ДД-ММ-ГГГГ:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_BIRTHDAY);
                }
            }
            case ENTER_BIRTHDAY -> {
                if (checkInput(receivedText, sendMessageRequest, DATE_FORMAT) &&
                        checkInput(receivedText, sendMessageRequest, BIRTHDAY)) {
                    resumeFields.put("дата рождения", receivedText);

                    List<String> buttonLabels = List.of("М", "Ж");
                    List<String> callbackData = List.of("choice_gender_m", "choice_gender_f");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_GENDER);
                    sendMessage("Выберите пол", sendMessageRequest);
                }
            }
            case ENTER_LOCATION -> {
                // todo Сделать проверку на локацию существует ли такой город?
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 256L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    resumeFields.put("местожительство", receivedText);

                    // todo Должно быть в самом конце диалога
                    BotUtil.userStates.put(chatId, BotState.FINISH_DIALOGUE);
                    finishDialogueWithClient(chatId, sendMessageRequest);
                }
            }
            default ->
                    sendMessage(EmojiParser.parseToUnicode("Что-то пошло не так.\nПопробуйте ещё раз.:cry:"), sendMessageRequest);
        }
    }

    private void finishDialogueWithClient(Long chatId, SendMessage sendMessageRequest) {
        if (BotUtil.userStates.get(chatId) == BotState.FINISH_DIALOGUE) {
            Map<String, String> resumeFields = BotUtil.userResumeData.get(chatId);
            sendResultMessageAboutClientData(resumeFields, sendMessageRequest);
        }
    }

    // todo У клиента тоже значения должны обновляться, чтобы получить обновленный json
    //  Нужно как то добавить здесь проверки те что и в начале диалога чекают что в имени только символы и т д??
    private void editResultClientData(String receivedText, Long chatId, SendMessage sendMessageRequest) {
        Map<String, String> resumeFields = checkAvailabilityResumeFields(chatId);

        if (BotUtil.userStates.get(chatId) == BotState.EDIT_CLIENT_RESULT_DATA) {
            String[] lines = receivedText.split("\\r?\\n");
            for (String line : lines) {
                String[] parts = line.split("-", 2);
                if (parts.length == 2) {
                    String fieldLabel = parts[0].trim().toLowerCase();
                    String fieldValue = parts[1].trim();

                    if (resumeFields.containsKey(fieldLabel)) {
                        resumeFields.put(fieldLabel, fieldValue);
                    } else {
                        sendMessage("Пожалуйста, вводите только существующие поля.", sendMessageRequest);
                    }
                } else {
                    sendMessage("Пожалуйста, вводите данные в формате\n*Поле - Ваше новое значение*.", sendMessageRequest);
                    return;
                }
            }

            sendResultMessageAboutClientData(resumeFields, sendMessageRequest);
        }
    }

    private void sendResultMessageAboutClientData(Map<String, String> resumeFields, SendMessage sendMessageRequest) {
        StringBuilder resume = new StringBuilder().append("Пожалуйста, проверьте введенные данные:\n\n");
        for (Map.Entry<String, String> entry : resumeFields.entrySet()) {
            resume.append("*").append(entry.getKey()).append("*").append(": ").append(entry.getValue()).append("\n");
        }
        resume.append(EmojiParser.parseToUnicode("\nЕсли есть не соответствие или вы ошиблись, нажмите на кнопку *Редактировать* "
                + "и введите это поле повторно в формате *Поле - новое значение*\n\n"
                + "*Пример:*\nИмя - Алексей\n\nИ я автоматически изменю некорректную информацию.:dizzy:"));
        List<String> buttonLabels = List.of("Редактировать", "Всё верно!");
        List<String> callbackData = List.of("edit_result_data", "result_data_is_correct");

        sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
        sendMessage(resume.toString(), sendMessageRequest);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private void createMenu(SendMessage sendMessageRequest) {
        List<String> buttonLabels = Arrays.asList("Создать резюме", "Экспорт резюме с hh.ru", "Мои резюме");
        List<String> callbackData = Arrays.asList("create_resume", "export_resume_hh", "my_resumes");

        sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));

        String menuInfo = "Выберите действие:\n\n"
                + "*Создать резюме* :memo:\nНачните процесс создания нового резюме с нуля!\n\n"
                + "*Экспорт резюме с hh.ru* :inbox_tray:\nЭкспортируйте свои данные с hh.ru для взаимодействия с ними.\n\n"
                + "*Мои резюме* :clipboard:\nПосмотрите список ваших созданных резюме.";

        sendMessage(EmojiParser.parseToUnicode(menuInfo), sendMessageRequest);
    }

    private SendMessage createSendMessageRequest(Long chatId) {
        SendMessage sendMessageRequest = new SendMessage();

        sendMessageRequest.enableMarkdown(true);
        sendMessageRequest.setChatId(chatId.toString());
        return sendMessageRequest;
    }

    private void sendMessage(String messageToSend, SendMessage sendMessageRequest) {
        sendMessageRequest.setText(messageToSend);
        executeMessage(sendMessageRequest);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }

    private boolean checkInput(String receivedText, SendMessage sendMessageRequest, JsonValidator.ValidationType validationType) {
        return checkInput(receivedText, null, sendMessageRequest, validationType);
    }

    private boolean checkInput(String receivedText, Long limit, SendMessage sendMessageRequest, JsonValidator.ValidationType validationType) {
        boolean validationResult = Boolean.TRUE.equals(checks.get(validationType).apply(new Object[]{receivedText, limit}));
        if (!validationResult) {
            String errorMessage = (limit != null) ? "Допустимо не более " + limit + " символов" : "Вводите корректные данные.";
            sendMessage(EmojiParser.parseToUnicode(errorMessage), sendMessageRequest);
        }
        return validationResult;
    }

    private boolean checkClientExists(Long chatId, SendMessage sendMessageRequest) {
        if (BotUtil.clientsMap.get(chatId) == null) {
            sendMessage(EmojiParser.parseToUnicode("Попробуйте ещё раз.:cry:\nВведите команду /start."), sendMessageRequest);
            return false;
        }
        return true;
    }

    private Map<String, String> checkAvailabilityResumeFields(Long chatId) {
        return BotUtil.userResumeData.computeIfAbsent(chatId, k -> new LinkedHashMap<>());
    }
}
