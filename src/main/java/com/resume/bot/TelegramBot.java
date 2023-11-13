//package com.resume.bot;
//
//import com.resume.bot.config.BotConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//
//@Component
//@RequiredArgsConstructor
//public class TelegramBot extends TelegramLongPollingBot {
//
//    private final BotConfig botConfig;
//
//    public TelegramBot(BotConfig botConfig) {
//        this.botConfig = botConfig;
//    }
//
//    @Override
//    public String getBotUsername() {
//        return botConfig.getBotName();
//    }
//
//    @Override
//    public String getBotToken() {
//        return botConfig.getToken();
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//
//    }
//
//    private void startCommandReceived(Long chatId, String name) {
//        String answer = "Hi, " + name + ", nice to meet you!" + "\n" +
//                "Enter the currency whose official exchange rate" + "\n" +
//                "you want to know in relation to BYN." + "\n" +
//                "For example: USD";
//        sendMessage(chatId, answer);
//    }
//
//    private void sendMessage(Long chatId, String textToSend) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(String.valueOf(chatId));
//        sendMessage.setText(textToSend);
//        try {
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//
//        }
//    }
//}
