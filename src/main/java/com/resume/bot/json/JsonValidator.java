package com.resume.bot.json;

import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.util.Constants;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

public class JsonValidator {
    public enum ValidationType {
        GRADUATION_YEAR_WITH_YEAR,
        GRADUATION_YEAR_WITH_DATE,
        SYMBOLS_LIMIT,
        BIRTHDAY,
        EXPERIENCE,
        ALPHA_FORMAT,
        ALPHANUMERIC_FORMAT,
        NUMERIC_FORMAT,
        DATE_FORMAT,
        PHONE_NUMBER_FORMAT,
        RESUME_VISIBILITY_TYPE,
        BUSINESS_TRIP_READINESS,
        DRIVER_LICENSE_TYPE,
        EMPLOYMENT_TYPE,
        HIDDEN_FIELD,
        SCHEDULE_TYPE,
        TRAVEL_TIME,
        SEX,
        RELOCATION_READINESS,
        CURRENCY,
        SITE_TYPE,
        CONTACT_TYPE,
        EDUCATION_LEVEL,
        LANGUAGE_LEVEL,
        COUNTRY_NAME,
        REGION_NAME,
        CITY_NAME,
        IN_LIST,
        IN_MAP
    }

    public static final Map<ValidationType, Function<Object[], Boolean>> checks = new HashMap<>();

    static {
        checks.put(ValidationType.GRADUATION_YEAR_WITH_YEAR, objects -> checkGraduationYear((String) objects[0]));
        checks.put(ValidationType.SYMBOLS_LIMIT, objects -> checkSymbolsLimit((String) objects[0], (Long) objects[1]));
        checks.put(ValidationType.BIRTHDAY, objects -> checkBirthday((String) objects[0]));
        checks.put(ValidationType.EXPERIENCE, objects -> checkExperience((Date) objects[0], (Date) objects[1]));
        checks.put(ValidationType.ALPHA_FORMAT, objects -> checkAlphaFormat((String) objects[0]));
        checks.put(ValidationType.ALPHANUMERIC_FORMAT, objects -> checkAlphanumericFormat((String) objects[0]));
        checks.put(ValidationType.NUMERIC_FORMAT, objects -> checkNumericFormat((String) objects[0]));
        checks.put(ValidationType.DATE_FORMAT, objects -> checkDateFormat((String) objects[0]));
        checks.put(ValidationType.PHONE_NUMBER_FORMAT, objects -> checkPhoneNumberFormat((String) objects[0]));
        checks.put(ValidationType.RESUME_VISIBILITY_TYPE, objects -> checkVisibilityType((String) objects[0]));
        checks.put(ValidationType.BUSINESS_TRIP_READINESS, objects -> checkTripReadiness((String) objects[0]));
        checks.put(ValidationType.DRIVER_LICENSE_TYPE, objects -> checkDriverLicenseType((String) objects[0]));
        checks.put(ValidationType.EMPLOYMENT_TYPE, objects -> checkEmploymentType((String) objects[0]));
        checks.put(ValidationType.HIDDEN_FIELD, objects -> checkHiddenField((String) objects[0]));
        checks.put(ValidationType.SCHEDULE_TYPE, objects -> checkScheduleType((String) objects[0]));
        checks.put(ValidationType.TRAVEL_TIME, objects -> checkTravelTime((String) objects[0]));
        checks.put(ValidationType.SEX, objects -> checkSex((String) objects[0]));
        checks.put(ValidationType.RELOCATION_READINESS, objects -> checkRelocationReadiness((String) objects[0]));
        checks.put(ValidationType.CURRENCY, objects -> checkCurrency((String) objects[0]));
        checks.put(ValidationType.SITE_TYPE, objects -> checkSiteType((String) objects[0]));
        checks.put(ValidationType.CONTACT_TYPE, objects -> checkContactType((String) objects[0]));
        checks.put(ValidationType.EDUCATION_LEVEL, objects -> checkEducationLevel((String) objects[0]));
        checks.put(ValidationType.LANGUAGE_LEVEL, objects -> checkLanguageLevel((String) objects[0]));
        checks.put(ValidationType.COUNTRY_NAME, objects -> checkCountry((String) objects[0]));
        checks.put(ValidationType.REGION_NAME, objects -> checkRegion((String) objects[0]));
        checks.put(ValidationType.CITY_NAME, objects -> checkCity((String) objects[0]));
        checks.put(ValidationType.IN_LIST, objects -> isInList((String) objects[0], List.of((String[]) objects[1])));
        // fixme норм или нет?
        checks.put(ValidationType.IN_MAP, objects -> isInMap((String) objects[0], (Map<String, String>) objects[1]));
    }

    private static final int GRADUATION_DATE_MIN_YEAR = 1950;
    private static final int BIRTH_DATE_MIN_YEAR = 1900;
    private static final String NUMBER_FORMAT = "\\d{11}";
    private static final String DATE_FORMAT = "\\d{2}-\\d{2}-\\d{4}";

