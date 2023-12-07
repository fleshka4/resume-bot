package com.resume.bot.config;

import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeBot;
import lombok.RequiredArgsConstructor;
import com.resume.util.BotUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class BotInitializer {
    private final ResumeBot resumeBot;
    private final HeadHunterService headHunterService;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        /*try {
            System.out.println("MONO");
            System.out.println(headHunterService.getFlux().get(0));
        } catch (RuntimeException e) {
            System.out.println("ERROR");
        }

        System.out.println("FLUX");
        System.out.println(headHunterService.getFlux().get(0));*/


        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(resumeBot);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }
}
