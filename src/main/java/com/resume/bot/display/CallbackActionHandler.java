package com.resume.bot.display;

public interface CallbackActionHandler {
    void performAction(String callbackData, Integer messageId, Long chatId);
}
