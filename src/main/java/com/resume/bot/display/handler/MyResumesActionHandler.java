package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.ResumeField;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.model.entity.Resume;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.hh_wrapper.impl.ApiClientTokenImpl;
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

    private final HeadHunterService headHunterService;

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
                    buttonIds.add("resume_" + i);
                }
                buttonIds.add("back_to_menu_3");

                // todo проецировать список резюме полученных из бд
                //  с помощью inline клавиатуры (кнопки это номера в списке)
                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("""
                        Здесь Вы можете просматривать список всех созданных резюме.:page_with_curl:
                        После выбора конкретного резюме, у вас будет возможность:

                        - *Опубликовать его на HeadHunter*
                        - *Скачать ваше резюме*
                        - *Внести изменения в резюме*
                        - *Удалить резюме*
                        """), messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_hh_resumes" -> {
                List<Resume> hhResumes = resumeService.getHhResumesByUserId(chatId);
                List<String> buttonLabels = new ArrayList<>(hhResumes.stream()
                        .map(Resume::getTitle)
                        .toList());
                buttonLabels.add("Назад");

                List<String> buttonIds = new ArrayList<>();
                for (int i = 1; i <= hhResumes.size(); i++) {
                    buttonIds.add("resume_hh_" + i);
                }
                buttonIds.add("back_to_menu_3");
                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("""
                        После выбора конкретного резюме, у вас будет возможность:

                        - *Скачать ваше резюме*
                        - *Внести изменения в резюме*
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
            case "resume_1", "resume_2", "resume_3", "resume_4", "resume_5", "resume_6" -> {
                List<String> buttonLabels = Arrays.asList("Опубликовать на HH", "Скачать резюме",
                        "Редактировать резюме", "Удалить резюме", "Назад");
                List<String> buttonIds = Arrays.asList("publish_on_hh_" + callbackData, "download_" + callbackData,
                        "edit_" + callbackData, "delete_" + callbackData, "back_to_my_resumes");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите, что нужно сделать с вашим резюме.:slightly_smiling:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            case "resume_hh_1", "resume_hh_2", "resume_hh_3", "resume_hh_4", "resume_hh_5", "resume_hh_6" -> {
                List<String> buttonLabels = Arrays.asList("Скачать резюме",
                        "Редактировать резюме", "Назад");
                List<String> buttonIds = Arrays.asList("download_" + callbackData,
                        "edit_" + callbackData, "back_to_hh_resumes");

                executeEditMessageWithKeyBoard(bot, EmojiParser.parseToUnicode("Выберите, что нужно сделать с вашим резюме.:slightly_smiling:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            default -> {
                if (finallyPublish(callbackData, chatId)
                        || publishOnHh(callbackData, messageId, chatId)
                        || downloadResume(callbackData, chatId)
                        || editResume(callbackData, chatId)
                        || deleteResume(callbackData, chatId)
                        || updateResumeOnHh(callbackData, chatId)) {
                    System.out.println("done");
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
            newResume.setLink(hhLink);
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
        Pattern patternForLatexResumes = Pattern.compile("download_resume_([1-6])");
        Pattern patternForHhResumes = Pattern.compile("download_resume_hh_([1-6])");

        Matcher matcherFirst = patternForLatexResumes.matcher(data);
        Matcher matcherSecond = patternForHhResumes.matcher(data);
        if (!matcherFirst.matches() && !matcherSecond.matches()) {
            return false;
        }

        Resume resume = null;
        if (matcherFirst.matches()) {
            resume = getResume(matcherFirst, chatId);

            String filePath = resume.getPdfPath();
            File file = new File(filePath);
            try {
                bot.execute(new SendDocument(String.valueOf(chatId), new InputFile(file)));
            } catch (TelegramApiException e) {
                log.error(e.getMessage());
                sendMessage(bot, "Не найден файл, содержащий резюме. Попробуйте создать заново", chatId);
            }
        } else {

        }

        if (resume == null) {
            return true;
        }

        return true;
    }

    private boolean editResume(String data, Long chatId) {
        BotUtil.userStates.put(chatId, BotState.EDIT_MY_RESUME);

        Pattern patternForLatexResumes = Pattern.compile("edit_resume_([1-6])");
        Pattern patternForHhResumes = Pattern.compile("download_resume_hh_([1-6])");

        Matcher matcherFirst = patternForLatexResumes.matcher(data);
        Matcher matcherSecond = patternForHhResumes.matcher(data);
        if (!matcherFirst.matches() && !matcherSecond.matches()) {
            return false;
        }

        Resume resume = null;
        if (matcherFirst.matches()) {

        } else {

        }

        if (resume == null) {
            return true;
        }

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


    //TODO сами вызовите где надо в этом говне
    private boolean editClientResumeData(com.resume.bot.json.entity.client.Resume resume,
                                         String receivedText, Long chatId) {
        String[] lines = receivedText.split("\\r?\\n");
        for (String line : lines) {
            String[] parts = line.split("-", 2);
            if (parts.length == 2) {
                String fieldLabel = parts[0].trim().toLowerCase();
                String fieldValue = parts[1].trim();

                ResumeField field = Arrays.stream(ResumeField.values())
                        .filter(f -> f.getValue().equals(fieldLabel)).findFirst().orElse(null);
                if (field != null) {
                    if (field.processCheck(fieldValue)) {
                        field.editInResume(resume, fieldValue);
                    } else {
                        sendMessage(bot, field.message(), chatId);
                        return false;
                    }
                }
            } else {
                sendMessage(bot, "Пожалуйста, введите данные в формате\n*Поле - Ваше новое значение*.", chatId);
                return false;
            }
        }

        return true;
    }
}
