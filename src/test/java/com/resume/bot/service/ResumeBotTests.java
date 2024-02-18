package com.resume.bot.service;

import com.resume.bot.config.BotConfig;
import com.resume.bot.display.BotState;
import com.resume.bot.display.MessageUtil;
import com.resume.bot.display.handler.CallbackActionFactory;
import com.resume.util.BotUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
public class ResumeBotTests {
    private Long chatId = 882258832L;
    private Long tgUId = 882258832L;

    @Mock
    private BotConfig botConfig;

    @MockBean
    private CallbackActionFactory callbackActionFactory;

    @MockBean
    private HeadHunterService headHunterService;

    @MockBean
    private UserService userService;

    @MockBean
    private ResumeService resumeService;

    @MockBean
    private TemplateService templateService;

    @Autowired
    private ResumeBot resumeBot;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    public Update buildUpdateFromText(String text) {
        Update update = new Update();
        org.telegram.telegrambots.meta.api.objects.User user =
                new org.telegram.telegrambots.meta.api.objects.User();
        user.setId(tgUId);

        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);
        message.setFrom(user);
        message.setText(text);

        update.setMessage(message);
        return update;
    }

    @Test
    public void testStartCommand() {

    }

    @Test
    public void testStartDialogueWithClient() {
        BotUtil.dialogueStates.put(chatId, BotState.ENTER_NAME);

        String receivedText = "John";

        resumeBot.onUpdateReceived(buildUpdateFromText(receivedText));

        try (MockedStatic<MessageUtil> utilities = Mockito.mockStatic(MessageUtil.class)) {
            utilities.verify(() -> MessageUtil.sendMessage(any(), any(), eq(chatId)));
        }
    }


//    @Test
//    public void testOnUpdateReceived() {
//        // Create mock objects
//        Update update = new Update();
//        Message message = new Message();
//        User user = new User();
//        user.setId("");
//        message.setChat(new Chat().setId());
//        message.setFrom(user);
//        update.setMessage(message);
//
//        // Mock behavior
//        when(update.hasMessage()).thenReturn(true);
//        when(update.hasCallbackQuery()).thenReturn(false);
//        when(message.getText()).thenReturn("/start");
//
//        // Call method under test
//        resumeBot.onUpdateReceived(update);
//
//        // Verify expected behavior
//        // Verify that userService.saveUser is called with the expected arguments
//        verify(userService).saveUser(any());
//        // Verify that callbackActionFactory.createCallbackActionHandler is never called
//        verify(callbackActionFactory, never()).createCallbackActionHandler(any(), anyString());
//    }
//
//    @Test
//    public void testOnUpdateReceived_CallbackQuery_Template() throws TelegramApiException {
//        // Create mock objects
//        Update update = new Update();
//        CallbackQuery callbackQuery = mock(CallbackQuery.class);
//        when(callbackQuery.getData()).thenReturn("template_data");
//        when(callbackQuery.getMessage()).thenReturn(new Message());
//        update.setCallbackQuery(callbackQuery);
//
//        // Mock behavior
//        when(update.hasMessage()).thenReturn(false);
//        when(update.hasCallbackQuery()).thenReturn(true);
//        when(callbackQuery.getData()).thenReturn("template_data");
//
//        // Call method under test
//        resumeBot.onUpdateReceived(update);
//
//        // Verify expected behavior
//        // Verify that execute method is called with the expected arguments
//        verify(resumeBot, times(1)).execute((SendDocument) any());
//    }

}
