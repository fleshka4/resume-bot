package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.bot.service.TokenHolderService;
import com.resume.bot.service.UserService;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.resume.bot.display.MessageUtil.executeEditMessageWithBigKeyBoard;
import static com.resume.util.BigKeyboardType.RESUMES;

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

    private final ResumeService resumeService;

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
        List<com.resume.bot.model.entity.Resume> resumesFromDB = resumeService.getResumesByUserId(chatId);
        exportDisplayLogic(bot, chatId, messageId, resumesFromDB, hhResumes);
    }

    private void exportDisplayLogic(TelegramLongPollingBot bot, Long chatId, Integer messageId,
                                    List<com.resume.bot.model.entity.Resume> resumesFromDb, List<Resume> resumesHh) {
        List<String> buttonIds = new ArrayList<>();
        int resumeCountFromDb = resumesFromDb.size();
        int counter = 0;

        Set<String> titles = new HashSet<>(resumesFromDb.stream()
                .map(com.resume.bot.model.entity.Resume::getTitle)
                .toList());

        for (Resume resumeHh : resumesHh) {
            com.resume.bot.model.entity.Resume resume;
            Resume resumeJson;
            if (!titles.contains(resumeHh.getTitle())) {
                resume = new com.resume.bot.model.entity.Resume();
                resumeJson = resumeHh;

                titles.add(resumeJson.getTitle());
                buttonIds.add(resumeJson.getTitle() + "_" + (counter + 1));

                resume.setResumeData(JsonProcessor.createJsonFromEntity(resumeJson));
                resume.setTitle(resumeJson.getTitle());
                resume.setUser(userService.getUser(chatId));
                resume.setLink(resumeJson.getAlternateUrl());
                resume.setDownloadLink(resumeJson.getDownload().getPdf().getUrl());
                resumeCountFromDb++;
                resumeService.saveResume(resume);
            }
        }

        List<String> buttonLabelsUpdated = new ArrayList<>(resumeService.getResumesByUserId(chatId).stream()
                .map(com.resume.bot.model.entity.Resume::getTitle)
                .toList());

        for (int i = 1; i <= resumeCountFromDb; i++) {
            buttonIds.add("res_" + buttonLabelsUpdated.get(i - 1) + "_" + i);
        }

        buttonLabelsUpdated.add("Назад");
        buttonIds.add("back_to_menu_3");

        executeEditMessageWithBigKeyBoard(bot, EmojiParser.parseToUnicode("""
                После выбора конкретного резюме, у вас будет возможность:
                                
                  - *Опубликовать его на HeadHunter*
                  - *Скачать ваше резюме*
                  - *Внести изменения в резюме*
                  - *Удалить резюме*
                """), messageId, chatId, buttonLabelsUpdated, buttonIds, 0, resumesFromDb.size(), RESUMES);
    }
}
