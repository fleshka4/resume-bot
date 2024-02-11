package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.service.HeadHunterService;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.List;

import static com.resume.bot.display.MessageUtil.executeEditMessageWithKeyBoard;
import static com.resume.bot.display.MessageUtil.sendMessage;

@Slf4j
@RequiredArgsConstructor
public class ExportResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    private final HhConfig hhConfig;

    private final HeadHunterService headHunterService;

    @Value("${hh.base-url}")
    private String hhBaseUrl;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        switch (callbackData) {
            case "export_data_hh" -> {
                if (!BotUtil.states.containsValue(chatId)) {
                    authorization("""
                            Отлично, для экспортирования своих резюме с hh.ru необходимо авторизоваться по следующей [ссылке](%s)!:key:
                            """, chatId);
                } else {
                    System.out.println(hhBaseUrl);
                    exportLogic(chatId, messageId);
                }
            }
            case "export_resume_hh" -> {
                if (!BotUtil.states.containsValue(chatId)) {
                    authorization("""
                            Прежде чем начать работу со своими резюме с hh.ru необходимо авторизоваться по следующей [ссылке](%s)!:key:
                            """, chatId);
                } else {
                    System.out.println(hhBaseUrl);
                    exportLogic(chatId, messageId);
                }
            }
        }
    }

    private void authorization(String message, Long chatId) {
        long randomValue = BotUtil.generateRandom12DigitNumber(BotUtil.random);
        while (BotUtil.states.containsKey(randomValue)) {
            randomValue = BotUtil.generateRandom12DigitNumber(BotUtil.random);
        }

        BotUtil.states.put(randomValue, chatId);

        String link = "https://hh.ru/oauth/authorize?" +
                "response_type=code&" +
                "client_id=" + hhConfig.getClientId() +
                "&state=" + randomValue +
                "&redirect_uri=http://localhost:5000/hh/auth";

        sendMessage(bot, EmojiParser.parseToUnicode(message.formatted(link)), chatId);
    }

    private void exportLogic(Long chatId, Integer messageId) {
        BotUtil.userStates.put(chatId, BotState.MY_RESUMES);

        List<Resume> resumeList = headHunterService.getClientResumes(hhBaseUrl, chatId);

        List<String> buttonLabels = new ArrayList<>(resumeList.stream()
                .map(Resume::getTitle)
                .toList());
        buttonLabels.add("Назад");

        List<String> buttonIds = new ArrayList<>();
        for (int i = 1; i <= resumeList.size(); i++) {
            buttonIds.add("resume_hh_" + i);
        }
        buttonIds.add("back_to_menu_3");
        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("""
                После выбора конкретного резюме, у вас будет возможность:

                - *Скачать ваше резюме*
                - *Внести изменения в резюме*
                """), messageId, chatId, buttonLabels, buttonIds);
    }
}
