package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@Slf4j
@RequiredArgsConstructor
public class MyResumesActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;
//    private final BotState currentState;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {

    }
}
