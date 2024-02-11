package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.service.*;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import static com.resume.util.Constants.*;

@AllArgsConstructor
@Component
public class CallbackActionFactory {
    private final HeadHunterService headHunterService;
    private final ResumeService resumeService;
    private final TemplateService templateService;
    private final TokenHolderService tokenHolderService;
    private final UserService userService;
    private final String hhBaseUrl;
    private final HhConfig hhConfig;

    public CallbackActionHandler createCallbackActionHandler(TelegramLongPollingBot pollingBot, String callbackData) {
        if (BotUtil.CREATE_RESUME_IDS_LIST.contains(callbackData) ||
                sexTypes.containsKey(callbackData) ||
                educationLevels.containsKey(callbackData) ||
                driverLicenseTypes.containsKey(callbackData) ||
                employmentTypes.containsKey(callbackData) ||
                scheduleTypes.containsKey(callbackData) ||
                contactTypes.containsKey(callbackData)) {
            return new CreateResumeActionHandler(pollingBot, resumeService, userService);
        }
        if (BotUtil.CORRECT_DATA_IDS_LIST.contains(callbackData)) {
            return new CorrectDataHandler(headHunterService, resumeService, templateService, pollingBot, tokenHolderService, userService, hhConfig, hhBaseUrl);
        }
        if (BotUtil.EXPORT_RESUME_IDS_LIST.contains(callbackData)) {
            return new ExportResumeActionHandler(pollingBot, hhConfig, headHunterService);
        }
        if (BotUtil.MY_RESUMES_IDS_LIST.contains(callbackData) || BotUtil.checkIfAction(callbackData)) {
            return new MyResumesActionHandler(pollingBot, resumeService, templateService, headHunterService);
        }
        if (BotUtil.checkIfBigType(callbackData)) {
            return new BigKeyBoardHandler(pollingBot);
        }
        return null;
    }
}
