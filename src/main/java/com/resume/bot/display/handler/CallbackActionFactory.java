package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@AllArgsConstructor
@Component
public class CallbackActionFactory {
    private final ResumeService resumeService;
    private final HeadHunterService headHunterService;
    private final HhConfig hhConfig;

    public CallbackActionHandler createCallbackActionHandler(TelegramLongPollingBot pollingBot, String callbackData) {
        if (BotUtil.CREATE_RESUME_IDS_LIST.contains(callbackData)) {
            return new CreateResumeActionHandler(pollingBot);
        }
        if (BotUtil.EXPORT_RESUME_IDS_LIST.contains(callbackData)) {
            return new ExportResumeActionHandler(pollingBot, hhConfig);
        }
        if (BotUtil.MY_RESUMES_IDS_LIST.contains(callbackData) || BotUtil.checkIfAction(callbackData)) {
            return new MyResumesActionHandler(pollingBot, resumeService, headHunterService);
        }

        return null;
    }
}

