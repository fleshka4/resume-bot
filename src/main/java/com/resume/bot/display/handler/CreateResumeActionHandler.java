package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.common.Id;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.resume.bot.display.MessageUtil.*;

@Slf4j
@RequiredArgsConstructor
public class CreateResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
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

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Отлично! Давайте заполним поля в вашем резюме.:lower_left_fountain_pen:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            case "start_dialogue" -> {
                BotUtil.userStates.put(chatId, BotState.START_DIALOGUE);
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_NAME);
                sendMessage(bot, "Введите имя:", chatId);
            }
            case "choice_gender_m", "choice_gender_f" -> {
                String genderName = callbackData.equals("choice_gender_m") ? "Мужской" : "Женский";
                Map<String, String> userData = BotUtil.userResumeData.getOrDefault(chatId, new HashMap<>());
                userData.put("пол", genderName);
                BotUtil.userResumeData.put(chatId, userData);
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_LOCATION);
                sendMessage(bot, "Введите ваше место жительство.\nВ формате *страна, регион, населенный пункт (опционально)*:", chatId);
            }
            case "edit_result_data" -> {
                BotUtil.userStates.put(chatId, BotState.EDIT_CLIENT_RESULT_DATA);
                sendMessage(bot, EmojiParser.parseToUnicode("Жду исправлений:eyes:"), chatId);
            }
            case "result_data_is_correct" -> {
                BotUtil.userStates.put(chatId, BotState.RESULT_DATA_CORRECT);
                fillClientData(chatId);
                sendMessage(bot, EmojiParser.parseToUnicode("Замечательно! Ваши данные успешно получены.:sparkles:\nВот что Вы можете сделать:"), chatId);
                // todo кнопки "Загрузить новое резюме на hh", "Обновить резюме на hh", "Выбор Latex-шаблона"
            }
            case "back_to_menu" -> {
                List<String> buttonLabels = Arrays.asList("Создать резюме", "Использовать резюме с hh.ru", "Мои резюме");
                List<String> buttonIds = Arrays.asList("create_resume", "export_resume_hh", "my_resumes");

                String menuInfo = "Выберите действие:\n\n"
                        + "*Создать резюме* :memo:\nНачните процесс создания нового резюме с нуля!\n\n"
                        + "*Экспорт резюме с hh.ru* :inbox_tray:\nЭкспортируйте свои данные с hh.ru для взаимодействия с ними.\n\n"
                        + "*Мои резюме* :clipboard:\nПосмотрите список ваших созданных резюме.";

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode(menuInfo), messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_create_resume" -> {
                List<String> buttonLabels = Arrays.asList("У меня нет резюме, хочу его сделать с нуля!", "Экспорт данных с hh.ru", "Назад");
                List<String> buttonIds = Arrays.asList("create_resume_from_scratch", "export_data_hh", "back_to_menu");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Супер! С чего начнем?:sparkles:\n"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
        }
    }

    private void fillClientData(Long chatId) {
        Resume resume = BotUtil.clientsMap.getOrDefault(chatId, new Resume());

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
            }
        }

        JsonProcessor.createJsonFromEntity(resume);
    }
}
