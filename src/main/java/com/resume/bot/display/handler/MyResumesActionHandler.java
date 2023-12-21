package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MyResumesActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        switch (callbackData) {
            case "my_resumes" -> {
                BotUtil.userStates.put(chatId, BotState.MY_RESUMES);
                List<String> buttonLabels = List.of("Назад");
                List<String> buttonIds = List.of("back_to_menu_3");

                // todo проецировать список резюме полученных из бд
                //  с помощью inline клавиатуры (кнопки это номера в списке)
                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Здесь Вы можете просматривать список всех созданных резюме.:page_with_curl:\n" +
                        "После выбора конкретного резюме, у вас будет возможность:\n\n" +
                        "- *Опубликовать его на HeadHunter*\n" +
                        "- *Скачать ваше резюме*\n" +
                        "- *Внести изменения в резюме*\n" +
                        "- *Удалить резюме*\n"), messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_menu_3" -> {
                List<String> buttonLabels = Arrays.asList("Создать резюме", "Экспорт резюме с hh.ru", "Мои резюме");
                List<String> buttonIds = Arrays.asList("create_resume", "export_resume_hh", "my_resumes");

                String menuInfo = "Выберите действие:\n\n"
                        + "*Создать резюме* :memo:\nНачните процесс создания нового резюме с нуля!\n\n"
                        + "*Экспорт резюме* с hh.ru :inbox_tray:\nЭкспортируйте свои данные с hh.ru для взаимодействия с ними.\n\n"
                        + "*Мои резюме* :clipboard:\nПосмотрите список ваших созданных резюме.";

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode(menuInfo), messageId, chatId, buttonLabels, buttonIds);
            }
            //todo действия после выбора одной из резюмешек
            case "resume_1", "resume_2" -> {
                List<String> buttonLabels = Arrays.asList("Опубликовать на HH", "Скачать резюме", "Редактировать резюме", "Удалить резюме", "Назад");
                List<String> buttonIds = Arrays.asList("publish_on_hh", "download_resume", "edit_resume", "back_to_my_resumes");

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Выберите, что нужно сделать с вашим резюме.:slightly_smiling:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
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
}
