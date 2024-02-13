package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.TokenHolderService;
import com.resume.bot.service.UserService;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.List;

import static com.resume.util.BotUtil.exportDisplayLogic;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    private final HhConfig hhConfig;

    private final HeadHunterService headHunterService;

    private final String hhBaseUrl;

    private final TokenHolderService tokenHolderService;

    private final UserService userService;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        switch (callbackData) {
            case "export_data_hh" -> {
                if (!tokenHolderService.checkTokenHolderExists(userService.getUser(chatId))) {
                    BotUtil.authorization(bot, hhConfig, """
                            Отлично, для экспортирования своих резюме с hh.ru необходимо авторизоваться по следующей [ссылке](%s)!:key:
                            """, chatId);
                } else {
                    exportLogic(chatId, messageId);
                }
            }
            case "export_resume_hh" -> {
                if (!tokenHolderService.checkTokenHolderExists(userService.getUser(chatId))) {
                    BotUtil.authorization(bot, hhConfig, """
                            Прежде чем начать работу со своими резюме с hh.ru необходимо авторизоваться по следующей [ссылке](%s)!:key:
                            """, chatId);
                } else {
                    exportLogic(chatId, messageId);
                }
            }
        }
    }

    private void exportLogic(Long chatId, Integer messageId) {
        BotUtil.userStates.put(chatId, BotState.MY_RESUMES);

        List<Resume> hhResumes = headHunterService.getClientResumes(hhBaseUrl, chatId);
        exportDisplayLogic(bot, chatId, messageId, hhResumes);
    }
}
