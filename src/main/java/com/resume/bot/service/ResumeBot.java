package com.resume.bot.service;

import com.resume.bot.config.BotConfig;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResumeBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                SendMessage sendMessageRequest = new SendMessage();
                sendMessageRequest.setChatId(message.getChatId().toString());

                if ("/start".equals(message.getText())) {
                    startCommandReceived(message, sendMessageRequest);
                } else {
                    sendMessage("Извините, я не понимаю эту команду.:cry:", sendMessageRequest);
                }
            }
        }
    }

    private void startCommandReceived(Message username, SendMessage sendMessageRequest) {
        String startMessage = "Привет " + username.getChat().getFirstName() + " !:wave: Я бот для создания резюме." +
                "Давай вместе составим профессиональное резюме для твоего будущего успеха!:star2:\n" +
                "Просто следуй моим инструкциям.";

        log.info("Replied to user: " + username.getChat().getFirstName());

        sendMessage(EmojiParser.parseToUnicode(startMessage), sendMessageRequest);
    }

    private void sendMessage(String messageToSend, SendMessage sendMessageRequest) {
        sendMessageRequest.setText(messageToSend);
        try {
            execute(sendMessageRequest);
        } catch (TelegramApiException e) {
            log.error("Error occured: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
