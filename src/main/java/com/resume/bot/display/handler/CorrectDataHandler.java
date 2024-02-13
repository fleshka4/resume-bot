package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.MessageUtil;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.util.BotUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

@Slf4j
@RequiredArgsConstructor
public class CorrectDataHandler implements CallbackActionHandler {
    private final HeadHunterService headHunterService;
    private final ResumeService resumeService;
    private final TelegramLongPollingBot bot;

    @Value("${hh.base-url}")
    private final String hhBaseUrl;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        if (!BotUtil.lastSavedResumeMap.containsKey(chatId)) {
            log.error("Last saved resume for user " + chatId + " not found");
            return;
        }

        switch (callbackData) {
            case "post_resume_to_hh" -> {
                BotUtil.publishResume(BotUtil.lastSavedResumeMap.get(chatId), chatId, resumeService, headHunterService, bot, hhBaseUrl, false);
                MessageUtil.sendMessage(bot, "Ваше резюме успешно опубликовано на hh.ru", chatId);
            }
            case "choose_latex_for_resume" -> {

            }
            default -> {
                throw new RuntimeException(callbackData + " not handled");
            }
        }

    }
}
