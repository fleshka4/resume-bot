package com.resume.bot.service;

import com.resume.bot.config.BotConfig;
import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.MessageUtil;
import com.resume.bot.display.ResumeField;
import com.resume.bot.display.handler.CallbackActionFactory;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.model.entity.User;
import com.resume.latex.LatexProcessor;
import com.resume.util.BigKeyboardType;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.resume.bot.display.MessageUtil.*;
import static com.resume.bot.json.JsonValidator.ValidationType.*;
import static com.resume.bot.json.JsonValidator.checkExperience;
import static com.resume.bot.json.JsonValidator.checks;
import static com.resume.util.BigKeyboardType.RESUMES;
import static com.resume.util.BotUtil.appendToField;
import static com.resume.util.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeBot extends TelegramLongPollingBot {
    private final CallbackActionFactory callbackActionFactory;
    private final BotConfig botConfig;

    private final HeadHunterService headHunterService;
    private final UserService userService;
    private final ResumeService resumeService;
    private final TemplateService templateService;

    private final String hhBaseUrl;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String[] messageWithCode = message.getText().split(" ");
            Long chatId = message.getChatId();

            if (!BotUtil.userStates.containsKey(chatId)) {
                BotUtil.userStates.put(chatId, BotState.START);
                userService.saveUser(new User(chatId));
            }

            if (message.hasText()) {
                String text = message.getText();
                SendMessage sendMessageRequest = createSendMessageRequest(this, chatId);
                BotState currentState = BotUtil.userStates.get(chatId);

                if ("/start".equals(text)) {
                    BotUtil.userStates.put(chatId, BotState.START);
                    BotUtil.userResumeData.put(chatId, new LinkedHashMap<>());
                    BotUtil.clientsMap.put(chatId, new Resume());
                    BotUtil.lastSavedResumeMap.remove(chatId);
                    startCommandReceived(message, sendMessageRequest);
                } else if (messageWithCode.length == 2 && text.startsWith("/start")) {
                    String receivedCode = messageWithCode[1].trim();
                    if ("/start ".concat(receivedCode).equals(text)) {
                        if (BotUtil.states.containsKey(Long.valueOf(receivedCode))) {
                            startWithCodeCommandReceived(chatId, sendMessageRequest);
                        }
                    }
                } else if ("/menu".equals(text)) {
                    if (checkClientExists(chatId, sendMessageRequest)) {
                        menuCommandReceived(sendMessageRequest);
                    }
                } else if (currentState == BotState.START_DIALOGUE) {
                    BotUtil.clientsMap.put(chatId, new Resume());
                    startDialogueWithClient(text, chatId, sendMessageRequest);
                } else if (currentState == BotState.EDIT_CLIENT_RESULT_DATA) {
                    if (checkClientExists(chatId, sendMessageRequest)) {
                        editResultClientData(text, chatId, sendMessageRequest);
                    }
                } else if (currentState == BotState.EDIT_MY_RESUME && BotUtil.userMyResumeMap.containsKey(chatId)) {
                    editClientResumeData(
                            JsonProcessor.createEntityFromJson(BotUtil.userMyResumeMap.get(chatId).getResumeData(), Resume.class),
                            text, chatId, sendMessageRequest);
                } else {
                    sendMessage(this, EmojiParser.parseToUnicode("Извините, я не понимаю эту команду.:cry:"), sendMessageRequest);
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackData = callbackQuery.getData();
            Integer messageId = callbackQuery.getMessage().getMessageId();
            Long chatId = callbackQuery.getMessage().getChatId();
            SendMessage sendMessageRequest = createSendMessageRequest(this, chatId);

            BotState currentState = BotUtil.userStates.get(chatId);
            if (callbackData.startsWith("template")) {
                if (currentState == BotState.EDIT_MY_RESUME_TEMPLATE) {
                    updateTemplate(callbackData, messageId, chatId);
                } else if (currentState == BotState.CHOOSE_TEMPLATE) {
                    var resume = BotUtil.clientsMap.get(chatId);
                    var dbResume = BotUtil.lastSavedResumeMap.get(chatId);
                    setTemplate(dbResume, callbackData.split("_"));

                    String pdfPath;
                    Template template = dbResume.getTemplate();
                    try {
                        pdfPath = LatexProcessor.compile(resume, template.getSourcePath(),
                                chatId.toString(), dbResume.getTitle());
                    } catch (IOException | InterruptedException e) {
                        log.error(e.getMessage());
                        throw new RuntimeException(e);
                    }
                    resumeService.updateTemplateByResumeId(template, dbResume.getResumeId());
                    resumeService.updatePdfPathByResumeId(pdfPath, dbResume.getResumeId());

                    File file = new File(pdfPath);
                    try {
                        execute(new SendDocument(String.valueOf(chatId), new InputFile(file)));
                    } catch (TelegramApiException e) {
                        log.error(e.getMessage());
                        sendMessage(this, "Не найден файл, содержащий резюме. Попробуйте создать заново", chatId);
                    }

                    BotUtil.createMenu(MessageUtil.createSendMessageRequest(this, chatId), this);
                }
                return;
            }

            switch (callbackData) {
                case "yes_go_to_menu" -> createMenu(sendMessageRequest);
                case "no_go_to_menu" ->
                        sendMessage(this, EmojiParser.parseToUnicode("Продолжим работу.:wink:"), sendMessageRequest);
                case "skip_contacts" -> {
                    BotUtil.userStates.put(chatId, BotState.FINISH_DIALOGUE);
                    finishDialogueWithClient(chatId, sendMessageRequest);
                }
                default -> {
                    CallbackActionHandler callbackHandler = callbackActionFactory.createCallbackActionHandler(this, callbackData);

                    if (callbackHandler != null) {
                        callbackHandler.performAction(callbackData, messageId, chatId);
                    }
                }
            }
        }
    }

    private void updateTemplate(String callbackData, Integer messageId, Long chatId) {
        String[] splits = callbackData.split("_");
        if (splits.length == 4) {
            List<com.resume.bot.model.entity.Resume> resumes = resumeService.getResumesByUserId(chatId);
            resumes.sort(Comparator.comparing(com.resume.bot.model.entity.Resume::getResumeId));

            int current = Integer.parseInt(splits[2]);
            if (current >= resumes.size()) {
                current = resumes.size() - 1;
            }

            com.resume.bot.model.entity.Resume resume = resumes.get(current);
            if (resume == null) {
                throw new RuntimeException("Resume is not found");
            }
            setTemplate(resume, splits);
            BotUtil.createActionsWithChosenResume(this, messageId, chatId, String.valueOf(current));
        }
    }

    private void setTemplate(com.resume.bot.model.entity.Resume resume, String[] splits) {
        Template template = templateService.getTemplate(Integer.parseInt(splits[splits.length - 1]));
        if (template == null) {
            throw new RuntimeException("Template is not found");
        }
        resume.setTemplate(template);
        resumeService.updateTemplateByResumeId(template, resume.getResumeId());
    }

    private void startCommandReceived(Message message, SendMessage sendMessageRequest) {
        String startMessage = "Привет " + message.getChat().getFirstName() + " !:wave:\nЯ бот для создания резюме. " +
                "Давай вместе составим профессиональное резюме для твоего будущего успеха!:star2:\n" +
                "Просто следуй моим инструкциям.";

        log.info("Replied to user: " + message.getChat().getFirstName());

        sendMessage(this, EmojiParser.parseToUnicode(startMessage), sendMessageRequest);
        createMenu(sendMessageRequest);
    }

    private void menuCommandReceived(SendMessage sendMessageRequest) {
        List<String> buttonLabels = Arrays.asList("Да", "Нет");
        List<String> callbackData = Arrays.asList("yes_go_to_menu", "no_go_to_menu");

        sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
        sendMessage(this, EmojiParser.parseToUnicode("Вы уверены, что хотите вернуться в главное меню?"), sendMessageRequest);
    }

    private void startWithCodeCommandReceived(Long chatId, SendMessage sendMessageRequest) {
        sendMessage(this, EmojiParser.parseToUnicode("Супер!:fire: Вы успешно авторизовались, теперь мы можем продолжить работу!"), sendMessageRequest);
        BotUtil.userStates.put(chatId, BotState.MY_RESUMES);

        List<Resume> hhResumes = headHunterService.getClientResumes(hhBaseUrl, chatId);
        List<com.resume.bot.model.entity.Resume> resumesFromDB = resumeService.getResumesByUserId(chatId);

        List<String> buttonLabelsHH = new ArrayList<>(hhResumes.stream()
                .map(Resume::getTitle)
                .toList());

        List<String> buttonLabelsDb = new ArrayList<>(resumesFromDB.stream()
                .map(com.resume.bot.model.entity.Resume::getTitle)
                .toList());

        buttonLabelsDb.addAll(buttonLabelsHH);

        buttonLabelsDb.add("Назад");

        List<String> buttonIdsAll = new ArrayList<>();

        for (int i = 1; i <= resumesFromDB.size() + hhResumes.size(); i++) {
            buttonIdsAll.add("res_" + buttonLabelsDb.get(i - 1) + "_" + i);
        }
        buttonIdsAll.add("back_to_menu_3");

        executeMessageWithBigKeyBoard(this, EmojiParser.parseToUnicode("""
                После выбора конкретного резюме, у вас будет возможность:
                                
                  - *Опубликовать его на HeadHunter*
                  - *Скачать ваше резюме*
                  - *Внести изменения в резюме*
                  - *Удалить резюме*
                """), chatId, buttonLabelsDb, buttonIdsAll, 0, resumesFromDB.size() + hhResumes.size(), RESUMES);
    }

    private void startDialogueWithClient(String receivedText, Long chatId, SendMessage sendMessageRequest) {
        BotState currentDialogueState = BotUtil.dialogueStates.get(chatId);
        Map<String, String> resumeFields = checkAvailabilityResumeFields(chatId);

        receivedText = receivedText.trim();

        switch (currentDialogueState) {
            case ENTER_NAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.NAME.getValue(), receivedText);

                    sendMessage(this, "Введите фамилию:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_SURNAME);
                }
            }
            case ENTER_SURNAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.SURNAME.getValue(), receivedText);

                    sendMessage(this, "Введите отчество:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_PATRONYMIC);
                }
            }
            case ENTER_PATRONYMIC -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.PATRONYMIC.getValue(), receivedText);

                    sendMessage(this, "Введите дату рождения в формате ДД-ММ-ГГГГ:", sendMessageRequest);
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
                    sendMessage(this, "Выберите пол", sendMessageRequest);
                }
            }
            case ENTER_LOCATION -> {
                if (checkInput(receivedText, sendMessageRequest, LOCATION)) {
                    appendToField(resumeFields, ResumeField.LIVE_LOCATION.getValue(), receivedText);

                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(educationLevels.values().stream().toList(), educationLevels.keySet().stream().toList()));
                    sendMessage(this, "Укажите уровень образования", sendMessageRequest);
                }
            }
            case ENTER_EDU_LEVEL -> {
                if (checkInput(receivedText, sendMessageRequest, LOCATION)) {
                    appendToField(resumeFields, ResumeField.LIVE_LOCATION.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Хочу", "Пропустить");
                    List<String> callbackData = List.of("want_enter_education", "skip_education");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage(this, "Хотите ли Вы указать в резюме о своём образовании?", sendMessageRequest);
                }
            }
            case ENTER_INSTITUTION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 512L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EDUCATION_INSTITUTION.getValue(), receivedText);

                    String educationLevel = resumeFields.get(ResumeField.EDUCATION_LEVEL.getValue());

                    if (!educationLevel.equals("Среднее") && !educationLevel.equals("Среднее специальное")) {
                        sendMessage(this, "Введите название факультета:", sendMessageRequest);
                        BotUtil.dialogueStates.put(chatId, BotState.ENTER_FACULTY);
                    } else if (educationLevel.equals("Среднее специальное")) {
                        sendMessage(this, "Введите название специализации:", sendMessageRequest);
                        BotUtil.dialogueStates.put(chatId, BotState.ENTER_SPECIALIZATION);
                    } else {
                        sendMessage(this, "Введите год окончания:", sendMessageRequest);
                        BotUtil.dialogueStates.put(chatId, BotState.ENTER_END_YEAR);
                    }
                }
            }
            case ENTER_FACULTY -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EDUCATION_FACULTY.getValue(), receivedText);

                    sendMessage(this, "Введите название специализации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_SPECIALIZATION);
                }
            }
            case ENTER_SPECIALIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EDUCATION_SPECIALIZATION.getValue(), receivedText);

                    sendMessage(this, "Введите год окончания:", sendMessageRequest);
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
                    sendMessage(this, "Хотите ли Вы указать ещё одно образование?", sendMessageRequest);
                }
            }
            case ENTER_PERIOD_OF_WORK -> {
                if (checkExperience(receivedText, resumeFields.get(ResumeField.BIRTHDAY.getValue()))) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_PERIOD.getValue(), receivedText);

                    sendMessage(this, "Введите название организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_NAME_OF_ORGANIZATION);
                } else {
                    sendMessage(this, EmojiParser.parseToUnicode("Введите корректные данные."), sendMessageRequest);
                }
            }
            case ENTER_NAME_OF_ORGANIZATION -> {
                if (checkInput(receivedText, 512L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_ORG_NAME.getValue(), receivedText);

                    sendMessage(this, "Введите город организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_CITY_OF_ORGANIZATION);
                }
            }
            case ENTER_CITY_OF_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, CITY_NAME)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_ORG_CITY.getValue(), receivedText);

                    sendMessage(this, "Прикрепите ссылку на сайт организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_SITE_OF_ORGANIZATION);
                }
            }
            case ENTER_SITE_OF_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, LINK) &&
                        checkInput(receivedText, 256L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_ORG_LINK.getValue(), receivedText);

                    List<String> buttonLabels = new ArrayList<>();
                    List<String> callbackDataList = new ArrayList<>();
                    StringBuilder message = new StringBuilder();
                    message.append("Выберите сферу деятельности компании:\n\n");
                    BotUtil.prepareBigKeyboardCreation(0, BigKeyboardType.INDUSTRIES, message, buttonLabels, callbackDataList);
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackDataList, BigKeyboardType.INDUSTRIES));
                    sendMessage(this, message.toString(), sendMessageRequest);
                }
            }
            case ENTER_POST_IN_ORGANIZATION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    if (BotUtil.personAndIndustry.containsKey(chatId)) {
                        appendToField(resumeFields, ResumeField.EXPERIENCE_ORG_INDUSTRY.getValue(), BotUtil.personAndIndustry.get(chatId));
                    }
                    appendToField(resumeFields, ResumeField.EXPERIENCE_POST.getValue(), receivedText);

                    sendMessage(this, "Введите свои обязанности в организации:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_DUTIES_IN_ORGANIZATION);
                }
            }
            case ENTER_DUTIES_IN_ORGANIZATION -> {
                if (checkInput(receivedText, 4096L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.EXPERIENCE_DUTIES.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Указать ещё одно место работы", "Продолжить");
                    List<String> callbackData = List.of("want_enter_work_experience", "skip_work_experience");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage(this, "Хотите ли Вы указать ещё одно место работы?", sendMessageRequest);
                }
            }
            case ENTER_SKILLS -> {
                if (checkInput(receivedText, sendMessageRequest, JsonValidator.ValidationType.SKILLS) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.SKILLS.getValue(), receivedText);
                    List<String> buttonLabels = List.of("Хочу", "Пропустить");
                    List<String> callbackData = List.of("want_enter_about_me", "skip_about_me");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage(this, "Хотите ли Вы рассказать о себе?", sendMessageRequest);
                }
            }
            case ENTER_ABOUT_ME -> {
                if (checkInput(receivedText, 4096L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.ABOUT_ME.getValue(), receivedText);

                    List<String> buttonLabels = List.of("Да", "Нет");
                    List<String> callbackData = List.of("yes_enter_car", "no_enter_car");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
                    sendMessage(this, "Есть ли у вас автомобиль?", sendMessageRequest);
                }
            }
            case ENTER_REC_NAME -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 512L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.REC_NAME.getValue(), receivedText);

                    sendMessage(this, "Введите должность:", sendMessageRequest);
                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_REC_POST);
                }
            }
            case ENTER_REC_POST -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.REC_POST.getValue(), receivedText);

                    sendMessage(this, "Введите организацию:", sendMessageRequest);
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
                    sendMessage(this, "Хотите ли Вы указать ещё рекомендующего вас человека?", sendMessageRequest);
                }
            }
            case ENTER_WISH_POSITION -> {
                if (checkInput(receivedText, sendMessageRequest, ALPHA_SPACE_FORMAT) &&
                        checkInput(receivedText, 128L, sendMessageRequest, SYMBOLS_LIMIT)) {
                    appendToField(resumeFields, ResumeField.WISH_POSITION.getValue(), receivedText);

                    List<String> buttonLabels = new ArrayList<>();
                    List<String> callbackDataList = new ArrayList<>();
                    StringBuilder message = new StringBuilder();
                    message.append("Выберите специализацию:\n\n");
                    BotUtil.prepareBigKeyboardCreation(0, BigKeyboardType.PROFESSIONAL_ROLES, message, buttonLabels, callbackDataList);
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackDataList, BigKeyboardType.PROFESSIONAL_ROLES));
                    sendMessage(this, message.toString(), sendMessageRequest);
                }
            }
            case ENTER_WISH_SALARY -> {
                if (checkInput(receivedText, sendMessageRequest, NUMERIC_FORMAT)) {
                    appendToField(resumeFields, ResumeField.WISH_SALARY.getValue(), receivedText);

                    List<String> keys = new ArrayList<>(employmentTypes.keySet().stream().toList());
                    List<String> values = new ArrayList<>(employmentTypes.values().stream().toList());
                    keys.add("skip_busyness");
                    values.add("Продолжить");
                    sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(values, keys));

                    BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_BUSYNESS);
                    sendMessage(this, "Выберите желаемую занятость", sendMessageRequest);
                }
            }
            case ENTER_PHONE, ENTER_WORK_PHONE, ENTER_HOME_PHONE -> {
                if (checkInput(receivedText, sendMessageRequest, PHONE_NUMBER_FORMAT)) {
                    appendToField(resumeFields, ResumeField.PHONE.getValue(), receivedText);

                    BotUtil.userStates.put(chatId, BotState.FINISH_DIALOGUE);
                    finishDialogueWithClient(chatId, sendMessageRequest);
                }
            }
            case ENTER_MAIL -> {
                if (checkInput(receivedText, sendMessageRequest, EMAIL_FORMAT)) {
                    appendToField(resumeFields, ResumeField.EMAIL.getValue(), receivedText);

                    BotUtil.userStates.put(chatId, BotState.FINISH_DIALOGUE);
                    finishDialogueWithClient(chatId, sendMessageRequest);
                }
            }
            case FINISH_DIALOGUE -> {
                BotUtil.userStates.put(chatId, BotState.FINISH_DIALOGUE);
                finishDialogueWithClient(chatId, sendMessageRequest);
            }
            default -> {
                sendMessage(this,
                        EmojiParser.parseToUnicode("Что-то пошло не так.\nПопробуйте ещё раз.:cry:"), sendMessageRequest);
            }
        }
    }

    private void finishDialogueWithClient(Long chatId, SendMessage sendMessageRequest) {
        if (BotUtil.userStates.get(chatId) == BotState.FINISH_DIALOGUE) {
            Map<String, String> resumeFields = BotUtil.userResumeData.get(chatId);
            if (BotUtil.personAndProfessionalRole.containsKey(chatId)) {
                appendToField(resumeFields, ResumeField.WITH_PROFESSIONAL_ROLE.getValue(), BotUtil.personAndProfessionalRole.get(chatId));
            }
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
                    int fieldId = 0;
                    String fieldLabel = parts[0].trim().toLowerCase();
                    String fieldValue = parts[1].trim();

                    String[] labelItems = fieldLabel.split(" ");
                    String fieldIdStr = labelItems[labelItems.length - 1];
                    String fieldLabelWithoutIdNumber = fieldLabel.substring(0, fieldLabel.length() - fieldIdStr.length()).trim();

                    if (resumeFields.containsKey(fieldLabel) || resumeFields.containsKey(fieldLabelWithoutIdNumber)) {

                        String[] actualValues = {};
                        try {
                            actualValues = resumeFields.get(fieldLabel).split(ITEMS_DELIMITER);
                        } catch (Exception ignored) {
                        }
                        try {
                            actualValues = resumeFields.get(fieldLabelWithoutIdNumber).split(ITEMS_DELIMITER);
                        } catch (Exception ignored) {
                        }

                        if (actualValues.length > 1) {
                            fieldLabel = fieldLabelWithoutIdNumber;
                            try {
                                fieldId = Integer.parseInt(fieldIdStr) - 1;
                            } catch (Exception e) {
                                sendMessage(this, "Пожалуйста, укажите номер редактируемого поля.", sendMessageRequest);
                                return;
                            }
                        }

                        String label = fieldLabel;
                        ResumeField field = Arrays.stream(ResumeField.values())
                                .filter(f -> f.getValue().equals(label)).findFirst().orElse(null);
                        if (field != null) {
                            if (field.processCheck(fieldValue)) {
                                try {
                                    actualValues[fieldId] = fieldValue;
                                    resumeFields.put(fieldLabel, String.join(ITEMS_DELIMITER, actualValues));
                                } catch (Exception e) {
                                    sendMessage(this, "Пожалуйста, укажите корректный номер поля.", sendMessageRequest);
                                    return;
                                }
                            } else {
                                sendMessage(this, field.message(), sendMessageRequest);
                                return;
                            }
                        }
                    } else {
                        sendMessage(this, "Пожалуйста, введите только существующие поля.", sendMessageRequest);
                        return;
                    }
                } else {
                    sendMessage(this, """
                                    Пожалуйста, введите данные в формате
                                    *Поле - Ваше новое значение*.""",
                            sendMessageRequest);
                    return;
                }
            }

            sendResultMessageAboutClientData(resumeFields, sendMessageRequest);
        }
    }

    private void editClientResumeData(com.resume.bot.json.entity.client.Resume resume, String receivedText,
                                      Long chatId, SendMessage sendMessageRequest) {
        String[] lines = receivedText.split("\\r?\\n");
        for (String line : lines) {
            String[] parts = line.split("-", 2);
            if (parts.length == 2) {
                String fieldLabel = parts[0].trim().toLowerCase();
                String fieldValue = parts[1].trim();

                ResumeField field = Arrays.stream(ResumeField.values())
                        .filter(f -> f.getValue().equals(fieldLabel)).findFirst().orElse(null);
                if (field != null) {
                    if (field.processCheck(fieldValue)) {
                        field.editInResume(resume, fieldValue);

                        int resumeId = BotUtil.userMyResumeMap.get(chatId).getResumeId();
                        resumeService.updateResumeDataByResumeId(JsonProcessor.createJsonFromEntity(resume), resumeId);
                        BotUtil.userMyResumeMap.put(chatId, resumeService.getResume(resumeId));

                        sendMessage(this,
                                "Новое значение поля \"%s\" - %s".formatted(field.getValue(), fieldValue),
                                sendMessageRequest);
                    } else {
                        sendMessage(this, field.message(), sendMessageRequest);
                    }
                }
            } else {
                sendMessage(this, """
                                Пожалуйста, введите данные в формате
                                *Поле - Ваше новое значение*.""",
                        sendMessageRequest);
            }
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
                resume.append(currentBlock).append("\n");
            } else if (key.equals(ResumeField.EDUCATION_LEVEL.getValue())) {
                currentBlock = "*Образование*";
                resume.append("\n\n").append(currentBlock).append("\n");
            } else if (key.equals(ResumeField.EXPERIENCE_PERIOD.getValue())) {
                currentBlock = "*Опыт*";
                resume.append("\n\n").append(currentBlock).append("\n");
            } else if (key.equals(ResumeField.SKILLS.getValue())) {
                currentBlock = "*Навыки*";
                resume.append("\n\n").append(currentBlock).append("\n");
            } else if (key.equals(ResumeField.ABOUT_ME.getValue())) {
                currentBlock = "*Дополнительная информация*";
                resume.append("\n\n").append(currentBlock).append("\n");
            } else if (key.equals(ResumeField.REC_NAME.getValue())) {
                currentBlock = "*Список рекомендаций*";
                resume.append("\n\n").append(currentBlock).append("\n");
            } else if (key.equals(ResumeField.WISH_POSITION.getValue())) {
                resume.append("\n\n");
            }

            // For fields with multiple values
            List<String> items = Arrays.stream(value.split(ITEMS_DELIMITER)).toList();
            resumeFieldToValues.put(key, items);

            if (isEndOfFieldsBlock(key)) {
                for (int i = 0; i < items.size(); i++) {
                    for (Map.Entry<String, List<String>> resumeFieldEntry : resumeFieldToValues.entrySet()) {
                        String currentKey = resumeFieldEntry.getKey();
                        String currentValue = resumeFieldEntry.getValue().get(i);
                        resume.append("\n").append("*").append(currentKey).append("*").append(": ").append(currentValue);
                    }
                    if (i < items.size() - 1) {
                        resume.append("\n");
                    }
                }
                resumeFieldToValues.clear();
            } else if (isCommaSeparatedMultipleField(key)) {
                value = value.replace(ITEMS_DELIMITER, ", ");
                resume.append("\n").append("*").append(key).append("*").append(": ").append(value);
                resumeFieldToValues.clear();
            } else {
                // For fields with single value
                if (items.size() == 1) {
                    resume.append("\n").append("*").append(key).append("*").append(": ").append(value);
                    resumeFieldToValues.clear();
                }
            }
        }

        resume.append(EmojiParser.parseToUnicode("""


                Если есть не соответствие или вы ошиблись, нажмите на кнопку *Редактировать* и введите это поле повторно в формате
                *Поле - новое значение*

                *Пример:*
                Имя - Алексей

                Чтобы отредактировать поле в одном из блоков резюме укажите его порядковый номер через пробел
                                
                *Пример:*
                Учебное заведение 2 - СПбПУ

                И я автоматически изменю некорректную информацию.:dizzy:"""));

        List<String> buttonLabels = List.of("Редактировать", "Всё верно!");
        List<String> callbackData = List.of("edit_result_data", "result_data_is_correct");

        sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackData));
        sendMessage(this, resume.toString(), sendMessageRequest);
    }

    private boolean isEndOfFieldsBlock(String key) {
        return key.equals(ResumeField.EDUCATION_END_YEAR.getValue()) ||
                key.equals(ResumeField.EXPERIENCE_DUTIES.getValue()) ||
                key.equals(ResumeField.REC_ORGANIZATION.getValue());
    }

    private boolean isCommaSeparatedMultipleField(String key) {
        return key.equals(ResumeField.DRIVER_LICENCE.getValue()) ||
                key.equals(ResumeField.BUSYNESS.getValue()) ||
                key.equals(ResumeField.SCHEDULE.getValue());
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
        BotUtil.createMenu(sendMessageRequest, this);
    }

    private boolean checkInput(String receivedText, SendMessage sendMessageRequest, JsonValidator.ValidationType validationType) {
        return checkInput(receivedText, null, sendMessageRequest, validationType);
    }

    private boolean checkInput(String receivedText, Long limit, SendMessage sendMessageRequest, JsonValidator.ValidationType validationType) {
        boolean validationResult = Boolean.TRUE.equals(checks.get(validationType).apply(new Object[]{receivedText, limit}));
        if (!validationResult) {
            String errorMessage = (limit != null) ? "Допустимо не более " + limit + " символов" : "Введите корректные данные.";
            sendMessage(this, EmojiParser.parseToUnicode(errorMessage), sendMessageRequest);
        }
        return validationResult;
    }

    private boolean checkClientExists(Long chatId, SendMessage sendMessageRequest) {
        if (BotUtil.clientsMap.get(chatId) == null) {
            sendMessage(this, EmojiParser.parseToUnicode("Попробуйте ещё раз.:cry:\nВведите команду /start."), sendMessageRequest);
            return false;
        }
        return true;
    }

    private Map<String, String> checkAvailabilityResumeFields(Long chatId) {
        return BotUtil.userResumeData.computeIfAbsent(chatId, k -> new LinkedHashMap<>());
    }
}
