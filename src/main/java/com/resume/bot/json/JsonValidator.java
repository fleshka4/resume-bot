package com.resume.bot.json;

import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.util.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import static com.resume.util.Constants.*;

public class JsonValidator {
    public enum ValidationType {
        GRADUATION_YEAR_WITH_YEAR,
        GRADUATION_YEAR_WITH_DATE,
        SYMBOLS_LIMIT,
        BIRTHDAY,
        EXPERIENCE,
        ALPHA_FORMAT,
        ALPHA_SPACE_FORMAT,
        ALPHANUMERIC_FORMAT,
        NUMERIC_FORMAT,
        DATE_FORMAT,
        PHONE_NUMBER_FORMAT,
        EMAIL_FORMAT,
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
        LINK,
        CONTACT_TYPE,
        EDUCATION_LEVEL,
        LANGUAGE_LEVEL,
        COUNTRY_NAME,
        REGION_NAME,
        CITY_NAME,
        LOCATION,
        SKILLS,
        IN_LIST,
        IN_MAP
    }

    public static final Map<ValidationType, Function<Object[], Boolean>> checks = new HashMap<>();

    static {
        checks.put(ValidationType.GRADUATION_YEAR_WITH_YEAR, objects -> checkGraduationYear((String) objects[0]));
        checks.put(ValidationType.SYMBOLS_LIMIT, objects -> checkSymbolsLimit((String) objects[0], (Long) objects[1]));
        checks.put(ValidationType.BIRTHDAY, objects -> checkBirthday((String) objects[0]));
        checks.put(ValidationType.EXPERIENCE, objects -> checkExperience((String) objects[0], (String) objects[1]));
        checks.put(ValidationType.ALPHA_FORMAT, objects -> checkAlphaFormat((String) objects[0]));
        checks.put(ValidationType.ALPHA_SPACE_FORMAT, objects -> checkAlphaSpaceFormat((String) objects[0]));
        checks.put(ValidationType.ALPHANUMERIC_FORMAT, objects -> checkAlphanumericFormat((String) objects[0]));
        checks.put(ValidationType.NUMERIC_FORMAT, objects -> checkNumericFormat((String) objects[0]));
        checks.put(ValidationType.DATE_FORMAT, objects -> checkDateFormat((String) objects[0]));
        checks.put(ValidationType.PHONE_NUMBER_FORMAT, objects -> checkPhoneFormat((String) objects[0]));
        checks.put(ValidationType.EMAIL_FORMAT, objects -> checkEmailFormat((String) objects[0]));
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
        checks.put(ValidationType.LINK, objects -> checkLinkFormat((String) objects[0]));
        checks.put(ValidationType.CONTACT_TYPE, objects -> checkContactType((String) objects[0]));
        checks.put(ValidationType.EDUCATION_LEVEL, objects -> checkEducationLevel((String) objects[0]));
        checks.put(ValidationType.LANGUAGE_LEVEL, objects -> checkLanguageLevel((String) objects[0]));
        checks.put(ValidationType.COUNTRY_NAME, objects -> checkCountry((String) objects[0]));
        checks.put(ValidationType.REGION_NAME, objects -> checkRegion((String) objects[0]));
        checks.put(ValidationType.CITY_NAME, objects -> checkCity((String) objects[0]));
        checks.put(ValidationType.LOCATION, objects -> checkLocation((String) objects[0]));
        checks.put(ValidationType.SKILLS, objects -> checkSkills((String) objects[0]));
        checks.put(ValidationType.IN_LIST, objects -> isInList((String) objects[0], List.of((String[]) objects[1])));
        checks.put(ValidationType.IN_MAP, objects -> isInMap((String) objects[0], (Map<String, String>) objects[1]));
    }

    private static final int GRADUATION_DATE_MIN_YEAR = 1950;
    private static final int BIRTH_DATE_MIN_YEAR = 1900;
    private static final String PHONE_FORMAT = "\\d{11}";
    private static final String DATE_FORMAT = "\\d{2}-\\d{2}-\\d{4}";
    private static final String EMAIL_FORMAT = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static boolean checkGraduationYear(String year) {
        int graduationYear;
        try {
            graduationYear = Integer.parseInt(year);
        } catch (RuntimeException e) {
            return false;
        }

        int currentYear = LocalDate.now().getYear();

        return graduationYear >= GRADUATION_DATE_MIN_YEAR && (graduationYear - currentYear) <= 10;
    }

    public static boolean checkSymbolsLimit(String text, long maxLen) {
        return text.length() < maxLen;
    }

