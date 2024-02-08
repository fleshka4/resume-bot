package com.resume.util;

import com.resume.bot.display.BotState;
import com.resume.bot.json.entity.client.Resume;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

import static com.resume.util.Constants.ITEMS_DELIMITER;

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

            "back_to_menu",
            "back_to_create_resume",
            "yes_go_to_menu",
            "no_go_to_menu"
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
            "INDUSTRIES"
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
    public final Map<Long, Resume> clientsMap = new HashMap<>();
    public final String ERROR_TEXT = "Error occurred: ";
    public final Map<Long, Long> states = new HashMap<>(); // state, chatId
    public final Map <Long, String> personAndIndustryType = new HashMap<>(); // chatId, industryType
    public final Random random = new Random();

    public InlineKeyboardMarkup createInlineKeyboard(List<String> buttonLabels, List<String> callbackData) {
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

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup createInlineKeyboard(/*List<String> buttonLabels, List<String> callbackData, int pageSize, int pageNumber, */BigKeyboardType type, String cbData) {
        if (type == BigKeyboardType.INVALID) {
            throw new RuntimeException("BigKeyBoardType is " + type.name());
        }

        String prev = "prev_page_";
        String next = "next_page_";
        int pageSize = 5;
        List<String> buttonLabels = new ArrayList<>();
        List<String> callbackData = new ArrayList<>();
        int pageNumber = !cbData.isEmpty() ? Integer.parseInt(cbData.substring(type.name().length() + prev.length())) : 0;
        int maxSize = 0;
        switch (type) {
            case INDUSTRIES -> {
                maxSize = Constants.INDUSTRIES.size();
                if ((pageNumber + 1) * pageSize > maxSize) {
                    throw new RuntimeException("There is no such many industries");
                }

                for (int i = pageNumber * pageSize; i < (pageNumber + 1) * pageSize; i++) {
                    buttonLabels.add(String.valueOf(i));
                    callbackData.add(type.name() + "_" + i);
                }
            }
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        int startIndex = pageNumber * pageSize;
        int endIndex = (pageNumber + 1) * pageSize;

        for (int i = startIndex; i < endIndex; i++) {
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
            previousButton.setCallbackData(type.name() +  prev+ (pageNumber - 1));
            previousButtonRow.add(previousButton);
            rowList.add(previousButtonRow);
        }

        if (endIndex < maxSize) {
            List<InlineKeyboardButton> nextButtonRow = new ArrayList<>();
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText("След. страница");
            nextButton.setCallbackData(type.name() + next + (pageNumber + 1));
            nextButtonRow.add(nextButton);
            rowList.add(nextButtonRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
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
}
