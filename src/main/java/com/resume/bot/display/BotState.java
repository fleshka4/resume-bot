package com.resume.bot.display;

public enum BotState {
    // Bot / Client states
    START,
    CREATE_RESUME,
    EXPORT_RESUME_HH,
    MY_RESUMES,
    CREATE_RESUME_SCRATCH,
    START_DIALOGUE,
    CHOOSE_TEMPLATE,
    EDIT_CLIENT_RESULT_DATA,
    EDIT_MY_RESUME,
    EDIT_MY_RESUME_TEMPLATE,
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
    ENTER_EDU_LEVEL,
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
    ENTER_ABOUT_ME,

    // Availability of a car
    ENTER_CAR_AVAILABILITY,

    // Person's recommendation
    ENTER_REC_NAME,
    ENTER_REC_POST,
    ENTER_REC_ORGANIZATION,

    ENTER_WISH_POSITION,
    ENTER_WISH_SALARY,
    ENTER_WISH_BUSYNESS,
    ENTER_WISH_SCHEDULE,

    // Person's contacts
    ENTER_CONTACTS,
    ENTER_PHONE,
    ENTER_MAIL,
    ENTER_HOME_PHONE,
    ENTER_WORK_PHONE
}
