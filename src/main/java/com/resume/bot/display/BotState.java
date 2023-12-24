package com.resume.bot.display;

public enum BotState {
    // Bot / Client states
    START,
    CREATE_RESUME,
    EXPORT_RESUME_HH,
    MY_RESUMES,
    CREATE_RESUME_SCRATCH,
    START_DIALOGUE,
    EDIT_CLIENT_RESULT_DATA,
    RESULT_DATA_CORRECT,
    FINISH_DIALOGUE,

    // Dialogue states
    ENTER_NAME,
    ENTER_SURNAME,
    ENTER_PATRONYMIC,
    ENTER_BIRTHDAY,
    ENTER_GENDER,
    ENTER_LOCATION,

    // Education
    ENTER_INSTITUTION,
    ENTER_FACULTY,
    ENTER_SPECIALIZATION,
    ENTER_END_YEAR,

    // Experience
    ENTER_PERIOD_OF_WORK
}