    public static boolean checkLinkFormat(String url) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(url);
    }

    public static boolean checkBirthday(String birthDateStr) {
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (RuntimeException e) {
            return false;
        }
        boolean isEnoughYears = LocalDate.now().isAfter(birthDate.plusYears(14));
        return isEnoughYears && (birthDate.getYear() >= BIRTH_DATE_MIN_YEAR);
    }

    public static boolean checkExperience(String experienceDateStr, String birthDayStr) {
        String[] items = experienceDateStr.split(" - ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate experienceDateStart;
        LocalDate experienceDateEnd;
        LocalDate birthDate;
        try {
            experienceDateStart = LocalDate.parse("01-" + items[0], formatter);
            if (items.length == 1) {
                experienceDateEnd = LocalDate.now().minusDays(1);
            } else if (items.length == 2) {
                experienceDateEnd = LocalDate.parse("01-" + items[1], formatter);
            } else {
                return false;
            }
            birthDate = LocalDate.parse(birthDayStr, formatter);
        } catch (RuntimeException e) {
            return false;
        }
        LocalDate minimumBirthDate = birthDate.plusYears(14);
        return (experienceDateEnd.isAfter(experienceDateStart) || experienceDateEnd.isEqual(experienceDateStart)) &&
                experienceDateStart.isAfter(minimumBirthDate) &&
                experienceDateEnd.isBefore(LocalDate.now());
    }

    public static boolean checkAlphaFormat(String text) {
        return StringUtils.isAlpha(text);
    }

    public static boolean checkAlphaSpaceFormat(String text) {
        return StringUtils.isAlphaSpace(text);
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

    public static boolean checkPhoneFormat(String phone) {
        return phone.matches(PHONE_FORMAT);
    }

    public static boolean checkEmailFormat(String email) {
        return email.matches(EMAIL_FORMAT);
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
        return isInMap(text, Constants.driverLicenseTypes);
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

    public static boolean checkLocation(String text) {
        List<String> items = Arrays.stream(text.split(",")).map(String::trim).toList();

        if (items.isEmpty()) {
            return false;
        }

        String countryName = items.getFirst();
        Optional<Area> country = getAreaByNameDeep(Constants.AREAS, countryName);
        if (country.isEmpty()) {
            return false;
        }

        if (items.size() == 1) {
            return checkCountry(countryName) && country.get().getAreas().isEmpty();
        }
        if (items.size() == 2) {
            String cityName = items.get(1);
            return checkCountry(countryName) && checkCity(cityName) &&
                    getAreaByNameDeep(country.get().getAreas(), cityName).isPresent();
        }
        if (items.size() == 3) {
            String regionName = items.get(1);
            String cityName = items.get(2);

            Optional<Area> region = getAreaByName(country.get().getAreas(), regionName);
            return region.filter(area -> checkCountry(countryName) && checkRegion(regionName) && checkCity(cityName) &&
                    getAreaByName(area.getAreas(), cityName).isPresent()).isPresent();
        }
        return false;
    }

    public static Optional<Area> getAreaByNameDeep(List<Area> areas, String name) {
        for (Area area : areas) {
            if (area.getName().equals(name)) {
                return Optional.of(area);
            } else if (!area.getAreas().isEmpty()) {
                Optional<Area> childArea = getAreaByNameDeep(area.getAreas(), name);
                if (childArea.isPresent()) {
                    return childArea;
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<Area> getAreaByName(List<Area> areas, String name) {
        return areas.stream().filter(area -> area.getName().equals(name)).findFirst();
    }

    public static boolean checkCountry(String text) {
        return isInList(text, Constants.COUNTRIES.stream().map(Country::getName).toArray(String[]::new));
    }

    public static boolean checkRegion(String text) {
        return isInList(text, Constants.AREAS.stream().filter(a -> COUNTRIES_WITH_REGIONS_IDS.contains(a.getId()))
                .map(Area::getAreas).flatMap(List::stream).filter(a -> !a.getAreas().isEmpty())
                .map(Area::getName).toArray(String[]::new));
    }

    public static boolean checkCity(String text) {
        return isInList(text, Constants.AREAS.stream().filter(a -> !a.getId().equals(OTHER_COUNTRIES_JSON_ID))
                .map(Area::getAreas).flatMap(List::stream).filter(a -> a.getAreas().isEmpty()).map(Area::getName)
                .toArray(String[]::new))
                || isInList(text, Constants.AREAS.stream()
                .filter(a -> COUNTRIES_WITH_REGIONS_IDS.contains(a.getId()) || a.getId().equals(OTHER_COUNTRIES_JSON_ID))
                .map(Area::getAreas).flatMap(List::stream).map(Area::getAreas).flatMap(List::stream)
                .map(Area::getName).toArray(String[]::new));
    }

    public static boolean checkSkills(String text) {
        List<String> skills = Arrays.stream(text.split(",")).map(String::trim).toList();
        for (String skill : skills) {
            if (skill.contains(ITEMS_DELIMITER)) { // Restricted symbol
                return false;
            }
        }
        return true;
    }
}