    private static final String OTHER_COUNTRIES_JSON_ID = "1001";
    private static final List<String> COUNTRIES_WITH_REGIONS_IDS = List.of(
            "113",   // Россия
            "16",    // Беларусь
            "5"      // Украина
    );

    public static boolean checkGraduationYear(String year) {
        int graduationYear = Integer.parseInt(year);
        int currentYear = LocalDate.now().getYear();

        return graduationYear >= GRADUATION_DATE_MIN_YEAR && (graduationYear - currentYear) <= 10;
    }

    public static boolean checkSymbolsLimit(String text, long maxLen) {
        return text.length() < maxLen;
    }

    public static boolean checkBirthday(String birthDateStr) {
        LocalDate birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        boolean isEnoughYears = LocalDate.now().isAfter(birthDate.plusYears(14));
        return isEnoughYears && (birthDate.getYear() >= BIRTH_DATE_MIN_YEAR);
    }

    public static boolean checkExperience(Date experienceDate, Date birthDate) {
        boolean isEnoughYears = experienceDate.after(Date.from(birthDate.toInstant().plus(14, ChronoUnit.YEARS)));
        boolean isFutureExp = experienceDate.after(Date.from(Instant.now()));

        return isEnoughYears && !isFutureExp;
    }

    public static boolean checkAlphaFormat(String text) {
        return StringUtils.isAlpha(text);
    }

    public static boolean checkAlphanumericFormat(String text) {
        return StringUtils.isAlphanumeric(text);
    }

    public static boolean checkNumericFormat(String text) {
        return StringUtils.isNumeric(text);
    }

    public static boolean checkDateFormat(String text) {
        return text.matches(DATE_FORMAT);
    }

    public static boolean checkPhoneNumberFormat(String text) {
        return text.matches(NUMBER_FORMAT);
    }

    public static boolean isInList(String value, String[] array) {
        return Arrays.asList(array).contains(value);
    }

    public static boolean isInList(String value, List<String> array) {
        return array.contains(value);
    }

    public static boolean isInMap(String value, Map<String, String> map) {
        return map.containsValue(value);
    }

    public static boolean checkVisibilityType(String text) {
        return isInMap(text, Constants.visibilityTypes);
    }

    public static boolean checkTripReadiness(String text) {
        return isInMap(text, Constants.tripReadinessTypes);
    }

    public static boolean checkDriverLicenseType(String text) {
        return isInList(text, Constants.driverLicenseTypes);
    }

    public static boolean checkEmploymentType(String text) {
        return isInMap(text, Constants.employmentTypes);
    }

    public static boolean checkHiddenField(String text) {
        return isInMap(text, Constants.hiddenFieldTypes);
    }

    public static boolean checkScheduleType(String text) {
        return isInMap(text, Constants.scheduleTypes);
    }

    public static boolean checkTravelTime(String text) {
        return isInMap(text, Constants.travelTimeTypes);
    }

    public static boolean checkSex(String text) {
        return isInMap(text, Constants.sexTypes);
    }

    public static boolean checkRelocationReadiness(String text) {
        return isInMap(text, Constants.relocationReadinessTypes);
    }

    public static boolean checkCurrency(String text) {
        return isInList(text, Constants.currencies);
    }

    public static boolean checkSiteType(String text) {
        return isInMap(text, Constants.siteTypes);
    }

    public static boolean checkContactType(String text) {
        return isInMap(text, Constants.contactTypes);
    }

    public static boolean checkEducationLevel(String text) {
        return isInMap(text, Constants.educationLevels);
    }

    public static boolean checkLanguageLevel(String text) {
        return isInMap(text, Constants.languageLevels);
    }

    public static boolean checkCountry(String text) {
        return isInList(text, Constants.COUNTRIES.stream().map(Country::getName).toArray(String[]::new));
    }

    public static boolean checkRegion(String text) {
        return  isInList(text, Constants.AREAS.stream().filter(a -> COUNTRIES_WITH_REGIONS_IDS.contains(a.getId()))
                .map(Area::getAreas).flatMap(List::stream).filter(a -> a.getAreas() != null)
                .map(Area::getName).toArray(String[]::new));
    }

    public static boolean checkCity(String text) {
        return isInList(text, Constants.AREAS.stream().filter(a -> !a.getId().equals(OTHER_COUNTRIES_JSON_ID))
                .map(Area::getAreas).flatMap(List::stream).filter(a -> a.getAreas() == null).map(Area::getName)
                .toArray(String[]::new))
                || isInList(text, Constants.AREAS.stream()
                .filter(a -> COUNTRIES_WITH_REGIONS_IDS.contains(a.getId()) || a.getId().equals(OTHER_COUNTRIES_JSON_ID))
                .map(Area::getAreas).flatMap(List::stream).map(Area::getAreas).flatMap(List::stream)
                .map(Area::getName).toArray(String[]::new));
    }
}
