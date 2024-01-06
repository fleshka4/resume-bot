package com.resume.bot.display;

import com.resume.bot.json.JsonValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResumeField {
    NAME("имя") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    SURNAME("фамилия") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    PATRONYMIC("отчество") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    SEX("пол") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSex(checkValue);
        }

        @Override
        public String message() {
            return "Пропаганда ЛГБТ запрещена УК РФ ст.282!";
        }
    },

    BIRTHDAY("дата рождения") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkBirthday(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_DATE_STRING.formatted(getValue());
        }
    },

    LIVE_LOCATION("место жительства") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256) && JsonValidator.checkLocation(checkValue);
        }

        @Override
        public String message() {
            return formatLocation(getValue());
        }
    },

    EDUCATION_LEVEL("уровень образования") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkEducationLevel(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }
    },

    EDUCATION_INSTITUTION("учебное заведение") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_512) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_512);
        }
    },

    EDUCATION_FACULTY("факультет") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    EDUCATION_SPECIALIZATION("специализация") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    EDUCATION_END_YEAR("год окончания") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkGraduationYear(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_NUMBER_STRING.formatted(getValue());
        }
    },

    EXPERIENCE_PERIOD("опыт работы") {
        @Override
        public boolean processCheck(String checkValue) {
            try {
                String[] dates = checkValue.split("-", 2);
                return JsonValidator.checkExperience(dates[0].trim(), dates[1].trim());
            } catch (RuntimeException e) {
                return false;
            }
        }

        @Override
        public String message() {
            return INCORRECT_PERIOD_STRING.formatted(getValue());
        }
    },

    EXPERIENCE_ORG_NAME("название организации") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_512);
        }

        @Override
        public String message() {
            return EXCEEDED_SYMBOLS_LIMIT_STRING.formatted(getValue(), MAX_LEN_512);
        }
    },

    EXPERIENCE_ORG_CITY("город организации") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256) && JsonValidator.checkCity(checkValue);
        }

        @Override
        public String message() {
            return formatLocation(getValue());
        }
    },

    EXPERIENCE_ORG_INDUSTRY("отрасль") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256) && JsonValidator.checkCity(checkValue);
        }

        @Override
        public String message() {
            return formatLocation(getValue());
        }
    },

    EXPERIENCE_ORG_LINK("ссылка") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_256) && JsonValidator.checkLinkFormat(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_HYPERLINK_STRING.formatted(MAX_LEN_256);
        }
    },

    EXPERIENCE_POST("должность") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    EXPERIENCE_DUTIES("обязанности") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_4096) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_4096);
        }
    },

    SKILLS("навыки") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    ABOUT_ME("о себе") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_4096) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_4096);
        }
    },

    DRIVER_LICENCE("категория прав") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkDriverLicenseType(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_PARAM_STRING.formatted(getValue());
        }
    },

    REC_NAME("имя выдавшего рекомендацию") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_512) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_512);
        }
    },

    REC_POST("должность выдавшего рекомендацию") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    REC_ORGANIZATION("организация выдавшего рекомендацию") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    WISH_POSITION("желаемая позиция") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkSymbolsLimit(checkValue, MAX_LEN_128) && JsonValidator.checkAlphaSpaceFormat(checkValue);
        }

        @Override
        public String message() {
            return formatLetters(getValue(), MAX_LEN_128);
        }
    },

    WISH_SALARY("желаемая зарплата") {
        @Override
        public boolean processCheck(String checkValue) {
            return JsonValidator.checkNumericFormat(checkValue);
        }

        @Override
        public String message() {
            return INCORRECT_SALARY_STRING.formatted(getValue());
        }
    };


    private static final short MAX_LEN_128 = 128;
    private static final short MAX_LEN_256 = 256;
    private static final short MAX_LEN_512 = 512;
    private static final short MAX_LEN_4096 = 4096;

    private static final String MUST_CONTAIN_LETTERS_STRING = "Параметр \"%s\" должен состоять только из букв! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_PARAM_WITH_SYMBOLS_STRING = "Параметр \"%s\" указан некорректно! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_PARAM_STRING = "Параметр \"%s\" указан некорректно!";
    private static final String INCORRECT_LOCATION_STRING = "Параметр \"%s\" указан неверно! Также, число символов не должно превышать %d!";
    private static final String EXCEEDED_SYMBOLS_LIMIT_STRING = "В параметре \"%s\" превышено число символов! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_HYPERLINK_STRING = "Ссылка на организацию написана некорректно! Также, число символов не должно превышать %d!";
    private static final String INCORRECT_NUMBER_STRING = "В параметре \"%s\" указан некорректный год!";
    private static final String INCORRECT_PERIOD_STRING = "В параметре \"%s\" указан некорректный период!";
    private static final String INCORRECT_DATE_STRING = "Параметр \"%s\" содержит в себе некорректную дату!";
    private static final String INCORRECT_SALARY_STRING = "В параметре \"%s\" указано некорректное значение!";

    private final String value;

    abstract public boolean processCheck(String checkValue);

    abstract public String message();

    private static String formatLetters(String string, short maxLen) {
        return MUST_CONTAIN_LETTERS_STRING.formatted(string, maxLen);
    }

    private static String formatLocation(String string) {
        return INCORRECT_LOCATION_STRING.formatted(string, MAX_LEN_256);
    }
}