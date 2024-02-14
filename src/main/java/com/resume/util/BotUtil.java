package com.resume.util;

import com.resume.bot.display.BotState;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.client.Experience;
import com.resume.bot.json.entity.client.Recommendation;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.Salary;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.hh_wrapper.config.HhConfig;
import com.vdurmont.emoji.EmojiParser;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

import static com.resume.bot.display.MessageUtil.sendMessage;
import static com.resume.util.Constants.ITEMS_DELIMITER;

@Slf4j
@UtilityClass
public class BotUtil {
    public final List<String> CREATE_RESUME_IDS_LIST = List.of(
            "menu",
            "create_resume",
            "create_resume_from_scratch",
            "start_dialogue",
            "edit_result_data",
            "result_data_is_correct",
            // education
            "want_enter_education",
            "skip_education",
            // experience
            "want_enter_work_experience",
            "skip_work_experience",
            // skills
            "want_enter_skills",
            "skip_skills",
            // about me
            "want_enter_about_me",
            "skip_about_me",
            // availability of a car
            "yes_enter_car",
            "no_enter_car",
            // person's recommendation
            "want_enter_rec",
            "skip_rec",
            "want_enter_salary",
            "skip_salary",
            "skip_busyness",
            "skip_schedule",
            // person's contacts
            "want_enter_contacts",

            "back_to_menu",
            "back_to_create_resume",
            "yes_go_to_menu",
            "no_go_to_menu"
    );
    public final List<String> CORRECT_DATA_IDS_LIST = List.of(
            "post_resume_to_hh",
            "choose_latex_for_resume"
    );
    public final List<String> MY_RESUMES_IDS_LIST = List.of(
            "my_resumes",
            "back_to_menu_3",
            "back_to_my_resumes",
            "edit_resume",
            "edit_resume_1",
            "edit_resume_2",
            "edit_resume_3",
            "edit_resume_4",
            "edit_resume_5",
            "edit_resume_6",
            "resume_1",
            "resume_2",
            "resume_3",
            "resume_4",
            "resume_5",
            "resume_6",
            "back_to_my_resumes"
    );
    public final List<String> BIG_TYPES_IDS = List.of(
            "INDUSTRIES",
            "PROFESSIONAL_ROLES"
    );
    public final List<String> ACTIONS_WITH_RESUME = List.of(
            "publish_on_hh",
            "finally_publish_on_hh",
            "back_to_publish_on_hh",
            "update",
            "download",
            "edit",
            "delete"
    );

    public final List<String> EXPORT_RESUME_IDS_LIST = List.of(
            "export_data_hh",
            "auth",
            "choose_resume"
    );

