package com.resume.bot.display.handler;

import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.service.ResumeService;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.common.Type;
import com.resume.util.BigKeyboardType;
import com.resume.util.BotUtil;
import com.resume.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

import static com.resume.bot.display.MessageUtil.*;
import static com.resume.util.Constants.INDUSTRIES;

@Slf4j
@RequiredArgsConstructor
public class BigKeyBoardHandler implements CallbackActionHandler {
    private final TelegramLongPollingBot bot;
    private final ResumeService resumeService;

    @Override
    public void performAction(String callbackData, Integer messageId, Long chatId) {
        SendMessage sendMessageRequest = new SendMessage();
        sendMessageRequest.setChatId(chatId);

        String type = callbackData.substring(0, callbackData.indexOf('_'));
        BigKeyboardType bigKeyboardType = BigKeyboardType.INVALID;
        int maxSize;

        switch (type) {
            case "INDUSTRIES" -> {
                bigKeyboardType = BigKeyboardType.INDUSTRIES;
                maxSize = Constants.INDUSTRIES.size();
            }
            case "PROFESSIONAL" -> {
                bigKeyboardType = BigKeyboardType.PROFESSIONAL_ROLES;
                maxSize = Constants.PROFESSIONAL_ROLES.getCategories().size();
            }
            case "RESUMES" -> {
                bigKeyboardType = BigKeyboardType.RESUMES;
                maxSize = resumeService.getResumesByUserId(chatId).size();
            }
            default -> {
                throw new RuntimeException("BigKeyBoardType is " + bigKeyboardType.name());
            }
        }

        if (!callbackData.contains("page")) {
            final int index = Integer.parseInt(callbackData.substring(bigKeyboardType.name().length() + 1));
            String message;

            if (bigKeyboardType == BigKeyboardType.INDUSTRIES) {
                String fieldValue = Constants.INDUSTRIES.get(index).getName();
                BotUtil.personAndIndustry.put(chatId, fieldValue);

                List<Type> industries = new ArrayList<>();
                for (Industry industry : INDUSTRIES) {
                    if (industry.getName().equals(fieldValue)) {
                        industries = industry.getIndustries();
                        break;
                    }
                }

                executeEditMessageWithKeyBoard(bot, "Выберите отрасль:", messageId, chatId,
                        industries.stream().map(Type::getName).toList(), industries.stream().map(Type::getId).toList());
            } else {
                BotUtil.personAndProfessionalRole.put(chatId, Constants.PROFESSIONAL_ROLES.getCategories().get(index).getName());
                BotUtil.dialogueStates.put(chatId, BotState.ENTER_WISH_SALARY);
                List<String> buttonLabels = List.of("Хочу", "Пропустить");
                List<String> callbackDataList = List.of("want_enter_salary", "skip_salary");
                sendMessageRequest.setReplyMarkup(BotUtil.createInlineKeyboard(buttonLabels, callbackDataList));
                message = "Хотите ли Вы указать желаемую зарплату?";
                sendMessage(bot, message, sendMessageRequest);
            }

            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("Выберите ")
                .append(bigKeyboardType == BigKeyboardType.INDUSTRIES
                        ? "сферу деятельности компании"
                        : bigKeyboardType == BigKeyboardType.PROFESSIONAL_ROLES
                        ? "профессиональную роль"
                        : "резюме")
                .append(":\n\n");

        List<String> buttonLabels = new ArrayList<>();
        List<String> callbackDataList = new ArrayList<>();

        final int pageNumber = Integer.parseInt(callbackData.substring(bigKeyboardType.name().length() + 11));
        if (bigKeyboardType == BigKeyboardType.RESUMES) {
            prepareResumeBigKeyboard(pageNumber, buttonLabels, callbackDataList, chatId);
        } else {
            BotUtil.prepareBigKeyboardCreation(pageNumber, bigKeyboardType, message, buttonLabels, callbackDataList);
        }

        executeEditMessageWithBigKeyBoard(bot, message.toString(), messageId, chatId, buttonLabels, callbackDataList,
                pageNumber, maxSize, bigKeyboardType);
    }

    public void prepareResumeBigKeyboard(int pageNumber, List<String> buttonLabels, List<String> callbackDataList, Long chatId) {
        final int pageSize = 5;
        List<com.resume.bot.model.entity.Resume> resumes = resumeService.getResumesByUserId(chatId);

        final int startCounter = pageNumber * pageSize;
        int limit = (pageNumber + 1) * pageSize;
        if ((pageNumber + 1) * pageSize > resumes.size()) {
            limit = startCounter + pageSize - (limit - resumes.size());
        }

        for (int i = startCounter; i < limit; i++) {
            buttonLabels.add(resumes.get(i).getTitle());
            callbackDataList.add("res_" + resumes.get(i).getTitle() + "_" + i);
        }
        buttonLabels.add("Назад");
    }
}
