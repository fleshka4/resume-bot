package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.MessageUtil;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.model.entity.User;
import com.resume.bot.service.*;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
import com.resume.latex.LatexProcessor;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.resume.bot.display.MessageUtil.*;
import static com.resume.util.BigKeyboardType.RESUMES;

@Slf4j
@RequiredArgsConstructor
public class MyResumesActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    private final ResumeService resumeService;

    private final TemplateService templateService;

    private final HeadHunterService headHunterService;

    private final TokenHolderService tokenHolderService;

    private final UserService userService;

    private final ApiClientTokenImpl apiClientTokenImpl;

    @Value("${hh.base-url}")
    private String hhBaseUrl;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        switch (callbackData) {
            case "my_resumes", "back_to_my_resumes" -> {
                BotUtil.userStates.put(chatId, BotState.MY_RESUMES);
                List<Resume> resumes = resumeService.getResumesByUserId(chatId);
                List<String> buttonLabels = new ArrayList<>(resumes.stream()
                        .map(Resume::getTitle)
                        .toList());
                buttonLabels.add("Назад");

                List<String> buttonIds = new ArrayList<>();
                for (int i = 1; i <= resumes.size(); i++) {
                    buttonIds.add("res_" + resumes.get(i - 1).getTitle() + "_" + i);
                }
                buttonIds.add("back_to_menu_3");

                executeEditMessageWithBigKeyBoard(bot, EmojiParser.parseToUnicode("""
                        Здесь Вы можете просматривать список всех созданных резюме.:page_with_curl:
                        После выбора конкретного резюме, у вас будет возможность:

                        - *Опубликовать его на HeadHunter*
                        - *Скачать ваше резюме*
                        - *Внести изменения в резюме*
                        - *Удалить резюме*
                        """), messageId, chatId, buttonLabels, buttonIds, 0, resumes.size(), RESUMES);
            }
            case "back_to_resumes" -> {
                //todo back_to_resumes
            }
            case "back_to_menu_3" -> {
                BotUtil.createMyResumesMenu(bot, messageId, chatId);
            }
            default -> {
                if (finallyPublish(callbackData, chatId)
                        || publishOnHh(callbackData, messageId, chatId)
                        || downloadResume(callbackData, chatId)
                        || editResume(callbackData, messageId, chatId)
                        || editTemplateResume(callbackData, messageId, chatId)
                        || editTextResume(callbackData, messageId, chatId)
                        || deleteResume(callbackData, chatId)
                        || updateResumeOnHh(callbackData, chatId)) {
                }
            }
        }

        if (callbackData.startsWith("res_")) {
            List<String> titles = new ArrayList<>(resumeService.getResumesByUserId(chatId).stream()
                    .map(Resume::getTitle)
                    .toList());

            String textMsg = "Выберите, что нужно сделать с вашим резюме.:slightly_smiling:";
            List<String> callbackDataListNonImmutable = Arrays.stream(callbackData.split("_")).toList();
            List<String> callbackParts = new ArrayList<>(callbackDataListNonImmutable);

            callbackParts.removeFirst();
            String postfix = callbackParts.removeLast();

            String title = String.join("_", callbackParts);

            if (titles.contains(title)) {
                if (resumeService.getResumeByTitle(title).getDownloadLink() != null) {
                    List<String> buttonLabels = Arrays.asList("Скачать резюме",
                            "Редактировать резюме", "Назад");
                    List<String> buttonIds = Arrays.asList("download_resume_" + postfix,
                            "edit_resume_" + postfix, "back_to_resumes");

                    executeEditMessageWithBigKeyBoard(bot, EmojiParser.parseToUnicode(textMsg),
                            messageId, chatId, buttonLabels, buttonIds, 0, resumeService.getResumesByUserId(chatId).size(), RESUMES);
                } else {
                    List<String> buttonLabels = Arrays.asList("Опубликовать на HH", "Скачать резюме",
                            "Редактировать резюме", "Удалить резюме", "Назад");
                    List<String> buttonIds = Arrays.asList("publish_on_hh_" + postfix, "download_resume_" + postfix,
                            "edit_resume_" + postfix, "delete_resume_" + postfix, "back_to_resumes");

                    executeEditMessageWithBigKeyBoard(bot, EmojiParser.parseToUnicode(textMsg),
                            messageId, chatId, buttonLabels, buttonIds, 0, resumeService.getResumesByUserId(chatId).size(), RESUMES);
                }
            }
        }
    }

    private boolean publishOnHh(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("publish_on_hh_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        int num = BotUtil.getResumeNumber(matcher.group());

        List<String> buttonLabels = Arrays.asList("Загрузить новое резюме на hh", "Обновить резюме на hh", "Назад");
        List<String> buttonIds = Arrays.asList("finally_publish_on_hh_resume_" + num, "update_resume_" + num,
                "resume_" + num);

        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Опубликовать как новое или обновить существующее"),
                messageId, chatId, buttonLabels, buttonIds);
        return true;
    }

    private boolean updateResumeOnHh(String data, Long chatId) {
        Pattern pattern = Pattern.compile("update_resume_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = BotUtil.getResume(matcher.group(), resumeService, chatId, bot);
        if (resume == null) {
            return true;
        }

        String hhLink = resume.getLink();
        if (hhLink == null || hhLink.isEmpty()) {
            sendMessage(bot, "Резюме не может быть обновлено, так как оно не найдено на hh.ru. Попробуйте сначала опубликовать его", chatId);
            return true;
        }
        try {
            String[] split = hhLink.split("/");
            String resumeId = split[split.length - 1];
            headHunterService.putEditClient(hhBaseUrl, chatId, resumeId,
                    JsonProcessor.createEntityFromJson(resume.getResumeData(), com.resume.bot.json.entity.client.Resume.class));
        } catch (Exception exception) {
            log.error(exception.getMessage());
            sendMessage(bot, "Произошла ошибка при обновлении резюме. " +
                    "Попробуйте удалить его и создать заново или выберите другое", chatId);
        }
        return true;
    }

    private boolean finallyPublish(String data, Long chatId) {
        Pattern pattern = Pattern.compile("finally_publish_on_hh_resume_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        com.resume.bot.model.entity.Resume resume = BotUtil.getResume(matcher.group(), resumeService, chatId, bot);
        BotUtil.publishResume(resume, chatId, resumeService, headHunterService, bot, hhBaseUrl, true);
        BotUtil.createMenu(MessageUtil.createSendMessageRequest(bot, chatId), bot);
        return true;
    }

    private boolean downloadResume(String data, Long chatId) {
        Pattern pattern = Pattern.compile("download_resume_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = BotUtil.getResume(matcher.group(), resumeService, chatId, bot);
        if (matcher.matches() && Objects.requireNonNull(resume).getDownloadLink() == null) {
            String filePath = resume.getPdfPath();
            if (filePath == null) {
                if (resume.getTemplate() == null) {
                    sendMessage(bot, "Выберите шаблон перед скачиванием данного резюме", chatId);
                    return true;
                }
                try {
                    filePath = LatexProcessor.compile(JsonProcessor.createEntityFromJson(resume.getResumeData(), com.resume.bot.json.entity.client.Resume.class),
                            resume.getTemplate().getSourcePath(), chatId.toString(), resume.getTitle());
                } catch (IOException | InterruptedException e) {
                    log.error(e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            resumeService.updatePdfPathByResumeId(resume.getPdfPath(), resume.getResumeId());
            File file = new File(filePath);
            try {
                bot.execute(new SendDocument(String.valueOf(chatId), new InputFile(file)));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
                sendMessage(bot, "Не найден файл, содержащий резюме. Попробуйте создать заново", chatId);
            }
        } else if (matcher.matches() && Objects.requireNonNull(resume).getDownloadLink() != null) {
            User user = userService.getUser(chatId);
            byte[] response = apiClientTokenImpl.get(resume.getDownloadLink(), tokenHolderService.getUserToken(user).getAccessToken(), byte[].class);
            InputStream inputStream = new ByteArrayInputStream(response);
            try {
                bot.execute(new SendDocument(String.valueOf(chatId), new InputFile(inputStream, "resume.pdf")));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
                sendMessage(bot, "Что-то пошло не так. Попробуйте создать заново", chatId);
            }
        }
        BotUtil.createMenu(MessageUtil.createSendMessageRequest(bot, chatId), bot);
        return true;
    }

    private boolean editResume(String data, Integer messageId, Long chatId) {
        BotUtil.userStates.put(chatId, BotState.EDIT_MY_RESUME);

        Pattern pattern = Pattern.compile("edit_resume_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = null;
        if (matcher.matches()) {
            List<String> labels = List.of("Изменить шаблон", "Изменить текст");
            String[] splits = data.split("_");
            List<String> ids = List.of(splits[0] + "_template_" + data.substring(5), splits[0] + "_text_" + data.substring(5));

            executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите, что нужно изменить.:slightly_smiling:"),
                    messageId, chatId, labels, ids);
        }

        return true;
    }

    private boolean editTemplateResume(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("edit_template_resume_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = BotUtil.getResume(matcher.group(), resumeService, chatId, bot);
        if (resume == null) {
            return true;
        }

        BotUtil.userMyResumeMap.put(chatId, resume);
        BotUtil.userStates.put(chatId, BotState.EDIT_MY_RESUME_TEMPLATE);

        List<String> labels = new ArrayList<>();
        List<String> ids = new ArrayList<>();

        final List<Template> templates = templateService.getTemplates();
        for (int i = 0; i < templates.size(); i++) {
            labels.add(String.valueOf(i + 1));
            ids.add(data.substring(5) + "_" + templates.get(i).getTemplateId()); // template_resume_([1-6])_templateId
        }

        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите шаблон:slightly_smiling:"), // todo: добавить изображения шаблонов
                messageId, chatId, labels, ids);

        return true;
    }

    private boolean editTextResume(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("edit_text_resume_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = BotUtil.getResume(matcher.group(), resumeService, chatId, bot);
        if (resume == null) {
            return true;
        }

        List<String> buttonLabels = List.of("Всё верно!");
        List<String> callbackData = List.of("resume_" + BotUtil.getResumeNumber(matcher.group()));
        BotUtil.userStates.put(chatId, BotState.EDIT_MY_RESUME);
        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode(BotUtil.askToEditMyResume(resume, chatId)),
                messageId, chatId, buttonLabels, callbackData);

        return true;
    }

    private boolean deleteResume(String data, Long chatId) {
        Pattern pattern = Pattern.compile("delete_resume_([1-9]+)");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = BotUtil.getResume(matcher.group(), resumeService, chatId, bot);
        if (resume != null) {
            resumeService.deleteResume(resume);
        }

        MessageUtil.sendMessage(bot, "Резюме успешно удалено", chatId);
        BotUtil.createMenu(MessageUtil.createSendMessageRequest(bot, chatId), bot);
        return true;
    }
}
