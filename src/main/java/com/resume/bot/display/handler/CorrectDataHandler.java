package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.MessageUtil;
import com.resume.bot.model.entity.Template;
import com.resume.bot.service.*;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CorrectDataHandler implements CallbackActionHandler {
    private final HeadHunterService headHunterService;
    private final ResumeService resumeService;
    private final TemplateService templateService;
    private final TelegramLongPollingBot bot;
    private final TokenHolderService tokenHolderService;
    private final UserService userService;
    private final HhConfig hhConfig;

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
                if (tokenHolderService.checkTokenHolderExists(userService.getUser(chatId))) {
                    BotUtil.publishResume(BotUtil.lastSavedResumeMap.get(chatId), chatId, resumeService, headHunterService,
                            bot, hhBaseUrl, false);
                    MessageUtil.executeEditMessage(bot, "Ваше резюме успешно опубликовано на hh.ru", messageId, chatId);
                    BotUtil.createMenu(MessageUtil.createSendMessageRequest(bot, chatId), bot);
                } else {
                    BotUtil.authorization(bot, hhConfig, "Чтобы опубликовать резюме необходимо авторизоваться на hh.ru" +
                            " по следующей [ссылке](%s)!:key:. После авторизации нажмите на кнопку публикации резюме еще раз", chatId);
                }
            }
            case "choose_latex_for_resume" -> {
                BotUtil.userStates.put(chatId, BotState.CHOOSE_TEMPLATE);

                List<String> labels = new ArrayList<>();
                List<String> ids = new ArrayList<>();

                final List<Template> templates = templateService.getTemplates();
                for (int i = 0; i < templates.size(); i++) {
                    labels.add(String.valueOf(i + 1));
                    ids.add("template_" + templates.get(i).getTemplateId()); // template_templateId
                }

                MessageUtil.executeEditMessageWithKeyBoard(bot, "Выберите шаблон из предложенных:", messageId, chatId, labels, ids);
            }
            default -> throw new RuntimeException(callbackData + " not handled");
        }
    }
}
