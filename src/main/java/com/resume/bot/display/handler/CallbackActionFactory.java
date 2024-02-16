package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.service.*;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import com.resume.util.BotUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import static com.resume.util.BotUtil.personAndIndustry;
import static com.resume.util.Constants.*;

@AllArgsConstructor
@Component
public class CallbackActionFactory {

    private final ResumeService resumeService;

    private final HeadHunterService headHunterService;

    private final TemplateService templateService;

    private final UserService userService;

    private final String hhBaseUrl;
    private final String serverUrl;

    private final HhConfig hhConfig;

    private final ApiClientTokenImpl apiClientTokenImpl;

    private final TokenHolderService tokenHolderService;

    public CallbackActionHandler createCallbackActionHandler(TelegramLongPollingBot pollingBot, String callbackData) {
        if (BotUtil.CREATE_RESUME_IDS_LIST.contains(callbackData) ||
                sexTypes.containsKey(callbackData) ||
                educationLevels.containsKey(callbackData) ||
                driverLicenseTypes.containsKey(callbackData) ||
                employmentTypes.containsKey(callbackData) ||
                scheduleTypes.containsKey(callbackData) ||
                contactTypes.containsKey(callbackData) ||
                isInChildIndustries(callbackData)) {
            return new CreateResumeActionHandler(pollingBot, resumeService, userService);
        }
        if (BotUtil.CORRECT_DATA_IDS_LIST.contains(callbackData)) {
            return new CorrectDataHandler(headHunterService, resumeService, templateService, pollingBot, tokenHolderService, userService, hhConfig, hhBaseUrl, serverUrl);
        }
        if (BotUtil.EXPORT_RESUME_IDS_LIST.contains(callbackData)) {
            return new ExportResumeActionHandler(pollingBot, hhConfig, hhBaseUrl, serverUrl, headHunterService, tokenHolderService, userService, resumeService);
        }
        if (BotUtil.MY_RESUMES_IDS_LIST.contains(callbackData) || BotUtil.checkIfAction(callbackData) || callbackData.startsWith("res_")) {
            return new MyResumesActionHandler(pollingBot, resumeService, templateService, headHunterService, tokenHolderService, userService, apiClientTokenImpl, hhBaseUrl);
        }
        if (BotUtil.checkIfBigType(callbackData)) {
            return new BigKeyBoardHandler(pollingBot);
        }
        return null;
    }

    private boolean isInChildIndustries(String id) {
        boolean ans = false;
        for (Industry industry : INDUSTRIES) {
            ans = industry.getIndustries()
                    .stream().anyMatch(ind -> ind.getId().equals(id));
            if (ans == true) {
                return ans;
            }
        }
        return ans;
    }
}
