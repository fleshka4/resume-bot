package com.resume.bot.display;

import com.resume.util.BigKeyboardType;
import com.resume.util.BotUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class MessageUtil {
    public static void executeEditMessageWithKeyBoard(TelegramLongPollingBot bot, String editMessageToSend, Integer messageId,
                                                      Long chatId, List<String> buttonLabels, List<String> buttonIds) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setParseMode(ParseMode.MARKDOWN);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(editMessageToSend);

        editMessage.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, buttonIds));
        executeMessage(bot, editMessage);
    }

    public static void executeMessageWithBigKeyBoard(TelegramLongPollingBot bot, String message, Long chatId, List<String> buttonLabels,
                                                     List<String> buttonIds, int pageNumber, int maxSize, BigKeyboardType type) {
        SendMessage sendMessageRequest = createSendMessageRequest(bot, chatId);
        sendMessageRequest.setText(message);

        sendMessageRequest.setReplyMarkup(BotUtil.createInlineBigKeyboard(buttonLabels, buttonIds, pageNumber, maxSize, type));
        executeMessage(bot, sendMessageRequest);
    }

    public static void executeEditMessageWithBigKeyBoard(TelegramLongPollingBot bot, String editMessageToSend, Integer messageId,
                                                         Long chatId, List<String> buttonLabels, List<String> buttonIds,
                                                         int pageNumber, int maxSize, BigKeyboardType type) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setParseMode(ParseMode.MARKDOWN);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        if (editMessageToSend != null) {
            editMessage.setText(editMessageToSend);
        }

        editMessage.setReplyMarkup(BotUtil.createInlineBigKeyboard(buttonLabels, buttonIds, pageNumber, maxSize, type));
        executeMessage(bot, editMessage);
    }

    public static void executeEditMessageWithBigKeyBoard(TelegramLongPollingBot bot, Integer messageId,
                                                         Long chatId, List<String> buttonLabels, List<String> buttonIds,
                                                         int pageNumber, int maxSize, BigKeyboardType type) {
        executeEditMessageWithBigKeyBoard(bot, null, messageId, chatId, buttonLabels, buttonIds, pageNumber, maxSize, type);
    }

    public void executeContinueKeyboardMessage(TelegramLongPollingBot bot, String messageText, Integer messageId,
                                               Long chatId, Map<String, String> data, String continueButtonLabel) {
        List<String> keys = new ArrayList<>(data.keySet().stream().toList());
        List<String> values = new ArrayList<>(data.values().stream().toList());
        keys.add(continueButtonLabel);
        values.add("Продолжить");
        executeEditMessageWithKeyBoard(bot, messageText, messageId, chatId, values, keys);
    }

    public static void executeEditMessage(TelegramLongPollingBot bot, String editMessageToSend, Integer messageId, Long chatId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setParseMode(ParseMode.MARKDOWN);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(editMessageToSend);

        executeMessage(bot, editMessage);
    }

    public static SendMessage createSendMessageRequest(TelegramLongPollingBot bot, Long chatId) {
        SendMessage sendMessageRequest = new SendMessage();

        sendMessageRequest.enableMarkdown(true);
        sendMessageRequest.setChatId(chatId.toString());
        return sendMessageRequest;
    }

    public static void sendMessage(TelegramLongPollingBot bot, String text, Long chatId) {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.MARKDOWN);
        message.setChatId(chatId.toString());
        message.setText(text);
        executeMessage(bot, message);
    }

    public void sendMessage(TelegramLongPollingBot bot, String messageToSend, SendMessage sendMessageRequest) {
        sendMessageRequest.setText(messageToSend);
        executeMessage(bot, sendMessageRequest);
    }

    public static <T extends Serializable, Method extends BotApiMethod<T>> void executeMessage(TelegramLongPollingBot bot, Method message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }
}
