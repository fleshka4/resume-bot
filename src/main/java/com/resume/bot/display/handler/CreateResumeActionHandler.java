package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.ResumeField;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.client.Experience;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.client.education.ElementaryEducation;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import com.resume.util.BotUtil;
import com.resume.util.Constants;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.stream.Collectors;

import static com.resume.util.BotUtil.appendToField;
import static com.resume.util.Constants.*;

@Slf4j
@RequiredArgsConstructor
public class CreateResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;
    private boolean isPrimaryEdu = true;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        Map<String, String> userData = BotUtil.userResumeData.get(chatId);

        switch (callbackData) {
            case "create_resume" -> {
                BotUtil.userStates.put(chatId, BotState.CREATE_RESUME);
                List<String> buttonLabels = Arrays.asList("У меня нет резюме, хочу его сделать с нуля!", "Экспорт данных с hh.ru", "Назад");
                List<String> buttonIds = Arrays.asList("create_resume_from_scratch", "export_data_hh", "back_to_menu");

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Супер! С чего начнем?:sparkles:\n"), messageId, chatId, buttonLabels, buttonIds);
            }
            case "create_resume_from_scratch" -> {
                BotUtil.userStates.put(chatId, BotState.CREATE_RESUME_SCRATCH);
                List<String> buttonLabels = Arrays.asList("Начать", "Назад");
                List<String> buttonIds = Arrays.asList("start_dialogue", "back_to_create_resume");

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Отлично! Давайте заполним поля в вашем резюме.:lower_left_fountain_pen:"), messageId, chatId, buttonLabels, buttonIds);
            }
            case "start_dialogue" -> {
                BotUtil.userStates.put(chatId, BotState.START_DIALOGUE);
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_NAME);

                sendMessage("Введите имя:", chatId);
            }
            case "want_enter_education" -> {
                String educationLevel = userData.get(ResumeField.EDUCATION_LEVEL.getValue());
                if (educationLevel != null) {
                    processOnChosenLevelEducation(educationLevel, chatId, userData);
                } else {
                    executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Выберите уровень образования.:nerd:"),
                            messageId, chatId, educationLevels.values().stream().sorted().toList(), educationLevels.keySet().stream().sorted().toList());
                }
            }
            case "skip_education" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_work_experience", "skip_work_experience");

                executeEditMessageWithKeyBoard("Хотите ли Вы указать опыт работы?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_work_experience" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_PERIOD_OF_WORK);

                executeEditMessage(EmojiParser.parseToUnicode("Введите период опыта работы в формате ММ-ГГГГ - ММ-ГГГГ.:necktie:"),
                        messageId, chatId);
            }
            case "skip_work_experience" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_skills", "skip_skills");

                executeEditMessageWithKeyBoard("Хотите ли Вы указать навыки?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_skills" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_SKILLS);
                sendMessage("Введите навыки:", chatId);
            }
            case "skip_skills" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_about_me", "skip_about_me");

                executeEditMessageWithKeyBoard("Хотите ли Вы рассказать о себе?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_about_me" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_ABOUT_ME);

                executeEditMessage(EmojiParser.parseToUnicode("Введите краткую информацию о себе.:eyes: (Не больше 4096 символов)"),
                        messageId, chatId);
            }
            case "skip_about_me" -> {
                List<String> buttonLabels = List.of("Да", "Нет");
                List<String> buttonIds = List.of("yes_enter_car", "no_enter_car");

                executeEditMessageWithKeyBoard("Есть ли у вас автомобиль?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "yes_enter_car" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_CAR_AVAILABILITY);

                List<String> keys = new ArrayList<>(driverLicenseTypes.keySet().stream().sorted().toList());
                List<String> values = new ArrayList<>(driverLicenseTypes.values().stream().sorted().toList());
                keys.add("no_enter_car");
                values.add("Продолжить");
                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Выберите категорию прав.:car:"),
                        messageId, chatId, values, keys);
            }
            case "no_enter_car" -> {
                System.out.println(userData.get(ResumeField.DRIVER_LICENCE.getValue()));
                // todo переходим к рекомендации человека
            }
            case "want_enter_salary" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_SALARY);

                executeEditMessage(EmojiParser.parseToUnicode("Введите желаемую зарплату в виде числа.:moneybag:"),
                        messageId, chatId);
            }
            case "skip_salary" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_BUSYNESS);

                executeEditMessageWithKeyBoard("Выберите желаемую занятость.", messageId, chatId,
                        employmentTypes.values().stream().toList(), employmentTypes.keySet().stream().toList());
            }
            case "edit_result_data" -> {
                BotUtil.userStates.put(chatId, BotState.EDIT_CLIENT_RESULT_DATA);

                sendMessage(EmojiParser.parseToUnicode("Жду исправлений:eyes:"), chatId);
            }
            case "result_data_is_correct" -> {
                BotUtil.userStates.put(chatId, BotState.RESULT_DATA_CORRECT);
                fillClientData(chatId);
                sendMessage(EmojiParser.parseToUnicode("Замечательно! Ваши данные успешно получены.:sparkles:\nВот что Вы можете сделать:"), chatId);
                // todo кнопки "Загрузить новое резюме на hh", "Выбор Latex-шаблона"
            }
            case "back_to_menu" -> {
                List<String> buttonLabels = Arrays.asList("Создать резюме", "Экспорт резюме с hh.ru", "Мои резюме");
                List<String> buttonIds = Arrays.asList("create_resume", "export_resume_hh", "my_resumes");

                String menuInfo = "Выберите действие:\n\n"
                        + "*Создать резюме* :memo:\nНачните процесс создания нового резюме с нуля!\n\n"
                        + "*Экспорт резюме с hh.ru* :inbox_tray:\nЭкспортируйте свои данные с hh.ru для взаимодействия с ними.\n\n"
                        + "*Мои резюме* :clipboard:\nПосмотрите список ваших созданных резюме.";

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode(menuInfo), messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_create_resume" -> {
                List<String> buttonLabels = Arrays.asList("У меня нет резюме, хочу его сделать с нуля!", "Экспорт данных с hh.ru", "Назад");
                List<String> buttonIds = Arrays.asList("create_resume_from_scratch", "export_data_hh", "back_to_menu");

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Супер! С чего начнем?:sparkles:\n"), messageId, chatId, buttonLabels, buttonIds);
            }
        }

        if (sexTypes.containsKey(callbackData)) {
            String genderName = sexTypes.get(callbackData);
            processOnChosenSex(genderName, chatId, userData);
        }

        if (educationLevels.containsKey(callbackData)) {
            String chosenEducationLevel = educationLevels.get(callbackData);
            if (chosenEducationLevel.equals("Среднее")) {
                isPrimaryEdu = false;
            }
            processOnChosenLevelEducation(chosenEducationLevel, chatId, userData);
        }

        if (INDUSTRIES.stream().map(Industry::getName).toList().contains(callbackData)) {
            // todo логика после выбора сферы деятельности организации
            BotUtil.dialogueStates.put(chatId, BotState.ENTER_POST_IN_ORGANIZATION);
            sendMessage(EmojiParser.parseToUnicode("Введите свою должность:"), chatId);
        }

        if (driverLicenseTypes.containsKey(callbackData)) {
            processOnChosenDriverLicense(callbackData, chatId, userData);
            List<String> chosenDriverLicenses = Arrays.stream(userData.get(ResumeField.DRIVER_LICENCE.getValue()).split(ITEMS_DELIMITER)).toList();

            Map<String, String> unchosenDriverLicenseTypes = driverLicenseTypes.entrySet()
                    .stream().filter(license -> !chosenDriverLicenses.contains(license.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            List<String> keys = new ArrayList<>(unchosenDriverLicenseTypes.keySet().stream().sorted().toList());
            List<String> values = new ArrayList<>(unchosenDriverLicenseTypes.values().stream().sorted().toList());
            keys.add("no_enter_car");
            values.add("Продолжить");
            executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Выберите категорию прав.:car:"),
                    messageId, chatId, values, keys);
        }

        if (employmentTypes.containsKey(callbackData)) {
//            processOnChosenBusyness();
            // todo цикличность выбора занятости
        }
    }

    private void fillClientData(Long chatId) {
        Resume resume = BotUtil.clientsMap.getOrDefault(chatId, new Resume());
        Education education = new Education();
        List<PrimaryEducation> primaryEducations = new ArrayList<>();
        List<ElementaryEducation> elementaryEducations = new ArrayList<>();
        List<Experience> workExperiences = new ArrayList<>();

        for (Map.Entry<String, String> entry : BotUtil.userResumeData.get(chatId).entrySet()) {
            String fieldLabel = entry.getKey();
            String fieldValue = entry.getValue();

            switch (fieldLabel.toLowerCase()) {
                case "имя" -> resume.setFirstName(fieldValue);
                case "фамилия" -> resume.setLastName(fieldValue);
                case "отчество" -> resume.setMiddleName(fieldValue);
                case "пол" -> {
                    Id genderId = new Id();
                    genderId.setId(fieldValue.equals("Мужской") ? "male" : "female");
                    resume.setGender(genderId);
                }
                case "место жительства" -> {
                    Id areaId = new Id();
                    String[] items = fieldValue.split(",");
                    String cityName = items[items.length - 1].trim();
                    areaId.setId(getCityId(cityName));
                    resume.setArea(areaId);
                }
                case "уровень образования" -> {
                    String[] items = fieldValue.split(ITEMS_DELIMITER);
                    for (String item : items) {
                        Type typeEdu = new Type();
                        typeEdu.setId(getKeyByValue(educationLevels, item));
                        typeEdu.setName(item);
                        education.setLevel(typeEdu);
                    }
                }
                case "учебное заведение", "факультет", "специализация", "год окончания" -> {
                    List<String> items = Arrays.stream(fieldValue.split(ITEMS_DELIMITER)).toList();
                    initList(primaryEducations, PrimaryEducation.class, items.size());
                    initList(elementaryEducations, ElementaryEducation.class, items.size());

                    for (int i = 0; i < items.size(); i++) {
                        String item = items.get(i);
                        if (isPrimaryEdu) {
                            addPrimaryEducation(primaryEducations.get(i), fieldLabel, item);
                        } else {
                            addElementaryEducation(elementaryEducations.get(i), fieldLabel, item);
                        }
                    }
                }
                case "опыт работы", "название организации", "город организации", "ссылка", "должность", "обязанности" -> {
                    List<String> items = Arrays.stream(fieldValue.split(ITEMS_DELIMITER)).toList();
                    initList(workExperiences, Experience.class, items.size());

                    for (int i = 0; i < items.size(); i++) {
                        String item = items.get(i);
                        Experience workExperience = workExperiences.get(i);
                        processOnWorkExperience(workExperience, fieldLabel, item);
                    }
                }
                case "навыки" ->
                        resume.setSkills(fieldValue); // todo сюда должна приходить строка состоящая из нескольких навыков или одного
                case "категория прав" -> {

                }
                case "желаемая позиция" -> resume.setTitle(fieldValue);
                case "профессиональная роль" -> {

                }
                case "желаемая зарплата" -> {

                }
                case "желаемая занятость" -> {

                }
                case "желаемый график работы" -> {

                }
            }
        }

        education.setPrimary(primaryEducations);
        education.setElementary(elementaryEducations);

        resume.setEducation(education);
        resume.setExperience(workExperiences);

        JsonProcessor.createJsonFromEntity(resume);
    }

    private <T> void initList(List<T> listToInit, Class<T> type, int size) {
        if (listToInit.isEmpty()) {
            for (int i = 0; i < size; i++) {
                try {
                    listToInit.add(type.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    private void addPrimaryEducation(PrimaryEducation primaryEducation, String fieldLabel, String fieldValue) {
        switch (fieldLabel) {
            case "учебное заведение" -> primaryEducation.setName(fieldValue);
            case "факультет" -> primaryEducation.setOrganization(fieldValue);
            case "специализация" -> primaryEducation.setResult(fieldValue);
            case "год окончания" -> primaryEducation.setYear(Long.parseLong(fieldValue));
        }
    }

    private void addElementaryEducation(ElementaryEducation elementaryEducation, String fieldLabel, String fieldValue) {
        switch (fieldLabel) {
            case "учебное заведение" -> elementaryEducation.setName(fieldValue);
            case "год окончания" -> elementaryEducation.setYear(Long.parseLong(fieldValue));
        }
    }

    private String getCityId(String cityName) {
        Optional<Area> city1 = Constants.AREAS.stream().filter(a -> !a.getId().equals(OTHER_COUNTRIES_JSON_ID))
                .map(Area::getAreas).flatMap(List::stream).filter(a -> a.getAreas().isEmpty())
                .filter(area -> area.getName().equals(cityName)).findFirst();
        Optional<Area> city2 = Constants.AREAS.stream()
                .filter(a -> COUNTRIES_WITH_REGIONS_IDS.contains(a.getId()) || a.getId().equals(OTHER_COUNTRIES_JSON_ID))
                .map(Area::getAreas).flatMap(List::stream).map(Area::getAreas).flatMap(List::stream)
                .filter(area -> area.getName().equals(cityName)).findFirst();
        return city1.map(Area::getId).orElseGet(() -> city2.get().getId());
    }

    private void processOnChosenSex(String genderName, Long chatId, Map<String, String> userData) {
        userData.put(ResumeField.SEX.getValue(), genderName);
        BotUtil.userResumeData.put(chatId, userData);
        BotUtil.dialogueStates.put(chatId, BotState.ENTER_LOCATION);
        sendMessage("Введите ваше место жительства.\n" +
                "В формате *страна, регион (опционально), населенный пункт*:", chatId);
    }

    private void processOnChosenLevelEducation(String level, Long chatId, Map<String, String> userData) {
        userData.put(ResumeField.EDUCATION_LEVEL.getValue(), level);
        BotUtil.userResumeData.put(chatId, userData);
        sendMessage("Введите название учебного заведения:", chatId);
        BotUtil.dialogueStates.put(chatId, BotState.ENTER_INSTITUTION);
    }

    private void processOnChosenDriverLicense(String license, Long chatId, Map<String, String> userData) {
        appendToField(userData, ResumeField.DRIVER_LICENCE.getValue(), license);
        BotUtil.userResumeData.put(chatId, userData);
    }

    private void processOnChosenBusyness(String busyness, Long chatId, Map<String, String> userData) {
        // todo
    }

    private void processOnWorkExperience(Experience workExperience, String fieldLabel, String fieldValue) {
        switch (fieldLabel) {
            case "опыт работы" -> {
                String[] dates = fieldValue.split(EXPERIENCE_DELIMITER);
                workExperience.setStart(dates[0]);
                workExperience.setEnd(dates[1]);
            }
            case "название организации" -> workExperience.setCompany(fieldValue);
            case "город организации" -> {
                com.resume.bot.json.entity.client.Area area = new com.resume.bot.json.entity.client.Area(
                        getCityId(fieldValue), fieldValue);
                workExperience.setArea(area);
            }
            case "ссылка" -> workExperience.setCompanyUrl(fieldValue);
            case "должность" -> workExperience.setPosition(fieldValue);
            case "обязанности" -> workExperience.setDescription(fieldValue);
            // todo case "отрасль" -> workExperience.setIndustries();
            // пользователь должен протыкать на клавиатуре отрасли своей компании
        }
    }

    private void sendMessage(String text, Long chatId) {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.MARKDOWN);
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }

    private void executeEditMessage(String editMessageToSend, Integer messageId, Long chatId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setParseMode(ParseMode.MARKDOWN);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(editMessageToSend);

        executeMessage(editMessage);
    }

    private void executeEditMessageWithKeyBoard(String editMessageToSend, Integer messageId, Long chatId, List<String> buttonLabels, List<String> buttonIds) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setParseMode(ParseMode.MARKDOWN);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(editMessageToSend);

        editMessage.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, buttonIds));
        executeMessage(editMessage);
    }

    private void executeMessage(EditMessageText message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }

    private String getKeyByValue(Map<String, String> map, String fieldValue) {
        return map.entrySet().stream()
                .filter(entry -> fieldValue.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }
}
