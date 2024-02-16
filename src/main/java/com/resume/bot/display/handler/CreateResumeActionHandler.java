package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.ResumeField;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.*;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.client.education.ElementaryEducation;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import com.resume.bot.json.entity.roles.Category;
import com.resume.bot.service.ResumeService;
import com.resume.bot.service.UserService;
import com.resume.util.BotUtil;
import com.resume.util.Constants;
import com.resume.util.ConstantsUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.resume.bot.display.MessageUtil.*;
import static com.resume.util.BotUtil.appendToField;
import static com.resume.util.Constants.*;
import static org.apache.commons.lang3.math.NumberUtils.createLong;

@Slf4j
@RequiredArgsConstructor
public class CreateResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    private boolean isPrimaryEdu = true;

    private final ResumeService resumeService;

    private final UserService userService;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        Map<String, String> userData = BotUtil.userResumeData.get(chatId);

        switch (callbackData) {
            case "create_resume" -> {
                BotUtil.userStates.put(chatId, BotState.CREATE_RESUME);
                List<String> buttonLabels = Arrays.asList("У меня нет резюме, хочу его сделать с нуля!", "Экспорт данных с hh.ru", "Назад");
                List<String> buttonIds = Arrays.asList("create_resume_from_scratch", "export_data_hh", "back_to_menu");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Супер! С чего начнем?:sparkles:\n"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            case "create_resume_from_scratch" -> {
                BotUtil.userStates.put(chatId, BotState.CREATE_RESUME_SCRATCH);
                List<String> buttonLabels = Arrays.asList("Начать", "Назад");
                List<String> buttonIds = Arrays.asList("start_dialogue", "back_to_create_resume");

                executeEditMessageWithKeyBoard(bot,
                        EmojiParser.parseToUnicode("Отлично! Давайте заполним поля в вашем резюме.:lower_left_fountain_pen:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            case "start_dialogue" -> {
                BotUtil.userStates.put(chatId, BotState.START_DIALOGUE);
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_NAME);
                sendMessage(bot, "Введите имя:", chatId);
            }
            case "want_enter_education" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_INSTITUTION);
                BotUtil.userResumeData.put(chatId, userData);
                sendMessage(bot, "Введите название учебного заведения:", chatId);
            }
            case "skip_education" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_work_experience", "skip_work_experience");

                executeEditMessageWithKeyBoard(bot, "Хотите ли Вы указать опыт работы?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_work_experience" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_PERIOD_OF_WORK);
                sendMessage(bot,
                        EmojiParser.parseToUnicode("""
                                Введите период опыта работы в формате ММ-ГГГГ - ММ-ГГГГ::necktie:
                                Если вы работаете по настоящее время введите только дату начала работы"""), chatId);
            }
            case "skip_work_experience" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_skills", "skip_skills");

                executeEditMessageWithKeyBoard(bot, "Хотите ли Вы указать навыки?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_skills" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_SKILLS);
                sendMessage(bot, "Введите навыки через запятую:", chatId);
            }
            case "skip_skills" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_about_me", "skip_about_me");

                executeEditMessageWithKeyBoard(bot, "Хотите ли Вы рассказать о себе?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_about_me" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_ABOUT_ME);

                executeEditMessage(bot,
                        EmojiParser.parseToUnicode("Введите краткую информацию о себе::eyes: (Не больше 4096 символов)"),
                        messageId, chatId);
            }
            case "skip_about_me" -> {
                List<String> buttonLabels = List.of("Да", "Нет");
                List<String> buttonIds = List.of("yes_enter_car", "no_enter_car");

                executeEditMessageWithKeyBoard(bot, "Есть ли у вас автомобиль?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "yes_enter_car" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_CAR_AVAILABILITY);

                executeContinueKeyboardMessage(bot, EmojiParser.parseToUnicode("Выберите категорию прав.:car:"),
                        messageId, chatId, driverLicenseTypes, "no_enter_car");
            }
            case "no_enter_car" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_rec", "skip_rec");

                executeEditMessageWithKeyBoard(bot,
                        EmojiParser.parseToUnicode("""
                                Хотите ли Вы добавить список рекомендаций?

                                Список рекомендаций - это перечень людей, которые высоко оценивают вас в качестве профессионала или сотрудника.:first_place_medal:"""),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_rec" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_REC_NAME);
                sendMessage(bot, "Введите ФИО:", chatId);
            }
            case "skip_rec" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_POSITION);
                sendMessage(bot, "Введите желаемую позицию:", chatId);
            }
            case "want_enter_salary" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_SALARY);
                executeEditMessage(bot, EmojiParser.parseToUnicode("Введите желаемую зарплату в виде числа::moneybag:"),
                        messageId, chatId);
            }
            case "skip_salary" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_BUSYNESS);
                executeContinueKeyboardMessage(bot, "Выберите желаемую занятость.",
                        messageId, chatId, employmentTypes, "skip_busyness");
            }
            case "skip_busyness" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_SCHEDULE);
                executeContinueKeyboardMessage(bot, "Выберите желаемый график.",
                        messageId, chatId, scheduleTypes, "skip_schedule");
            }
            case "skip_schedule" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_CONTACTS);

                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_contacts", "skip_contacts");
                executeEditMessageWithKeyBoard(bot, "Хотите ли Вы указать контактную информацию?",
                        messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_contacts" -> {
                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите желаемый тип связи.:telephone_receiver:"),
                        messageId, chatId, contactTypes.values().stream().toList(), contactTypes.keySet().stream().toList());
            }
            case "home" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_HOME_PHONE);
                executeEditMessage(bot, EmojiParser.parseToUnicode("Введите домашний номер телефона в формате 7ХХХХХХХХХХ или 8ХХХХХХХХХХ:"),
                        messageId, chatId);
            }
            case "work" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WORK_PHONE);
                executeEditMessage(bot, EmojiParser.parseToUnicode("Введите рабочий номер телефона в формате 7ХХХХХХХХХХ или 8ХХХХХХХХХХ:"),
                        messageId, chatId);
            }
            case "cell" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_PHONE);
                executeEditMessage(bot, EmojiParser.parseToUnicode("Введите мобильный номер телефона в формате 7ХХХХХХХХХХ или 8ХХХХХХХХХХ:"),
                        messageId, chatId);
            }
            case "email" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_MAIL);
                executeEditMessage(bot, EmojiParser.parseToUnicode("Введите электронную почту:"),
                        messageId, chatId);
            }
            case "edit_result_data" -> {
                BotUtil.userStates.put(chatId, BotState.EDIT_CLIENT_RESULT_DATA);

                sendMessage(bot, EmojiParser.parseToUnicode("Жду исправлений:eyes:"), chatId);
            }
            case "result_data_is_correct" -> {
                BotUtil.userStates.put(chatId, BotState.RESULT_DATA_CORRECT);
                fillClientData(chatId);

                List<String> buttonLabels = Arrays.asList("Загрузить новое резюме на hh", "Выбор Latex-шаблона");
                List<String> buttonIds = Arrays.asList("post_resume_to_hh", "choose_latex_for_resume");
                executeEditMessageWithKeyBoard(bot,
                        EmojiParser.parseToUnicode("Замечательно! Ваши данные успешно получены.:sparkles:\nВот что Вы можете сделать:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_menu" -> {
                List<String> buttonLabels = Arrays.asList("Создать резюме", "Использовать резюме с hh.ru", "Мои резюме");
                List<String> buttonIds = Arrays.asList("create_resume", "export_resume_hh", "my_resumes");

                String menuInfo = """
                        Выберите действие:

                        *Создать резюме* :memo:
                        Начните процесс создания нового резюме с нуля!

                        *Экспорт резюме с hh.ru* :inbox_tray:
                        Экспортируйте свои данные с hh.ru для взаимодействия с ними.

                        *Мои резюме* :clipboard:
                        Посмотрите список ваших созданных резюме.""";

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode(menuInfo), messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_create_resume" -> {
                List<String> buttonLabels = Arrays.asList("У меня нет резюме, хочу его сделать с нуля!", "Экспорт данных с hh.ru", "Назад");
                List<String> buttonIds = Arrays.asList("create_resume_from_scratch", "export_data_hh", "back_to_menu");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Супер! С чего начнем?:sparkles:\n"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
        }

        if (sexTypes.containsKey(callbackData)) {
            String genderName = sexTypes.get(callbackData);
            processOnChosenSex(genderName, chatId, userData);
        }

        if (educationLevels.containsKey(callbackData)) {
            String chosenEducationLevel = educationLevels.get(callbackData);
            appendToField(userData, ResumeField.EDUCATION_LEVEL.getValue(), chosenEducationLevel);
            if (chosenEducationLevel.equals("Среднее")) {
                isPrimaryEdu = false;
            }
            List<String> buttonLabels = List.of("Хочу", "Пропустить");
            List<String> buttonIds = List.of("want_enter_education", "skip_education");

            executeEditMessageWithKeyBoard(bot,  "Хотите ли Вы указать место учебы?",
                    messageId, chatId, buttonLabels, buttonIds);
        }

        if (driverLicenseTypes.containsKey(callbackData)) {
            processOnChosenDriverLicense(callbackData, chatId, userData);

            List<String> chosenDriverLicenses = Arrays.stream(userData.get(ResumeField.DRIVER_LICENCE.getValue()).split(ITEMS_DELIMITER)).toList();
            Map<String, String> unChosenDriverLicenseTypes = getUnChosenItems(driverLicenseTypes, chosenDriverLicenses);

            executeContinueKeyboardMessage(bot, EmojiParser.parseToUnicode("Выберите категорию прав.:car:"),
                    messageId, chatId, unChosenDriverLicenseTypes, "no_enter_car");
        }

        if (employmentTypes.containsKey(callbackData)) {
            processOnChosenBusyness(employmentTypes.get(callbackData), chatId, userData);

            List<String> chosenBusyness = Arrays.stream(userData.get(ResumeField.BUSYNESS.getValue()).split(ITEMS_DELIMITER)).toList();
            Map<String, String> unChosenBusyness = getUnChosenItemsByValue(employmentTypes, chosenBusyness);

            executeContinueKeyboardMessage(bot, "Выберите желаемую занятость.", messageId, chatId, unChosenBusyness, "skip_busyness");
        }

        if (scheduleTypes.containsKey(callbackData)) {
            processOnChosenSchedule(scheduleTypes.get(callbackData), chatId, userData);

            List<String> chosenSchedule = Arrays.stream(userData.get(ResumeField.SCHEDULE.getValue()).split(ITEMS_DELIMITER)).toList();
            Map<String, String> unChosenSchedule = getUnChosenItemsByValue(scheduleTypes, chosenSchedule);

            executeContinueKeyboardMessage(bot, "Выберите желаемый график.", messageId, chatId, unChosenSchedule, "skip_schedule");
        }

        if (INDUSTRIES.stream()
                .filter(ind -> ind.getName().equals(BotUtil.personAndIndustry.get(chatId)))
                .findFirst().get().getIndustries()
                .stream().anyMatch(ind -> ind.getId().equals(callbackData))
        ) {
            processOnChosenIndustry(callbackData, chatId, userData);
            BotUtil.dialogueStates.put(chatId, BotState.ENTER_POST_IN_ORGANIZATION);
            executeEditMessage(bot, "Введите свою должность в организации:", messageId, chatId);
        }
    }

    private void fillClientData(Long chatId) {
        Resume resume = BotUtil.clientsMap.getOrDefault(chatId, new Resume());
        Education education = new Education();
        List<PrimaryEducation> primaryEducations = new ArrayList<>();
        List<ElementaryEducation> elementaryEducations = new ArrayList<>();
        List<Experience> workExperiences = new ArrayList<>();
        List<Id> driverLicenseTypes = new ArrayList<>();
        Recommendation recommendation = new Recommendation();
        List<Recommendation> recommendationList = new ArrayList<>();
        List<Type> busyness = new ArrayList<>();
        List<Type> schedules = new ArrayList<>();

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
                    areaId.setId(ConstantsUtil.getAreaByNameDeep(Constants.AREAS, cityName).get().getId());
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
                    if (isPrimaryEdu) {
                        elementaryEducations = null;
                    } else {
                        primaryEducations = null;
                    }
                    if (items.isEmpty()) {
                        primaryEducations = null;
                        elementaryEducations = null;
                    }

                    for (int i = 0; i < items.size(); i++) {
                        String item = items.get(i);
                        if (isPrimaryEdu) {
                            addPrimaryEducation(primaryEducations.get(i), fieldLabel, item);
                        } else {
                            addElementaryEducation(elementaryEducations.get(i), fieldLabel, item);
                        }
                    }
                }
                case "опыт работы", "название организации", "город организации", "ссылка", "отрасль", "должность", "обязанности" -> {
                    List<String> items = Arrays.stream(fieldValue.split(ITEMS_DELIMITER)).toList();
                    initList(workExperiences, Experience.class, items.size());
                    if (items.isEmpty()) {
                        workExperiences = null;
                    }

                    for (int i = 0; i < items.size(); i++) {
                        String item = items.get(i);
                        Experience workExperience = workExperiences.get(i);
                        processOnWorkExperience(workExperience, fieldLabel, item, chatId);
                    }
                }
                case "навыки" ->
                        resume.setSkillSet(Arrays.stream(fieldValue.split(",")).map(String::trim).collect(Collectors.toSet()));
                case "категория прав" -> {
                    List<String> items = Arrays.stream(fieldValue.split(ITEMS_DELIMITER)).toList();
                    //initList(driverLicenseTypes, Id.class, items.size());
                    if (items.isEmpty()) {
                        resume.setHasVehicle(false);
                        driverLicenseTypes = null;
                    } else {
                        resume.setHasVehicle(true);
                    }

                    for (String item : items) {
                        Id idLicense = new Id();
                        idLicense.setId(item);
                        driverLicenseTypes.add(idLicense);
                    }
                }
                case "имя выдавшего рекомендацию", "должность выдавшего рекомендацию", "организация выдавшего рекомендацию" -> {
                    List<String> items = Arrays.stream(fieldValue.split(ITEMS_DELIMITER)).toList();
                    //initList(recommendationList, Recommendation.class, items.size());
                    if (items.isEmpty()) {
                        recommendationList = null;
                    }

                    for (String item : items) {
                        processOnRecommendations(recommendation, fieldLabel, item);
                        recommendationList.add(recommendation);
                    }
                }
                case "желаемая позиция" -> resume.setTitle(fieldValue);
                case "профессиональная роль" -> {
                    if (fieldValue != null) {
                        String roleId = null;
                        for (Category category : Constants.PROFESSIONAL_ROLES.getCategories()) {
                            if (category.getName().equals(fieldValue)) {
                                roleId = category.getId();
                                break;
                            }
                        }
                        if (roleId == null) {
                            throw new RuntimeException("Professional role is null");
                        }
                        resume.setProfessionalRoles(List.of(new Id(roleId)));
                    }
                }
                case "желаемая зарплата" -> {
                    Salary salary = new Salary();
                    salary.setAmount(createLong(fieldValue));
                    resume.setSalary(salary);
                }
                case "желаемая занятость" -> {
                    List<String> items = Arrays.stream(fieldValue.split(ITEMS_DELIMITER)).toList();
                    //initList(busyness, Type.class, items.size());

                    for (String item : items) {
                        Type typeBusyness = new Type();
                        typeBusyness.setId(getKeyByValue(employmentTypes, item));
                        typeBusyness.setName(item);
                        busyness.add(typeBusyness);
                    }
                }
                case "желаемый график" -> {
                    List<String> items = Arrays.stream(fieldValue.split(ITEMS_DELIMITER)).toList();
                    //initList(schedules, Type.class, items.size());

                    for (String item : items) {
                        Type typeSchedules = new Type();
                        typeSchedules.setId(getKeyByValue(scheduleTypes, item));
                        typeSchedules.setName(item);
                        schedules.add(typeSchedules);
                    }
                }
                case "контакты" -> resume.setContacts(fieldValue);
            }
        }

        if (busyness.isEmpty()) {
            busyness = null;
        }
        if (schedules.isEmpty()) {
            schedules = null;
        }
        if (resume.getHasVehicle() == null) {
            resume.setHasVehicle(false);
        }

        education.setPrimary(primaryEducations);
        education.setElementary(elementaryEducations);

        resume.setEducation(education);
        resume.setExperience(workExperiences);
        resume.setDriverLicenseTypes(driverLicenseTypes);
        resume.setRecommendation(recommendationList);
        resume.setEmployments(busyness);
        resume.setSchedules(schedules);

        resume.setLanguage(List.of(new Language("rus", "Русский", new Type("l1", "Родной"))));

        BotUtil.clientsMap.put(chatId, resume);

        com.resume.bot.model.entity.Resume dbResume = new com.resume.bot.model.entity.Resume();
        dbResume.setUser(userService.getUser(chatId));
        dbResume.setTitle(resume.getTitle());
        dbResume.setResumeData(JsonProcessor.createJsonFromEntity(resume));
        BotUtil.lastSavedResumeMap.put(chatId, resumeService.saveResume(dbResume));
    }

    private <T> void initList(List<T> listToInit, Class<T> type, int size) {
        if (listToInit != null && listToInit.isEmpty()) {
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

    private void processOnChosenSex(String genderName, Long chatId, Map<String, String> userData) {
        appendToField(userData, ResumeField.SEX.getValue(), genderName);
        BotUtil.userResumeData.put(chatId, userData);
        BotUtil.dialogueStates.put(chatId, BotState.ENTER_LOCATION);
        sendMessage(bot, """
                Введите ваше место жительства.
                В формате *страна, регион (опционально), населенный пункт*:""", chatId);
    }

    private void processOnChosenDriverLicense(String license, Long chatId, Map<String, String> userData) {
        appendToField(userData, ResumeField.DRIVER_LICENCE.getValue(), license);
        BotUtil.userResumeData.put(chatId, userData);
    }

    private void processOnChosenBusyness(String busyness, Long chatId, Map<String, String> userData) {
        appendToField(userData, ResumeField.BUSYNESS.getValue(), busyness);
        BotUtil.userResumeData.put(chatId, userData);
    }

    private void processOnChosenSchedule(String schedule, Long chatId, Map<String, String> userData) {
        appendToField(userData, ResumeField.SCHEDULE.getValue(), schedule);
        BotUtil.userResumeData.put(chatId, userData);
    }

    private void processOnChosenIndustry(String industry, Long chatId, Map<String, String> userData) {
        appendToField(userData, ResumeField.EXPERIENCE_ORG_INDUSTRY.getValue(), industry);
        BotUtil.userResumeData.put(chatId, userData);
    }

    private void processOnRecommendations(Recommendation recommendation, String fieldLabel, String fieldValue) {
        switch (fieldLabel) {
            case "имя выдавшего рекомендацию" -> recommendation.setName(fieldValue);
            case "должность выдавшего рекомендацию" -> recommendation.setPosition(fieldValue);
            case "организация выдавшего рекомендацию" -> recommendation.setOrganization(fieldValue);
        }
    }

    private void processOnWorkExperience(Experience workExperience, String fieldLabel, String fieldValue, Long chatId) {
        switch (fieldLabel) {
            case "опыт работы" -> {
                String[] dates = fieldValue.split(EXPERIENCE_DELIMITER);
                workExperience.setStart(dates[0]);
                if (dates.length == 2) {
                    workExperience.setEnd(dates[1]);
                }
            }
            case "название организации" -> workExperience.setCompany(fieldValue);
            case "город организации" -> {
                com.resume.bot.json.entity.client.Area area = new com.resume.bot.json.entity.client.Area(
                        ConstantsUtil.getAreaByNameDeep(Constants.AREAS, fieldValue).get().getId(), fieldValue);
                workExperience.setArea(area);
            }
            case "ссылка" -> workExperience.setCompanyUrl(fieldValue);
            case "должность" -> workExperience.setPosition(fieldValue);
            case "обязанности" -> workExperience.setDescription(fieldValue);
            case "отрасль" -> {
                Type industry = INDUSTRIES.stream()
                        .filter(ind -> ind.getName().equals(BotUtil.personAndIndustry.get(chatId)))
                        .findFirst().get().getIndustries()
                        .stream().filter(ind -> ind.getId().equals(fieldValue)).findFirst().get();
                workExperience.setIndustries(List.of(industry));
            }
        }
    }

    private String getKeyByValue(Map<String, String> map, String fieldValue) {
        return map.entrySet().stream()
                .filter(entry -> fieldValue.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }

    private Map<String, String> getUnChosenItems(Map<String, String> allItems, List<String> chosenItems) {
        return allItems.entrySet()
                .stream()
                .filter(item -> !chosenItems.contains(item.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<String, String> getUnChosenItemsByValue(Map<String, String> allItems, List<String> chosenItems) {
        return allItems.entrySet()
                .stream()
                .filter(item -> !chosenItems.contains(item.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
