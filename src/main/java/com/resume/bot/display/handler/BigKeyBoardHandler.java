package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.util.BigKeyboardType;
import com.resume.util.BotUtil;
import com.resume.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@RequiredArgsConstructor
public class BigKeyBoardHandler implements CallbackActionHandler {
    private final TelegramLongPollingBot bot;
    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(chatId);
        String type = callbackData.substring(0, callbackData.indexOf('_'));
        BigKeyboardType bigKeyboardType = BigKeyboardType.INVALID;
        switch (type) {
            case "INDUSTRIES" -> {
                bigKeyboardType = BigKeyboardType.INDUSTRIES;
            }
            default -> {}
        }
        if (callbackData.contains("page")) {
            sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(bigKeyboardType, callbackData));
        } else {
            int index = Integer.parseInt(callbackData.substring(bigKeyboardType.name().length() + 1));
            BotUtil.personAndIndustryType.put(chatId, Constants.INDUSTRIES.get(index).getName());
            BotUtil.dialogueStates.put(chatId, BotState.ENTER_POST_IN_ORGANIZATION);
        }
        sendMessageRequest.setText("");
        try {
            bot.execute(sendMessageRequest);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }
}
