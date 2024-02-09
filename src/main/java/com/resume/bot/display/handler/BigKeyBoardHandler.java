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

import java.util.ArrayList;
import java.util.List;

import static com.resume.bot.display.MessageUtil.executeEditMessageWithBigKeyBoard;
import static com.resume.bot.display.MessageUtil.sendMessage;

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
        int maxSize;
        switch (type) {
            case "INDUSTRIES" -> {
                bigKeyboardType = BigKeyboardType.INDUSTRIES;
                maxSize = Constants.INDUSTRIES.size();
            }
            default -> throw new RuntimeException("BigKeyBoardType is " + bigKeyboardType.name());
        }
        if (!callbackData.contains("page")) {
            int index = Integer.parseInt(callbackData.substring(bigKeyboardType.name().length() + 1));
            BotUtil.personAndIndustryType.put(chatId, Constants.INDUSTRIES.get(index).getName());
            BotUtil.dialogueStates.put(chatId, BotState.ENTER_POST_IN_ORGANIZATION);
            sendMessage(bot, "Введите свою должность в организации:", sendMessageRequest);
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("Выберите сферу деятельности компании:\n\n");

        List<String> buttonLabels = new ArrayList<>();
        List<String> callbackDataList = new ArrayList<>();

        final int pageNumber = Integer.parseInt(callbackData.substring(bigKeyboardType.name().length() + 11));
        BotUtil.prepareBigKeyboardCreation(pageNumber, bigKeyboardType, message, buttonLabels, callbackDataList);

        executeEditMessageWithBigKeyBoard(bot, String.valueOf(message), messageId, chatId, buttonLabels, callbackDataList, pageNumber, maxSize, bigKeyboardType.name());
    }
}
