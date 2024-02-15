package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.List;

import static com.resume.bot.display.MessageUtil.executeEditMessageWithKeyBoard;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExportResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    private final HhConfig hhConfig;

    private final HeadHunterService headHunterService;

    private final ResumeService resumeService;

    private final String hhBaseUrl;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        switch (callbackData) {
            case "export_data_hh" -> {
                if (!BotUtil.states.containsValue(chatId)) {
                    BotUtil.authorization(bot, hhConfig,"""
                            Отлично, для экспортирования своих резюме с hh.ru необходимо авторизоваться по следующей [ссылке](%s)!:key:
                            """, chatId);
                } else {
                    exportLogic(chatId, messageId);
                }
            }
            case "export_resume_hh" -> {
                if (!BotUtil.states.containsValue(chatId)) {
                    BotUtil.authorization(bot, hhConfig,"""
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

        List<Resume> hhResumes= headHunterService.getClientResumes(hhBaseUrl, chatId);
        exportDisplayLogic(chatId, messageId, hhResumes);
    }

    private void exportDisplayLogic(Long chatId, Integer messageId, List<Resume> resumeList) {
        List<String> buttonLabels = new ArrayList<>();
        List<String> buttonIds = new ArrayList<>();
        int resumeCount = 0;

        for (Resume resume : resumeList) {
            resumeCount++;
            buttonIds.add("resume_hh_" + resumeCount);
            buttonLabels.add(resume.getTitle());
        }
        buttonIds.add("back_to_menu_3");
        buttonLabels.add("Назад");

        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("""
            После выбора конкретного резюме, у вас будет возможность:
            - *Скачать ваше резюме*
            - *Внести изменения в резюме*
            """), messageId, chatId, buttonLabels, buttonIds);
    }
}
