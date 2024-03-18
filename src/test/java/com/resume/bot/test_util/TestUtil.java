package com.resume.bot.test_util;

import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.*;

@UtilityClass
public class TestUtil {
    public User createUser() {
        org.telegram.telegrambots.meta.api.objects.User user =
                new org.telegram.telegrambots.meta.api.objects.User();
        user.setId(1L);
        user.setUserName("test_user");

        return user;
    }

    public Message createMessage() {
        Message message = new Message();
        message.setMessageId(1);
        message.setChat(new Chat(1L, "name"));
        return message;
    }

    public Message createMessage(String text) {
        Message message = createMessage();
        message.setFrom(createUser());
        message.setText(text);
        return message;
    }

    public CallbackQuery createCallBackQuery() {
        CallbackQuery callbackQuery = new CallbackQuery();
        callbackQuery.setFrom(createUser());
        callbackQuery.setMessage(createMessage());
        return callbackQuery;
    }

    public Update createUpdateByCommand(String text) {
        Update update = new Update();

        Message message = createMessage(text);
        update.setMessage(message);
        return update;
    }
}
