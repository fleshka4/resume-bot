package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import com.resume.util.BotUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import static com.resume.util.Constants.*;

@Component
public class CallbackActionFactory {

    public CallbackActionHandler createCallbackActionHandler(TelegramLongPollingBot pollingBot, String callbackData) {
        if (BotUtil.CREATE_RESUME_IDS_LIST.contains(callbackData) ||
                sexTypes.containsKey(callbackData) ||
                educationLevels.containsKey(callbackData) ||
                driverLicenseTypes.contains(callbackData)) { //todo сферы деятельности компании
            return new CreateResumeActionHandler(pollingBot);
        } else if ("export_resume_hh".equals(callbackData)) {
            return new ExportResumeActionHandler(pollingBot);
        } else if ("my_resumes".equals(callbackData)) {
            return new MyResumesActionHandler(pollingBot);
        }

        return null;
    }
}

