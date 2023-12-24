package com.resume.util;

import com.resume.bot.display.BotState;
import com.resume.bot.json.entity.client.Resume;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@UtilityClass
public class BotUtil {
    public final List<String> CREATE_RESUME_IDS_LIST = List.of(
            "menu",
            "create_resume",
            "create_resume_from_scratch",
            "export_data_hh",
            "start_dialogue",
            "edit_result_data",
            "result_data_is_correct",
            // education
            "want_enter_education",
            "skip_education",
            // experience
            "want_enter_work_experience",
            "skip_work_experience",

            "back_to_menu",
            "back_to_create_resume",
            "yes_go_to_menu",
            "no_go_to_menu"
    );
    public final Map<Long, BotState> userStates = new HashMap<>();
    public final Map<Long, BotState> dialogueStates = new HashMap<>();
    public final Map<Long, Map<String, String>> userResumeData = new LinkedHashMap<>();
    public final Map<Long, Resume> clientsMap = new HashMap<>();
    public final String ERROR_TEXT = "Error occurred: ";
    public final Map<Long, Long> states = new HashMap<>(); // state, chatId
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

    public InlineKeyboardMarkup createInlineKeyboard(List<String> buttonLabels, List<String> callbackData, int pageSize, int pageNumber) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min((pageNumber + 1) * pageSize, buttonLabels.size());

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
            previousButton.setText("⬅️ Предыдущая страница");
            previousButton.setCallbackData("previous_page");
            previousButtonRow.add(previousButton);
            rowList.add(previousButtonRow);
        }

        if (endIndex < buttonLabels.size()) {
            List<InlineKeyboardButton> nextButtonRow = new ArrayList<>();
            InlineKeyboardButton nextButton = new InlineKeyboardButton();
            nextButton.setText("Следующая страница ➡️");
            nextButton.setCallbackData("next_page");
            nextButtonRow.add(nextButton);
            rowList.add(nextButtonRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public static long generateRandom12DigitNumber(Random random) {
        return (long) (Math.pow(10, 11) + random.nextInt((int) Math.pow(10, 11)));
    }
}
