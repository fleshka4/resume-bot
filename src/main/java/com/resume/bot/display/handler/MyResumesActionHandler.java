package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.model.entity.Resume;
import com.resume.bot.model.entity.Template;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.bot.service.TemplateService;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.resume.bot.display.MessageUtil.executeEditMessageWithKeyBoard;
import static com.resume.bot.display.MessageUtil.sendMessage;

@Slf4j
@RequiredArgsConstructor
public class MyResumesActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    private final ResumeService resumeService;

    private final TemplateService templateService;

    private final HeadHunterService headHunterService;

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
                    buttonIds.add("resume_" + i);
                }
                buttonIds.add("back_to_menu_3");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("""
                        Здесь Вы можете просматривать список всех созданных резюме.:page_with_curl:
                        После выбора конкретного резюме, у вас будет возможность:

                        - *Опубликовать его на HeadHunter*
                        - *Скачать ваше резюме*
                        - *Внести изменения в резюме*
                        - *Удалить резюме*
                        """), messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_menu_3" -> {
                List<String> buttonLabels = Arrays.asList("Создать резюме", "Использовать резюме с hh.ru", "Мои резюме");
                List<String> buttonIds = Arrays.asList("create_resume", "export_resume_hh", "my_resumes");

                String menuInfo = """
                        Выберите действие:

                        *Создать резюме* :memo:
                        Начните процесс создания нового резюме с нуля!

                        *Экспорт резюме* с hh.ru :inbox_tray:
                        Экспортируйте свои данные с hh.ru для взаимодействия с ними.

                        *Мои резюме* :clipboard:
                        Посмотрите список ваших созданных резюме.""";

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode(menuInfo), messageId, chatId, buttonLabels, buttonIds);
            }
            //todo действия после выбора одной из резюмешек
            case "resume_1", "resume_2", "resume_3", "resume_4", "resume_5", "resume_6" -> {
                List<String> buttonLabels = Arrays.asList("Опубликовать на HH", "Скачать резюме",
                        "Редактировать резюме", "Удалить резюме", "Назад");
                List<String> buttonIds = Arrays.asList("publish_on_hh_" + callbackData, "download_" + callbackData,
                        "edit_" + callbackData, "delete_" + callbackData, "back_to_my_resumes");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите, что нужно сделать с вашим резюме.:slightly_smiling:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            /*case "edit_resume" -> {
                Map<String, String> buttons = Map.of(
                        "edit_resume_1", "1",
                        "edit_resume_2", "2",
                        "edit_resume_3", "3",
                        "edit_resume_4", "4",
                        "edit_resume_5", "5",
                        "edit_resume_6", "6");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите резюме, которое нужно изменить.:slightly_smiling:"),
                        messageId, chatId, buttons.values().stream().toList(), buttons.keySet().stream().toList());
            }*/
            default -> {
                if (finallyPublish(callbackData, chatId)
                        || publishOnHh(callbackData, messageId, chatId)
                        || downloadResume(callbackData, chatId)
                        || editResume(callbackData, messageId, chatId)
                        || editTemplateResume(callbackData, messageId, chatId)
                        || editTextResume(callbackData, chatId)
                        || deleteResume(callbackData, chatId)
                        || updateResumeOnHh(callbackData, chatId)) {
                }
            }
        }
    }

    private boolean publishOnHh(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("publish_on_hh_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        int num = getResumeNumber(matcher);

        List<String> buttonLabels = Arrays.asList("Загрузить новое резюме на hh", "Обновить резюме на hh", "Назад");
        List<String> buttonIds = Arrays.asList("finally_publish_on_hh_resume_" + num, "update_resume_" + num,
                "resume_" + num);

        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Опубликовать как новое или обновить существующее"),
                messageId, chatId, buttonLabels, buttonIds);
        return true;
    }

    private boolean updateResumeOnHh(String data, Long chatId) {
        Pattern pattern = Pattern.compile("update_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = getResume(matcher, chatId);
        if (resume == null) {
            return true;
        }

        String hhLink = resume.getHhLink();
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
        Pattern pattern = Pattern.compile("finally_publish_on_hh_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = getResume(matcher, chatId);
        if (resume == null) {
            return true;
        }
        try {
            String hhLink = headHunterService.postCreateClient(hhBaseUrl, chatId,
                    JsonProcessor.createEntityFromJson(resume.getResumeData(), com.resume.bot.json.entity.client.Resume.class));
            Resume newResume = new Resume();
            newResume.setResumeData(resume.getResumeData());
            newResume.setTitle(resume.getTitle());
            newResume.setUser(resume.getUser());
            newResume.setTemplate(resume.getTemplate());
            newResume.setHhLink(hhLink);
            newResume.setPdfPath(resume.getPdfPath());
            resumeService.saveResume(newResume);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            sendMessage(bot, "Произошла ошибка при публикации резюме. " +
                    "Попробуйте удалить его и создать заново или выберите другое", chatId);
        }
        return true;
    }

    private boolean downloadResume(String data, Long chatId) {
        Pattern pattern = Pattern.compile("download_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = getResume(matcher, chatId);
        if (resume == null) {
            return true;
        }

        String filePath = resume.getPdfPath();
        File file = new File(filePath);
        try {
            bot.execute(new SendDocument(String.valueOf(chatId), new InputFile(file)));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            sendMessage(bot, "Не найден файл, содержащий резюме. Попробуйте создать заново", chatId);
        }

        return true;
    }

    private boolean editResume(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("edit_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        List<String> labels = List.of("Изменить шаблон", "Изменить текст");
        String[] splits = data.split("_");
        List<String> ids = List.of(splits[0] + "_template_" + data.substring(5), splits[0] + "_text_" + data.substring(5));

        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите, что нужно изменить.:slightly_smiling:"),
                messageId, chatId, labels, ids);
        return true;
    }

    private boolean editTemplateResume(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("edit_template_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = getResume(matcher, chatId);
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

        executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите, что нужно изменить.:slightly_smiling:"), // todo: добавить изображения шаблонов
                messageId, chatId, labels, ids);

        return true;
    }

    private boolean editTextResume(String data, Long chatId) {
        Pattern pattern = Pattern.compile("edit_text_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = getResume(matcher, chatId);
        if (resume == null) {
            return true;
        }

        BotUtil.userStates.put(chatId, BotState.EDIT_MY_RESUME);

        BotUtil.askToEditMyResume(resume, chatId, bot);

        return true;
    }

    private boolean deleteResume(String data, Long chatId) {
        Pattern pattern = Pattern.compile("delete_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = getResume(matcher, chatId);
        if (resume != null) {
            resumeService.deleteResume(resume);
        }
        return true;
    }

    private Resume getResume(Matcher matcher, Long chatId) {
        int numOfResume = getResumeNumber(matcher);
        List<Resume> resumes = resumeService.getResumesByUserId(chatId);
        resumes.sort(Comparator.comparingInt(Resume::getResumeId));
        if (resumes.size() >= numOfResume) {
            return resumes.get(numOfResume - 1);
        }
        sendMessage(bot, "Резюме не найдено. Попробуйте снова", chatId);
        return null;
    }

    private int getResumeNumber(Matcher matcher) {
        String[] s = matcher.group().split("_");
        return Integer.parseInt(s[s.length - 1]);
    }
}
