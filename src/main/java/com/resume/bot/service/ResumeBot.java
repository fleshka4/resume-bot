package com.resume.bot.service;

import com.resume.bot.config.BotConfig;
import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.ResumeField;
import com.resume.bot.display.handler.CallbackActionFactory;
import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.model.entity.User;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

import static com.resume.bot.json.JsonValidator.ValidationType.*;
import static com.resume.bot.json.JsonValidator.checkExperience;
import static com.resume.bot.json.JsonValidator.checks;
import static com.resume.util.BotUtil.appendToField;
import static com.resume.util.Constants.ITEMS_DELIMITER;
import static com.resume.util.Constants.employmentTypes;
import static com.resume.util.Constants.sexTypes;

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

        receivedText = receivedText.trim();

        switch (currentDialogueState) {
            case ENTER_NAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.NAME.getValue(), receivedText);

                    sendMessage("Введите фамилию:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_SURNAME);
                }
            }
            case ENTER_SURNAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.SURNAME.getValue(), receivedText);

                    sendMessage("Введите отчество:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_PATRONYMIC);
                }
            }
            case ENTER_PATRONYMIC -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.PATRONYMIC.getValue(), receivedText);

                    sendMessage("Введите дату рождения в формате ДД-ММ-ГГГГ:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_BIRTHDAY);
                }
            }
            case ENTER_BIRTHDAY -> {
                if (checkInput(receivedText, sendMessageRequest, DATE_FORMAT) &&
                        checkInput(receivedText, sendMessageRequest, BIRTHDAY)) {
                    appendToField(resumeFields, ResumeField.BIRTHDAY.getValue(), receivedText);

                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(
                            sexTypes.values().stream().toList(),
                            sexTypes.keySet().stream().toList())
                    );
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_GENDER);
                    sendMessage("Выберите пол", sendMessageRequest);
                }
            }
            case ENTER_LOCATION -> {
                if (checkInput(receivedText, sendMessageRequest, LOCATION)) {
                    appendToField(resumeFields, ResumeField.LIVE_LOCATION.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Хочу", "Пропустить");
                    List<String> callbackData = List.of("want_enter_education", "skip_education");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage("Хотите ли Вы указать в резюме о своём образовании?", sendMessageRequest);
                }
            }
            case ENTER_INSTITUTION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 512L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EDUCATION_INSTITUTION.getValue(), receivedText);

                    String educationLevel = resumeFields.get(ResumeField.EDUCATION_LEVEL.getValue());

                    if (!educationLevel.equals("Среднее") && !educationLevel.equals("Среднее специальное")) {
                        sendMessage("Введите название факультета:", sendMessageRequest);
                        BotUtil.dialogueStates.put(chatId, BotState.ENTER_FACULTY);
                    } else if (educationLevel.equals("Среднее специальное")) {
                        sendMessage("Введите название специализации:", sendMessageRequest);
                        BotUtil.dialogueStates.put(chatId, BotState.ENTER_SPECIALIZATION);
                    } else {
                        sendMessage("Введите год окончания:", sendMessageRequest);
                        BotUtil.dialogueStates.put(chatId, BotState.ENTER_END_YEAR);
                    }
                }
            }
            case ENTER_FACULTY -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EDUCATION_FACULTY.getValue(), receivedText);

                    sendMessage("Введите название специализации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_SPECIALIZATION);
                }
            }
            case ENTER_SPECIALIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EDUCATION_SPECIALIZATION.getValue(), receivedText);

                    sendMessage("Введите год окончания:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_END_YEAR);
                }
            }
            case ENTER_END_YEAR -> {
                if (checkInput(receivedText, sendMessageRequest, NUMERIC_FORMAT) &&
                        checkInput(receivedText, sendMessageRequest, GRADUATION_YEAR_WITH_YEAR)) {
                    appendToField(resumeFields, ResumeField.EDUCATION_END_YEAR.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Указать ещё одно место обучения", "Продолжить");
                    List<String> callbackData = List.of("want_enter_education", "skip_education");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage("Хотите ли Вы указать ещё одно образование?", sendMessageRequest);
                }
            }
            case ENTER_PERIOD_OF_WORK -> {
                if (checkExperience(receivedText, resumeFields.get(ResumeField.BIRTHDAY.getValue()))) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_PERIOD.getValue(), receivedText);

                    sendMessage("Введите название организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_NAME_OF_ORGANIZATION);
                } else {
                    sendMessage(EmojiParser.parseToUnicode("Введите корректные данные."), sendMessageRequest);
                }
            }
            case ENTER_NAME_OF_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, sendMessageRequest, ALPHANUMERIC_FORMAT) &&
                        checkInput(receivedText, 512L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_ORG_NAME.getValue(), receivedText);

                    sendMessage("Введите город организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_CITY_OF_ORGANIZATION);
                }
            }
            case ENTER_CITY_OF_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, CITY_NAME)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_ORG_CITY.getValue(), receivedText);

                    sendMessage("Прикрепите ссылку на сайт организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_SITE_OF_ORGANIZATION);
                }
            }
            case ENTER_SITE_OF_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, LINK) &&
                        checkInput(receivedText, 256L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_ORG_LINK.getValue(), receivedText);

                    sendMessage("Введите свою должность в организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_POST_IN_ORGANIZATION);
                    // todo Должно быть в самом конце диалога
//                     BotUtil.userStates.put(chatId, BotState.FINISH_DIALOGUE);
//                     finishDialogueWithClient(chatId, sendMessageRequest);

                    // todo сфера деятельности
                    // List<String> industriesNameList = INDUSTRIES.stream().map(Industry::getName).toList();
                    // sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(industriesNameList, industriesNameList,
                    //         5, 0));
                    // sendMessage("Выберите сферу деятельности компании:", sendMessageRequest);
                    // todo сфера деятельности!!!!!!!!!!!!!!!! проблема с клавой хз как сделать 30 кнопок
//                    List<String> industriesNameList = INDUSTRIES.stream().map(Industry::getName).toList();
//                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(industriesNameList, industriesNameList,
//                            5, 0));
//                    sendMessage("Выберите сферу деятельности компании:", sendMessageRequest);
                }
            }
            case ENTER_POST_IN_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_POST.getValue(), receivedText);

                    sendMessage("Введите свои обязанности в организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_DUTIES_IN_ORGANIZATION);
                }
            }
            case ENTER_DUTIES_IN_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 4096L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_DUTIES.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Указать ещё одно место работы", "Продолжить");
                    List<String> callbackData = List.of("want_enter_work_experience", "skip_work_experience");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage("Хотите ли Вы указать ещё одно место работы?", sendMessageRequest);
                }
            }
            case ENTER_SKILLS -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.SKILLS.getValue(), receivedText);
                    // todo цикличность навыков я думаю их нужно записывать в лист и передавать одной строкой в json (поле skills)
                    //  так как skill_set не работает https://api.hh.ru/suggests/skill_set
                    //  пользователь будет вводить по одному навыку а ему нужно будет каждый раз предлагать еще раз
                    List<String> buttonLabels = List.of("Хочу", "Пропустить");
                    List<String> callbackData = List.of("want_enter_about_me", "skip_about_me");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage("Хотите ли Вы рассказать о себе?", sendMessageRequest);
                }
            }
            case ENTER_ABOUT_ME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 4096L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.ABOUT_ME.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Да", "Нет");
                    List<String> callbackData = List.of("yes_enter_car", "no_enter_car");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage("Есть ли у вас автомобиль?", sendMessageRequest);
                }
            }
            case ENTER_REC_NAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 512L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.REC_NAME.getValue(), receivedText);

                    sendMessage("Введите должность:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_REC_POST);
                }
            }
            case ENTER_REC_POST -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.REC_POST.getValue(), receivedText);

                    sendMessage("Введите организацию:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_REC_ORGANIZATION);
                }
            }
            case ENTER_REC_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 512L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.REC_ORGANIZATION.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Хочу", "Продолжить");
                    List<String> callbackData = List.of("want_enter_rec", "skip_rec");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage("Хотите ли Вы указать ещё рекомендующего вас человека?", sendMessageRequest);
                }
            }
            case ENTER_WISH_POSITION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.WISH_POSITION.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Хочу", "Пропустить");
                    List<String> callbackData = List.of("want_enter_salary", "skip_salary");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage("Хотите ли Вы указать желаемую зарплату?", sendMessageRequest);
                }
            }
            case ENTER_WISH_SALARY -> {
                if (checkInput(receivedText, sendMessageRequest, NUMERIC_FORMAT)) {
                    appendToField(resumeFields, ResumeField.WISH_SALARY.getValue(), receivedText);

                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(employmentTypes.values().stream().toList(),
                            employmentTypes.keySet().stream().toList()));
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_BUSYNESS);
                    sendMessage("Выберите желаемую занятость", sendMessageRequest);
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
                        ResumeField field = Arrays.stream(ResumeField.values())
                                .filter(f -> f.getValue().equals(fieldLabel)).findFirst().orElse(null);
                        if (field != null) {
                            if (field.processCheck(fieldValue)) {
                                resumeFields.put(fieldLabel, fieldValue);
                            } else {
                                sendMessage(field.message(), sendMessageRequest);
                                return;
                            }
                        }
                    } else {
                        sendMessage("Пожалуйста, введите только существующие поля.", sendMessageRequest);
                        return;
                    }
                } else {
                    sendMessage("Пожалуйста, введите данные в формате\n*Поле - Ваше новое значение*.", sendMessageRequest);
                    return;
                }
            }

            sendResultMessageAboutClientData(resumeFields, sendMessageRequest);
        }
    }

    private void sendResultMessageAboutClientData(Map<String, String> resumeFields, SendMessage sendMessageRequest) {
        StringBuilder resume = new StringBuilder().append("Пожалуйста, проверьте введенные данные:\n\n");
        Map<String, List<String>> resumeFieldToValues = new LinkedHashMap<>();
        String currentBlock;

        for (Map.Entry<String, String> entry : resumeFields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (key.equals(ResumeField.NAME.getValue())) {
                currentBlock = "*Основная информация*";
                resume.append(currentBlock).append("\n\n");
            } else if (key.equals(ResumeField.EDUCATION_LEVEL.getValue())) {
                currentBlock = "*Образование*";
                resume.append("\n").append(currentBlock).append("\n\n");
            } else if (key.equals(ResumeField.EXPERIENCE_PERIOD.getValue())) {
                currentBlock = "*Опыт*";
                resume.append("\n").append(currentBlock).append("\n");
            } else if (key.equals(ResumeField.SKILLS.getValue())) {
                currentBlock = "*Навыки*";
                resume.append("\n").append(currentBlock).append("\n\n");
            } else if (key.equals(ResumeField.ABOUT_ME.getValue())) {
                currentBlock = "*Дополнительная информация*";
                resume.append("\n").append(currentBlock).append("\n\n");
            } else if (key.equals(ResumeField.REC_NAME.getValue())) {
                currentBlock = "*Список рекомендаций*";
                resume.append("\n").append(currentBlock).append("\n");
            }

            // For fields with multiple values
            List<String> items = Arrays.stream(value.split(ITEMS_DELIMITER)).toList();
            resumeFieldToValues.put(key, items);

            if (isEndOfFieldsBlock(key)) {
                resume.append("\n");
                for (int i = 0; i < items.size(); i++) {
                    for (Map.Entry<String, List<String>> resumeFieldEntry : resumeFieldToValues.entrySet()) {
                        String currentKey = resumeFieldEntry.getKey();
                        String currentValue = resumeFieldEntry.getValue().get(i);
                        resume.append("*").append(currentKey).append("*").append(": ").append(currentValue).append("\n");
                    }
                    if (i < items.size() - 1) {
                        resume.append("\n");
                    }
                }
                resumeFieldToValues.clear();
            } else {
                // For fields with single value
                if (items.size() == 1) {
                    resume.append("*").append(key).append("*").append(": ").append(value).append("\n");
                    resumeFieldToValues.clear();
                }
            }
        }

        resume.append(EmojiParser.parseToUnicode("\nЕсли есть не соответствие или вы ошиблись, нажмите на кнопку *Редактировать* "
                + "и введите это поле повторно в формате *Поле - новое значение*\n\n"
                + "*Пример:*\nИмя - Алексей\n\nИ я автоматически изменю некорректную информацию.:dizzy:"));
        List<String> buttonLabels = List.of("Редактировать", "Всё верно!");
        List<String> callbackData = List.of("edit_result_data", "result_data_is_correct");

        sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
        sendMessage(resume.toString(), sendMessageRequest);
    }

    private boolean isEndOfFieldsBlock(String key) {
        return key.equals(ResumeField.EDUCATION_END_YEAR.getValue()) ||
                key.equals(ResumeField.EXPERIENCE_DUTIES.getValue()) ||
                key.equals(ResumeField.REC_ORGANIZATION.getValue());
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
            String errorMessage = (limit != null) ? "Допустимо не более " + limit + " символов" : "Введите корректные данные.";
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
