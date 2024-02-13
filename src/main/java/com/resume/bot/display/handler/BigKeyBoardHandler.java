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
            case "PROFESSIONAL" -> {
                bigKeyboardType = BigKeyboardType.PROFESSIONAL_ROLES;
                maxSize = Constants.PROFESSIONAL_ROLES.getCategories().size();
            }
            default -> throw new RuntimeException("BigKeyBoardType is " + bigKeyboardType.name());
        }
        if (!callbackData.contains("page")) {
            final int index = Integer.parseInt(callbackData.substring(bigKeyboardType.name().length() + 1));
            String message;
            if (bigKeyboardType == BigKeyboardType.INDUSTRIES) {
                BotUtil.personAndIndustry.put(chatId, Constants.INDUSTRIES.get(index).getName());
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_POST_IN_ORGANIZATION);
                message = "Введите свою должность в организации:";
            } else {
                BotUtil.personAndProfessionalRole.put(chatId, Constants.PROFESSIONAL_ROLES.getCategories().get(index).getName());
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_SALARY);
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> callbackDataList = List.of("want_enter_salary", "skip_salary");
                sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackDataList));
                message = "Хотите ли Вы указать желаемую зарплату?";
            }
            sendMessage(bot, message, sendMessageRequest);
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("Выберите сферу деятельности компании:\n\n");

        List<String> buttonLabels = new ArrayList<>();
        List<String> callbackDataList = new ArrayList<>();

        final int pageNumber = Integer.parseInt(callbackData.substring(bigKeyboardType.name().length() + 11));
        BotUtil.prepareBigKeyboardCreation(pageNumber, bigKeyboardType, message, buttonLabels, callbackDataList);

        executeEditMessageWithBigKeyBoard(bot, message.toString(), messageId, chatId, buttonLabels, callbackDataList, pageNumber, maxSize, bigKeyboardType.name());
    }
}