    public boolean checkIfAction(String suspectAction) {
        for (String s : ACTIONS_WITH_RESUME) {
            if (suspectAction.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfBigType(String suspectAction) {
        for (String s : BIG_TYPES_IDS) {
            if (suspectAction.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    public final Map<Long, BotState> userStates = new HashMap<>();
    public final Map<Long, Integer> pages = new HashMap<>();
    public final Map<Long, BotState> dialogueStates = new HashMap<>();
    public final Map<Long, Map<String, String>> userResumeData = new LinkedHashMap<>();
    public final Map<Long, com.resume.bot.model.entity.Resume> userMyResumeMap = new HashMap<>();
    public final Map<Long, Resume> clientsMap = new HashMap<>();
    public final Map<Long, com.resume.bot.model.entity.Resume> lastSavedResumeMap = new HashMap<>(); // chatId, last Resume which was created from 0
    public final String ERROR_TEXT = "Error occurred: ";
    public final Map<Long, Long> states = new HashMap<>(); // authorize: state, chatId
    public final Map<Long, String> personAndIndustryType = new HashMap<>(); // chatId, industryType
    public final Map<Long, String> personAndIndustry = new HashMap<>(); // chatId, industry
    public final Map<Long, String> personAndProfessionalRole = new HashMap<>(); // chatId, professionalRole
    public final Random random = new Random();

    public InlineKeyboardMarkup createInlineKeyboard(List<String> buttonLabels, List<String> callbackData, BigKeyboardType type) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (int i = 0; i < buttonLabels.size(); i++) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(buttonLabels.get(i));
            button.setCallbackData(callbackData.get(i));
            keyboardRow.add(button);

            rowList.add(keyboardRow);
        }

        if (type != BigKeyboardType.INVALID) {
            List<InlineKeyboardButton> nextButtonRow = new ArrayList<>();
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText("След. страница");
            nextButton.setCallbackData(type + "_next_page_" + 1);
            nextButtonRow.add(nextButton);
            rowList.add(nextButtonRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboard(List<String> buttonLabels, List<String> callbackData) {
        return createInlineKeyboard(buttonLabels, callbackData, BigKeyboardType.INVALID);
    }

    public InlineKeyboardMarkup createInlineBigKeyboard(List<String> buttonLabels, List<String> callbackData, int pageNumber, int maxSize, String type) {
        final String prev = "_prev_page_";
        final String next = "_next_page_";
        final int defaultPageSize = 5;
        final int pageSize = Math.min(buttonLabels.size(), defaultPageSize);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (int i = 0; i < pageSize; i++) {
            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            InlineKeyboardButton button = new InlineKeyboardButton();

            button.setText(buttonLabels.get(i));
            button.setCallbackData(callbackData.get(i));
            keyboardRow.add(button);

            rowList.add(keyboardRow);
        }

        if (pageNumber > 0) {
            List<InlineKeyboardButton> previousButtonRow = new ArrayList<>();
            InlineKeyboardButton previousButton = new InlineKeyboardButton();
            previousButton.setText("Пред. страница");
            previousButton.setCallbackData(type + prev + (pageNumber - 1));
            previousButtonRow.add(previousButton);
            rowList.add(previousButtonRow);
        }

        final int endIndex = (pageNumber + 1) * defaultPageSize;
        if (endIndex < maxSize) {
            List<InlineKeyboardButton> nextButtonRow = new ArrayList<>();
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText("След. страница");
            nextButton.setCallbackData(type + next + (pageNumber + 1));
            nextButtonRow.add(nextButton);
            rowList.add(nextButtonRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public void prepareBigKeyboardCreation(int pageNumber, BigKeyboardType bigKeyboardType, StringBuilder message, List<String> buttonLabels, List<String> callbackDataList) {
        final int pageSize = 5;
        switch (bigKeyboardType) {
            case INDUSTRIES, PROFESSIONAL_ROLES -> {
                final boolean isIndustry = bigKeyboardType == BigKeyboardType.INDUSTRIES;
                final int maxSize = isIndustry ?
                        Constants.INDUSTRIES.size() :
                        Constants.PROFESSIONAL_ROLES.getCategories().size();

                final int startCounter = pageNumber * pageSize;
                int limit = (pageNumber + 1) * pageSize;
                if ((pageNumber + 1) * pageSize > maxSize) {
                    limit = startCounter + pageSize - (limit - maxSize);
                }

                for (int i = startCounter; i < limit; i++) {
                    buttonLabels.add(String.valueOf(i + 1));
                    callbackDataList.add(bigKeyboardType.name() + "_" + i);
                    message.append((i + 1))
                            .append(". ")
                            .append(isIndustry ?
                                    Constants.INDUSTRIES.get(i).getName() :
                                    Constants.PROFESSIONAL_ROLES.getCategories().get(i).getName());
                    if (i != (pageNumber + 1) * pageSize - 1) {
                        message.append('\n');
                    }
                }
            }
        }
    }

    public static long generateRandom12DigitNumber(Random random) {
        return (long) (Math.pow(10, 11) + random.nextInt((int) Math.pow(10, 11)));
    }

    public static String appendToField(Map<String, String> resumeFields, String key, String value) {
        if (resumeFields.containsKey(key)) {
            String oldText = resumeFields.get(key);
            return resumeFields.put(key, oldText + ITEMS_DELIMITER + value);
        } else {
            return resumeFields.put(key, value);
        }
    }

    public static SortedMap<String, String> createSortedMap(Map<String, String> originalMap) {
        return new TreeMap<>(originalMap);
    }

    public void authorization(TelegramLongPollingBot bot, HhConfig hhConfig, String message, Long chatId) {
        long randomValue = BotUtil.generateRandom12DigitNumber(BotUtil.random);
        while (BotUtil.states.containsKey(randomValue)) {
            randomValue = BotUtil.generateRandom12DigitNumber(BotUtil.random);
        }

        BotUtil.states.put(randomValue, chatId);

        String link = "https://hh.ru/oauth/authorize?" +
                "response_type=code&" +
                "client_id=" + hhConfig.getClientId() +
                "&state=" + randomValue +
                "&redirect_uri=http://localhost:5000/hh/auth";

        sendMessage(bot, EmojiParser.parseToUnicode(message.formatted(link)), chatId);
    }

    public void publishResume(com.resume.bot.model.entity.Resume resume, Long chatId, ResumeService resumeService, HeadHunterService headHunterService, TelegramLongPollingBot bot, String hhBaseUrl, boolean fromMyResumes) {
        if (resume != null) {
            try {
                String hhLink = headHunterService.postCreateClient(hhBaseUrl, chatId,
                        JsonProcessor.createEntityFromJson(resume.getResumeData(), com.resume.bot.json.entity.client.Resume.class));
                resumeService.updateHhLinkByResumeId(hhLink, resume.getResumeId());
            } catch (Exception exception) {
                log.error(exception.getMessage());
                String message = "Произошла ошибка при публикации резюме. " +
                        "Попробуйте удалить его и создать заново" + (!fromMyResumes ? "" :  "или выберите другое");
                sendMessage(bot, message, chatId);
            }
        }
    }

    public com.resume.bot.model.entity.Resume getResume(String string, ResumeService resumeService, Long chatId, TelegramLongPollingBot bot) {
        int numOfResume = getResumeNumber(string);
        List<com.resume.bot.model.entity.Resume> resumes = resumeService.getResumesByUserId(chatId);
        if (resumes.size() >= numOfResume) {
            return resumes.get(numOfResume - 1);
        }
        sendMessage(bot, "Резюме не найдено. Попробуйте снова", chatId);
        return null;
    }

    public int getResumeNumber(String string) {
        String[] s = string.split("_");
        return Integer.parseInt(s[s.length - 1]);
    }

    public String askToEditMyResume(com.resume.bot.model.entity.Resume resume, Long chatId) {
        Resume res = JsonProcessor.createEntityFromJson(resume.getResumeData(), Resume.class);

        Area area = JsonValidator.getAreaByIdDeep(Constants.AREAS, res.getArea().getId()).orElse(null);
        StringBuilder areaSB = new StringBuilder();
        while (area != null) {
            areaSB.insert(0, area.getName()).insert(0, area.getParentId() != null ? ", " : "");
            area = JsonValidator.getAreaByIdDeep(Constants.AREAS, area.getParentId()).orElse(null);
        }
        String areaStr = areaSB.toString();

        Education education = res.getEducation();
        String educStr = education != null && !education.getPrimary().isEmpty()
                ? education.getPrimary().stream().map(ed -> """
                        уровень образования: %s
                        учебное заведение: %s
                        факультет: %s
                        специализация: %s
                        год окончания: %d
                        """.formatted(
                        education.getLevel().getName(),
                        ed.getName(),
                        ed.getOrganization(),
                        ed.getResult(),
                        ed.getYear()))
                .collect(Collectors.joining("\n"))
                : "";

        List<Experience> experience = res.getExperience();
        String expStr = experience != null && !experience.isEmpty()
                ? experience.stream().map(exp -> """
                        опыт работы: %s
                        название организации: %s
                        город организации: %s
                        ссылка: %s
                        отрасль: %s
                        должность: %s
                        обязанности: %s
                                                
                        """.formatted(
                        exp.getStart() + (exp.getEnd() != null ? " - %s".formatted(exp.getEnd()) : ""),
                        exp.getCompany(),
                        exp.getArea().getName(),
                        exp.getCompanyUrl(),
                        exp.getIndustries().stream().map(Type::getName).collect(Collectors.joining(", ")),
                        exp.getPosition(),
                        exp.getDescription()))
                .collect(Collectors.joining("\n"))
                : "";

        List<Recommendation> recommendation = res.getRecommendation();
        String recStr = recommendation != null && !recommendation.isEmpty()
                ? recommendation.stream().map(rec -> """
                        имя выдавшего рекомендацию: %s
                        должность выдавшего рекомендацию: %s
                        организация выдавшего рекомендацию: %s
                                                        
                                """.formatted(
                        rec.getName(),
                        rec.getPosition(),
                        rec.getOrganization()
                ))
                .collect(Collectors.joining("\n"))
                : "";

        Salary salary = res.getSalary();
        String salStr = "%d %s".formatted(salary.getAmount(), salary.getCurrency());

        List<Type> employment = res.getEmployments();
        String empStr = employment.stream().map(Type::getName)
                .collect(Collectors.joining("", "желаемая занятость: ", "\n"));

        List<Type> schedule = res.getSchedules();
        String schedStr = schedule.stream().map(Type::getName)
                .collect(Collectors.joining("", "желаемый график: ", "\n"));

        String outInfo = """
                Ваше резюме:
                                
                Основная информация
                                
                имя: %s
                отчество: %s
                фамилия: %s
                дата рождения: %s
                пол: %s
                место жительства: %s
                                
                %s
                %s
                Дополнительная информация
                                
                навыки: %s
                о себе: %s
                наличие авто: %s
                категория прав: %s
                  
                %s
                желаемая позиция: %s
                профессиональная роль: %s
                желаемая зарплата: %s
                %s
                %s
                контакты: %s
                    
                Присылайте исправления в формате *Поле - новое значение*

                *Пример:*
                Имя - Алексей
                """
                .formatted(
                        res.getFirstName(),
                        res.getMiddleName(),
                        res.getLastName(),
                        res.getBirthDate(),
                        Constants.sexTypes.get(res.getGender().getId()),
                        areaStr,
                        !educStr.isEmpty() ? "Образование\n\n" + educStr : "",
                        !expStr.isEmpty() ? "Опыт\n\n" + expStr : "",
                        String.join(", ", res.getSkillSet()),
                        res.getSkills(),
                        res.getHasVehicle() ? "да" : "нет",
                        String.join(", ", res.getDriverLicenseTypes().stream().map(Id::getId).toList()),
                        !recStr.isEmpty() ? "Список рекомендаций\n\n" + recStr : "",
                        res.getTitle(),
                        Constants.PROFESSIONAL_ROLES.getCategories().stream()
                                .filter(cat -> cat.getId().equals(res.getProfessionalRoles().getFirst().getId()))
                                .findFirst().get().getName(),
                        salStr,
                        empStr,
                        schedStr,
                        res.getContacts()
                );

        BotUtil.userMyResumeMap.put(chatId, resume);
        return outInfo;
    }
}
