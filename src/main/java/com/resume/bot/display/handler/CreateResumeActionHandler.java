package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.client.education.ElementaryEducation;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.resume.util.Constants.educationLevels;
import static com.resume.util.Constants.sexTypes;

@Slf4j
@RequiredArgsConstructor
public class CreateResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;
    private boolean isPrimaryEdu = true;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
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
                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Выберите уровень образования.:nerd:"),
                        messageId, chatId, educationLevels.values().stream().toList(), educationLevels.keySet().stream().toList());
            }
            case "skip_education" -> {
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> buttonIds = List.of("want_enter_work_experience", "skip_work_experience");
                executeEditMessageWithKeyBoard("Хотите ли Вы указать опыт работы?", messageId, chatId, buttonLabels, buttonIds);
            }
            case "want_enter_work_experience" -> {
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_PERIOD_OF_WORK);
                executeEditMessage(EmojiParser.parseToUnicode("Введите период опыта работы в формате ММ-ГГГГ.:necktie:"),
                        messageId, chatId);
            }
            case "skip_work_experience" -> {

            }
            case "edit_result_data" -> {
                BotUtil.userStates.put(chatId, BotState.EDIT_CLIENT_RESULT_DATA);
                sendMessage(EmojiParser.parseToUnicode("Жду исправлений:eyes:"), chatId);
            }
            case "result_data_is_correct" -> {
                BotUtil.userStates.put(chatId, BotState.RESULT_DATA_CORRECT);
                fillClientData(chatId);
                sendMessage(EmojiParser.parseToUnicode("Замечательно! Ваши данные успешно получены.:sparkles:\nВот что Вы можете сделать:"), chatId);
                // todo кнопки "Загрузить новое резюме на hh", "Обновить резюме на hh", "Выбор Latex-шаблона"
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

        Map<String, String> userData = BotUtil.userResumeData.get(chatId);

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
    }

    private void fillClientData(Long chatId) {
        Resume resume = BotUtil.clientsMap.getOrDefault(chatId, new Resume());
        Education education = new Education();
        List<PrimaryEducation> primaryEducationsList = new ArrayList<>();
        List<ElementaryEducation> elementaryEducationsList = new ArrayList<>();
        PrimaryEducation primaryEducation = new PrimaryEducation();
        ElementaryEducation elementaryEducation = new ElementaryEducation();

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
                case "местожительство" -> {
                    // todo
//                    Id areaId = new Id();
//                    areaId.setId(fieldValue);
//                    client.setArea();
                }
                case "уровень образования" -> {
                    Type typeEdu = new Type();
                    typeEdu.setId(getKeyByValue(educationLevels, fieldValue));
                    typeEdu.setName(fieldValue);
                    education.setLevel(typeEdu);
                }
                case "учебное заведение", "факультет", "специализация", "год окончания" -> {
                    if (isPrimaryEdu) {
                        addPrimaryEducation(primaryEducation, fieldLabel, fieldValue);
                    } else {
                        addElementaryEducation(elementaryEducation, fieldLabel, fieldValue);
                    }
                }
            }
        }

        primaryEducationsList.add(primaryEducation);
        elementaryEducationsList.add(elementaryEducation);
        education.setPrimary(primaryEducationsList);
        education.setElementary(elementaryEducationsList);
        resume.setEducation(education);

        JsonProcessor.createJsonFromEntity(resume);
    }

    private void addPrimaryEducation(PrimaryEducation primaryEducation, String fieldLabel, String fieldValue) {
        switch (fieldLabel.toLowerCase()) {
            case "учебное заведение" -> primaryEducation.setName(fieldValue);
            case "факультет" -> primaryEducation.setOrganization(fieldValue);
            case "специализация" -> primaryEducation.setResult(fieldValue);
            case "год окончания" -> primaryEducation.setYear(Long.parseLong(fieldValue));
        }
    }

    private void addElementaryEducation(ElementaryEducation elementaryEducation, String fieldLabel, String fieldValue) {
        switch (fieldLabel.toLowerCase()) {
            case "учебное заведение" -> elementaryEducation.setName(fieldValue);
            case "год окончания" -> elementaryEducation.setYear(Long.parseLong(fieldValue));
        }
    }

    public static String getKeyByValue(Map<String, String> map, String fieldValue) {
        return map.entrySet().stream()
                .filter(entry -> fieldValue.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }

    private void processOnChosenSex(String genderName, Long chatId, Map<String, String> userData) {
        userData.put("пол", genderName);
        BotUtil.userResumeData.put(chatId, userData);
        BotUtil.dialogueStates.put(chatId, BotState.ENTER_LOCATION);
        sendMessage("Введите ваше место жительство.\nВ формате *страна, регион, населенный пункт (опционально)*:", chatId);
    }

    private void processOnChosenLevelEducation(String level, Long chatId, Map<String, String> userData) {
        userData.put("уровень образования", level);
        BotUtil.userResumeData.put(chatId, userData);
        sendMessage("Введите название учебного заведения:", chatId);
        BotUtil.dialogueStates.put(chatId, BotState.ENTER_INSTITUTION);
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
}
