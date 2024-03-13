package com.unit.resume.bot.service;

import com.resume.bot.ResumeBot;
import com.resume.bot.config.BotConfig;
import com.resume.bot.display.BotState;
import com.resume.bot.display.CallbackActionHandler;
import com.resume.bot.display.MessageUtil;
import com.resume.bot.display.handler.CallbackActionFactory;
import com.resume.bot.display.handler.CreateResumeActionHandler;
import com.resume.bot.service.ResumeService;
import com.resume.bot.service.TemplateService;
import com.resume.bot.service.UserService;
import com.unit.resume.bot.test_util.TestUtil;
import com.resume.util.BotUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ResumeBotServiceTest {
    @Mock
    private BotConfig botConfig;

    @Mock
    private CallbackActionFactory callbackActionFactory;

    @Mock
    private UserService userService;

    @Mock
    private ResumeService resumeService;

    @Mock
    private TemplateService templateService;

    @Mock
    private CreateResumeActionHandler createResumeActionHandler;

    @Mock
    private BotUtil botUtil;

    @Mock
    private CallbackActionHandler callbackActionHandler;

    @InjectMocks
    @Spy
    private ResumeBot resumeBot;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterTestClass
    public void tearDown() {
        clearAllCaches();
    }

    @Test
    public void testOnUpdateReceivedStartCommand() {
        resumeBot.onUpdateReceived(TestUtil.createUpdateByCommand("/start"));

        verify(resumeBot).startCommandReceived(any(), any(SendMessage.class));
    }

//    @Test
//    public void testOnUpdateReceivedMenuCommand() {
//        resumeBot.onUpdateReceived(TestUtil.createUpdateByCommand("/menu"));
//
//        verify(resumeBot).menuCommandReceived(any(SendMessage.class));
//    }

    @Test
    public void testOnUpdateReceivedCallbackQueryComeToMenu() {
        CallbackQuery callbackQuery = TestUtil.createCallBackQuery();
        callbackQuery.setData("yes_go_to_menu");

        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        resumeBot.onUpdateReceived(update);

        try (MockedStatic<BotUtil> utilities = Mockito.mockStatic(BotUtil.class)) {
            utilities.when(() -> BotUtil.createMenu(any(SendMessage.class), eq(resumeBot)))
                    .thenAnswer(Answers.RETURNS_DEFAULTS);
        }
    }

    @Test
    public void testOnUpdateReceivedCallbackQueryNotComeToMenu() {
        CallbackQuery callbackQuery = TestUtil.createCallBackQuery();
        callbackQuery.setData("no_go_to_menu");

        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        resumeBot.onUpdateReceived(update);

        try (MockedStatic<MessageUtil> utilities = Mockito.mockStatic(MessageUtil.class)) {
            utilities.when(() -> MessageUtil.sendMessage(eq(resumeBot), anyString(), any(SendMessage.class)))
                    .thenAnswer(Answers.RETURNS_DEFAULTS);
        }
    }

//    @Test
//    public void testOnUpdateReceivedCallbackQuerySkipContacts() {
//        CallbackQuery callbackQuery = TestUtil.createCallBackQuery();
//        callbackQuery.setData("skip_contacts");
//
//        Update update = new Update();
//        update.setCallbackQuery(callbackQuery);
//
//        BotUtil.userStates.put(1L, BotState.FINISH_DIALOGUE);
//        resumeBot.onUpdateReceived(update);
//
//        verify(resumeBot).finishDialogueWithClient(eq(1L), any(SendMessage.class));
////        verify(resumeBot).sendResultMessageAboutClientData(anyMap(), any(SendMessage.class));
//    }

    @Test
    public void testOnUpdateReceivedCallbackQueryWithoutData() {
        CallbackQuery callbackQuery = TestUtil.createCallBackQuery();
        callbackQuery.setData("");

        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        resumeBot.onUpdateReceived(update);

        verify(callbackActionHandler, never()).performAction(anyString(), anyInt(), anyLong());
    }

    @Test
    public void testUpdateTemplateWithEditState() {
        CallbackQuery callbackQuery = TestUtil.createCallBackQuery();
        callbackQuery.setData("template_some_data");

        Update update = new Update();
        update.setCallbackQuery(callbackQuery);

        BotUtil.userStates.put(1L, BotState.EDIT_MY_RESUME_TEMPLATE);

        resumeBot.onUpdateReceived(update);

        verify(resumeBot).updateTemplate(eq("template_some_data"), eq(1), eq(1L));
    }

    // integration?
//    @Test
//    public void testUpdateTemplateWithChooseState() {
//        CallbackQuery callbackQuery = TestUtil.createCallBackQuery();
//        callbackQuery.setData("template_1");
//
//        Update update = new Update();
//        update.setCallbackQuery(callbackQuery);
//
//        BotUtil.userStates.put(1L, BotState.CHOOSE_TEMPLATE);
//
//        resumeBot.onUpdateReceived(update);
//
//        verify(resumeBot).setTemplate(any(Resume.class), eq("template_1".split("_")));
//        verify(resumeService).updatePdfPathByResumeId(anyString(), anyInt());
//        verify(botUtil);
//        BotUtil.createMenu(any(SendMessage.class), resumeBot);
//    }

//    @Test
//    public void testOnUpdateReceivedCallbackQuery() {
//        CallbackQuery callbackQuery = TestUtil.createCallBackQuery();
//        callbackQuery.setData("create_resume");
//
//        Update update = new Update();
//        update.setCallbackQuery(callbackQuery);
//
//        BotUtil.userStates.put(1L, BotState.CREATE_RESUME);
//        resumeBot.onUpdateReceived(update);
//
//        verify(callbackActionFactory).createCallbackActionHandler(resumeBot, eq("create_resume"));
//        verify(callbackActionHandler).performAction(eq("create_resume"), eq(1), eq(1L));
//    }
}
