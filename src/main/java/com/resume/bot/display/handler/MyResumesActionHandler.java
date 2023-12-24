package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.json.JsonProcessor;
import com.resume.bot.model.entity.Resume;
import com.resume.bot.service.HeadHunterService;
import com.resume.bot.service.ResumeService;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class MyResumesActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;

    private final ResumeService resumeService;

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

                // todo проецировать список резюме полученных из бд
                //  с помощью inline клавиатуры (кнопки это номера в списке)
                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("""
                        Здесь Вы можете просматривать список всех созданных резюме.:page_with_curl:
                        После выбора конкретного резюме, у вас будет возможность:

                        - *Опубликовать его на HeadHunter*
                        - *Скачать ваше резюме*
                        - *Внести изменения в резюме*
                        - *Удалить резюме*
                        """), messageId, chatId, buttonLabels, buttonIds);
            }
            case "back_to_menu_3" -> {
                List<String> buttonLabels = Arrays.asList("Создать резюме", "Экспорт резюме с hh.ru", "Мои резюме");
                List<String> buttonIds = Arrays.asList("create_resume", "export_resume_hh", "my_resumes");

                String menuInfo = """
                        Выберите действие:

                        *Создать резюме* :memo:
                        Начните процесс создания нового резюме с нуля!

                        *Экспорт резюме* с hh.ru :inbox_tray:
                        Экспортируйте свои данные с hh.ru для взаимодействия с ними.

                        *Мои резюме* :clipboard:
                        Посмотрите список ваших созданных резюме.""";

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode(menuInfo), messageId, chatId, buttonLabels, buttonIds);
            }
            //todo действия после выбора одной из резюмешек
            case "resume_1", "resume_2", "resume_3", "resume_4", "resume_5", "resume_6" -> {
                List<String> buttonLabels = Arrays.asList("Опубликовать на HH", "Скачать резюме",
                        "Редактировать резюме", "Удалить резюме", "Назад");
                List<String> buttonIds = Arrays.asList("publish_on_hh_" + callbackData, "download_" + callbackData,
                        "edit_" + callbackData, "delete_" + callbackData, "back_to_my_resumes");

                executeEditMessageWithKeyBoard(EmojiParser.parseToUnicode("Выберите, что нужно сделать с вашим резюме.:slightly_smiling:"),
                        messageId, chatId, buttonLabels, buttonIds);
            }
            default -> {
                if (publishOnHh(callbackData, messageId, chatId) || downloadResume(callbackData, messageId, chatId) ||
                        editResume(callbackData, messageId, chatId) || deleteResume(callbackData, messageId, chatId)) {
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

        Resume resume = getResume(matcher, chatId);
        if (resume == null) {
            return true;
        }
        try {
            String hhLink = resume.getHhLink();
            if (hhLink == null) {
                hhLink = headHunterService.postCreateClient(hhBaseUrl, chatId,
                        JsonProcessor.createEntityFromJson(resume.getResumeData(), com.resume.bot.json.entity.client.Resume.class));
                resumeService.updateHhLinkByResumeId(hhLink, resume.getResumeId());
            } else {
                String[] split = hhLink.split("/");
                String resumeId = split[split.length - 1];
                headHunterService.putEditClient(hhBaseUrl, chatId, resumeId,
                        JsonProcessor.createEntityFromJson(resume.getResumeData(), com.resume.bot.json.entity.client.Resume.class));
            }
        } catch (Exception exception) {
            log.error(exception.getMessage());
            sendMessage("Произошла ошибка при отправке резюме. " +
                    "Попробуйте удалить его и создать заново или выберите другое", chatId);
        }
        return true;
    }

    private boolean downloadResume(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("download_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        Resume resume = getResume(matcher, chatId);
        if (resume == null) {
            // todo: say its not found etc
            return true;
        }

        String filePath = resume.getPdfPath();
        File file = new File(filePath);
        try {
            bot.execute(new SendDocument(String.valueOf(chatId), new InputFile(file)));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

        return true;
    }

    private boolean editResume(String data, Integer messageId, Long chatId) {
        Pattern pattern = Pattern.compile("edit_resume_([1-6])");

        Matcher matcher = pattern.matcher(data);
        if (!matcher.matches()) {
            return false;
        }

        return true;
    }

    private boolean deleteResume(String data, Integer messageId, Long chatId) {
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
        String[] s = matcher.group().split("_");

        int numOfResume = Integer.parseInt(s[s.length - 1]);
        List<Resume> resumes = resumeService.getResumesByUserId(chatId);
        if (resumes.size() >= numOfResume) {
            return resumes.get(numOfResume - 1);
        }
        return null;
    }

    private void sendMessage(String text, Long chatId) {
        SendMessage message = new SendMessage();
        message.setParseMode(ParseMode.MARKDOWN);
        message.setChatId(chatId.toString());
        message.setText(text);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }

    private void executeEditMessage(String editMessageToSend, Integer messageId, Long chatId) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setParseMode(ParseMode.MARKDOWN);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(editMessageToSend);

        executeMessage(editMessage);
    }

    private void executeEditMessageWithKeyBoard(String editMessageToSend, Integer messageId, Long chatId, List<String> buttonLabels, List<String> buttonIds) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setParseMode(ParseMode.MARKDOWN);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(messageId);
        editMessage.setText(editMessageToSend);

        editMessage.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, buttonIds));
        executeMessage(editMessage);
    }

    private void executeMessage(EditMessageText message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(BotUtil.ERROR_TEXT + e.getMessage());
        }
    }
}
