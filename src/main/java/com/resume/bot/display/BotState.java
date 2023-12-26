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
    ENTER_PERIOD_OF_WORK,
    ENTER_NAME_OF_ORGANIZATION,
    ENTER_CITY_OF_ORGANIZATION,
    ENTER_SITE_OF_ORGANIZATION,
    ENTER_POST_IN_ORGANIZATION,
    ENTER_DUTIES_IN_ORGANIZATION,

    // Skills
    ENTER_SKILLS,

    // About me
    ENTER_ABOUT_ME
}
