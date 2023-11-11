package com.resume.bot.json;

import com.resume.bot.exception.json.JsonValidationException;
import org.apache.commons.lang3.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

public class JsonValidator {
    private static final int GRADUATION_DATE_MIN_YEAR = 1950;
    private static final int BIRTH_DATE_MIN_YEAR = 1900;
    private static final String NUMBER_FORMAT = "\\d{11}";
    private static final String DATE_FORMAT = "\\d{2}-\\d{2}-\\d{4}";


    public static void checkGraduationYear(int year) {
        checkGraduationYear(Date.valueOf(LocalDate.of(year, 12, 31)));
    }

    public static void checkGraduationYear(Date graduationDate) {
        int year = graduationDate.toLocalDate().getYear();
        boolean isCorrectYears = LocalDate.now().isAfter(graduationDate.toLocalDate().minus(Period.ofYears(10)));

        if ((year < GRADUATION_DATE_MIN_YEAR) || !isCorrectYears) {
            throw new JsonValidationException("Incorrect graduation date!");
        }
    }

    public static void checkSymbolsLimit(String text, long maxLen) {
        if (text.length() > maxLen) {
            throw new JsonValidationException(String.format("Symbols limit %d exceeded!", maxLen));
        }
    }

    public static void checkBirthday(Date birthDate) {
        int birthYear = birthDate.toLocalDate().getYear();
        boolean isEnoughYears = LocalDate.now().isAfter(birthDate.toLocalDate().plus(Period.ofYears(14)));

        if ((birthYear < BIRTH_DATE_MIN_YEAR) || !isEnoughYears) {
            throw new JsonValidationException("Incorrect birth date!");
        }
    }

    public static void checkExperience(Date experienceDate, Date birthDate) {
        boolean isEnoughYears = experienceDate.toLocalDate().isAfter(birthDate.toLocalDate().plus(Period.ofYears(14)));
        boolean isFutureExp = experienceDate.toLocalDate().isAfter(LocalDate.now());

        if (!isEnoughYears || isFutureExp) {
            throw new JsonValidationException("Incorrect experience date!");
        }
    }

    public static void checkAlphaFormat(String text) {
        if (!StringUtils.isAlpha(text)) {
            throw new JsonValidationException("Incorrect word alphabetical format!");
        }
    }

    public static void checkAlphanumericFormat(String text) {
        if (!StringUtils.isAlphanumeric(text)) {
            throw new JsonValidationException("Incorrect word alphanumeric format!");
        }
    }

    public static void checkNumericFormat(String text) {
        if (!StringUtils.isNumeric(text)) {
            throw new JsonValidationException("Incorrect word numeric format!");
        }
    }

    public static void checkDateFormat(String text) {
        if (!text.matches(DATE_FORMAT)) {
            throw new JsonValidationException("Incorrect date format!");
        }
    }

    public static void checkPhoneNumberFormat(String text) {
        if (!text.matches(NUMBER_FORMAT)) {
            throw new JsonValidationException("Incorrect number phone format!");
        }
    }
}
