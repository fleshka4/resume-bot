package com.resume.bot.display.handler;

import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.MessageUtil;
import com.resume.hh_wrapper.config.HhConfig;
import com.resume.util.BotUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import static com.resume.bot.display.MessageUtil.executeEditMessageWithKeyBoard;

@Slf4j
@RequiredArgsConstructor
public class ExportResumeActionHandler implements CallbackActionHandler {

    private final TelegramLongPollingBot bot;
    private final HhConfig hhConfig;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        switch (callbackData) {
            case "export_data_hh" -> {
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

                String message = """
                                Отлично, для экспортирования своего резюме с hh.ru необходимо авторизоваться по следующей [ссылке](%s)!
                                """.formatted(link);

                MessageUtil.sendMessage(bot, message, chatId);
            }
            default -> {

            }
        }
    }
}
