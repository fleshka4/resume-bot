package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.Arrays;
import java.util.List;

@Component
public class CallbackActionFactory {

    public CallbackActionHandler createCallbackActionHandler(TelegramLongPollingBot pollingBot, String callbackData) {
        if (BotUtil.CREATE_RESUME_IDS_LIST.contains(callbackData)) {
            return new CreateResumeActionHandler(pollingBot);
        } else if ("export_resume_hh".equals(callbackData)) {
            return new ExportResumeActionHandler(pollingBot);
        } else if (BotUtil.MY_RESUMES_IDS_LIST.contains(callbackData)) {
            return new MyResumesActionHandler(pollingBot);
        }

        return null;
    }
}

