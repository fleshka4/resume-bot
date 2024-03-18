package com.resume.unit.bot.json;

import com.resume.bot.json.JsonValidator;
import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.resume.util.Constants.ITEMS_DELIMITER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonValidatorTest {

    @BeforeEach
    public void setUp() {
        Area city = new Area("5", "2", "Город", List.of());
        Area region = new Area("2", "113", "Регион", List.of(city));

        Area moscow = new Area("1", "113", "Москва", List.of());

        Constants.AREAS = List.of(new Area("113", null, "Россия", List.of(region, moscow)), moscow, region, city);

        Constants.COUNTRIES = List.of(new Country("113", "Россия", "url"));
    }

    @Test
    public void testCheckGraduationYearWithYearValidYearTest() {
        Assertions.assertTrue(JsonValidator.checkGraduationYear("2022"));
    }

    @Test
    public void testCheckGraduationYearWithYearNotIntegerYearTest() {
        assertFalse(JsonValidator.checkGraduationYear("invalid"));
    }

    @Test
    public void testCheckGraduationYearWithYearInvalidYearTest() {
        assertFalse(JsonValidator.checkGraduationYear("1700"));
    }

    @Test
    public void testCheckSymbolsLimitValidTextTest() {
        assertTrue(JsonValidator.checkSymbolsLimit("Lorem ipsum dolor sit amet", 50));
    }

    @Test
    public void testCheckSymbolsLimitInvalidTextTest() {
        assertFalse(JsonValidator.checkSymbolsLimit("Lorem ipsum dolor sit amet", 10));
    }

    @Test
    public void testCheckLinkFormatValidUrlTest() {
        assertTrue(JsonValidator.checkLinkFormat("https://example.com"));
    }

    @Test
    public void testCheckLinkFormatInvalidUrlTest() {
        assertFalse(JsonValidator.checkLinkFormat("example.com"));
    }

    @Test
    public void testCheckBirthdayMaxDateTest() {
        assertTrue(JsonValidator.checkBirthday("01-01-2010"));
    }

    @Test
    public void testCheckBirthdayMaxInvalidDateTest() {
        assertFalse(JsonValidator.checkBirthday("01-01-2020"));
    }

    @Test
    public void testCheckBirthdayMinDateTest() {
        assertTrue(JsonValidator.checkBirthday("01-01-1990"));
    }

    @Test
    public void testCheckBirthdayMinInvalidDateTest() {
        assertFalse(JsonValidator.checkBirthday("31-02-1700"));
    }

    @Test
    public void testCheckBirthdayNotIntegerDateTest() {
        assertFalse(JsonValidator.checkBirthday("invalid"));
    }

    @Test
    public void testCheckExperienceValidDatesTest() {
        assertTrue(JsonValidator.checkExperience("01-2020 - 02-2021", "01-01-1990"));
    }

    @Test
    public void testCheckExperienceInvalidDatesTest() {
        assertFalse(JsonValidator.checkExperience("01-01-2022 - 01-01-2021", "01-01-1990"));
    }

    @Test
    public void testCheckAlphaFormatValidAlphaTest() {
        assertTrue(JsonValidator.checkAlphaFormat("Lorem"));
    }

    @Test
    public void testCheckAlphaFormatInvalidAlphaTest() {
        assertFalse(JsonValidator.checkAlphaFormat("123"));
    }

    @Test
    public void testCheckAlphaSpaceFormatValidAlphaSpaceTest() {
        assertTrue(JsonValidator.checkAlphaSpaceFormat("John Doe"));
    }

    @Test
    public void testCheckAlphaSpaceFormatInvalidAlphaSpaceTest() {
        assertFalse(JsonValidator.checkAlphaSpaceFormat("John123"));
    }

    @Test
    public void testCheckStringWithPunctuationValidStringTest() {
        assertTrue(JsonValidator.checkStringWithPunctuation("Hello world"));
    }

    @Test
    public void testCheckStringWithPunctuationInvalidStringTest() {
        assertFalse(JsonValidator.checkStringWithPunctuation("123@abc"));
    }

    @Test
    public void testCheckAlphanumericFormatValidAlphanumericTest() {
        assertTrue(JsonValidator.checkAlphanumericFormat("abc123"));
    }

    @Test
    public void testCheckAlphanumericFormatInvalidAlphanumericTest() {
        assertFalse(JsonValidator.checkAlphanumericFormat("abc@123"));
    }

    @Test
    public void testCheckNumericFormatValidNumericTest() {
        assertTrue(JsonValidator.checkNumericFormat("123456"));
    }

    @Test
    public void testCheckNumericFormatInvalidNumericTest() {
        assertFalse(JsonValidator.checkNumericFormat("123abc"));
    }

    @Test
    public void testCheckDateFormatValidDateTest() {
        assertTrue(JsonValidator.checkDateFormat("01-01-2022"));
    }

    @Test
    public void testCheckDateFormatInvalidDateTest() {
        assertFalse(JsonValidator.checkDateFormat("2022-01-01")); // Invalid date format
    }

    @Test
    public void testCheckPhoneFormatValidPhoneTest() {
        assertTrue(JsonValidator.checkPhoneFormat("12345678901"));
    }

    @Test
    public void testCheckPhoneFormatInvalidPhoneTest() {
        assertFalse(JsonValidator.checkPhoneFormat("12345"));
    }

    @Test
    public void testCheckEmailFormatValidEmailTest() {
        assertTrue(JsonValidator.checkEmailFormat("test@example.com"));
    }

    @Test
    public void testCheckEmailFormatInvalidEmailTest() {
        assertFalse(JsonValidator.checkEmailFormat("test@example"));
    }

    @Test
    public void testCheckVisibilityTypeValidTypeTest() {
        assertTrue(JsonValidator.checkVisibilityType("не видно никому"));
    }

    @Test
    public void testCheckVisibilityTypeInvalidTypeTest() {
        assertFalse(JsonValidator.checkVisibilityType("invalid"));
    }

    @Test
    public void testCheckTripReadinessValidTypeTest() {
        assertTrue(JsonValidator.checkTripReadiness("готов к командировкам"));
    }

    @Test
    public void testCheckTripReadinessInvalidTypeTest() {
        assertFalse(JsonValidator.checkTripReadiness("unavailable"));
    }

    @Test
    public void testCheckDriverLicenseTypeValidTypeTest() {
        assertTrue(JsonValidator.checkDriverLicenseType("B"));
    }

    @Test
    public void testCheckDriverLicenseTypeInvalidTypeTest() {
        assertFalse(JsonValidator.checkDriverLicenseType("invalid"));
    }

    @Test
    public void testCheckEmploymentTypeValidTypeTest() {
        assertTrue(JsonValidator.checkEmploymentType("Полная занятость"));
    }

    @Test
    public void testCheckEmploymentTypeInvalidTypeTest() {
        assertFalse(JsonValidator.checkEmploymentType("part-time"));
    }

    @Test
    public void testCheckHiddenFieldValidTypeTest() {
        assertTrue(JsonValidator.checkHiddenField("ФИО и фотографию"));
    }

    @Test
    public void testCheckHiddenFieldInvalidTypeTest() {
        assertFalse(JsonValidator.checkHiddenField("visible"));
    }

    @Test
    public void testCheckScheduleTypeValidTypeTest() {
        assertTrue(JsonValidator.checkScheduleType("Полный день"));
    }

    @Test
    public void testCheckScheduleTypeInvalidTypeTest() {
        assertFalse(JsonValidator.checkScheduleType("fixed"));
    }

    @Test
    public void testCheckTravelTimeValidTypeTest() {
        assertTrue(JsonValidator.checkTravelTime("Не имеет значения"));
    }

    @Test
    public void testCheckTravelTimeInvalidTypeTest() {
        assertFalse(JsonValidator.checkTravelTime("one-hour"));
    }

    @Test
    public void testCheckSexValidTypeTest() {
        assertTrue(JsonValidator.checkSex("Мужской"));
    }

    @Test
    public void testCheckSexInvalidTypeTest() {
        assertFalse(JsonValidator.checkSex("female"));
    }

    @Test
    public void testCheckRelocationReadinessValidTypeTest() {
        assertTrue(JsonValidator.checkRelocationReadiness("не готов к переезду"));
    }

    @Test
    public void testCheckRelocationReadinessInvalidTypeTest() {
        assertFalse(JsonValidator.checkRelocationReadiness("not-ready"));
    }

    @Test
    public void testCheckCurrencyValidTypeTest() {
        assertTrue(JsonValidator.checkCurrency("Рубли"));
    }

    @Test
    public void testCheckCurrencyInvalidTypeTest() {
        assertFalse(JsonValidator.checkCurrency("invalid"));
    }

    @Test
    public void testCheckSiteTypeValidTypeTest() {
        assertTrue(JsonValidator.checkSiteType("Другой сайт"));
    }

    @Test
    public void testCheckSiteTypeInvalidTypeTest() {
        assertFalse(JsonValidator.checkSiteType("business"));
    }

    @Test
    public void testCheckContactTypeValidTypeTest() {
        assertTrue(JsonValidator.checkContactType("Домашний телефон"));
    }

    @Test
    public void testCheckContactTypeInvalidTypeTest() {
        assertFalse(JsonValidator.checkContactType("phone"));
    }


    @Test
    public void testCheckEducationLevelValidLevelTest() {
        assertTrue(JsonValidator.checkEducationLevel("Среднее"));
    }

    @Test
    public void testCheckEducationLevelInvalidLevelTest() {
        assertFalse(JsonValidator.checkEducationLevel("Associate")); // Not a valid education level
    }

    @Test
    public void testCheckLanguageLevelValidLevelTest() {
        assertTrue(JsonValidator.checkLanguageLevel("A1 — Начальный"));
    }

    @Test
    public void testCheckLanguageLevelInvalidLevelTest() {
        assertFalse(JsonValidator.checkLanguageLevel("Intermediate")); // Not a valid language level
    }

    @Test
    public void testCheckSkillsValidSkillsTest() {
        assertTrue(JsonValidator.checkSkills("Java, Python, SQL"));
    }

    @Test
    public void testCheckSkillsInvalidSkillsTest() {
        assertFalse(JsonValidator.checkSkills(ITEMS_DELIMITER));
    }

    @Test
    public void testCheckNameValidNameTest() {
        assertTrue(JsonValidator.checkName("Саша"));
    }

    @Test
    public void testCheckNameInvalidNameTest() {
        assertFalse(JsonValidator.checkName("1234"));
    }

    @Test
    public void testCheckCityValidCityTest() {
        assertTrue(JsonValidator.checkCity("Москва"));
    }

    @Test
    public void testCheckCityInvalidCityTest() {
        assertFalse(JsonValidator.checkCity("invalid"));
    }

    @Test
    public void testCheckRegionValidRegionTest() {
        assertTrue(JsonValidator.checkRegion("Регион"));
    }

    @Test
    public void testCheckRegionInvalidRegionTest() {
        assertFalse(JsonValidator.checkRegion("invalid"));
    }

    @Test
    public void testCheckLocationValidLocationTest() {
        assertTrue(JsonValidator.checkLocation("Россия, Регион, Город"));
    }

    @Test
    public void testCheckLocationTwoValidLocationTest() {
        assertTrue(JsonValidator.checkLocation("Россия, Москва"));
    }

    @Test
    public void testCheckLocationInvalidLocationTest() {
        assertFalse(JsonValidator.checkLocation("invalid"));
    }

    @Test
    public void testCheckLocationEmptyLocationTest() {
        assertFalse(JsonValidator.checkLocation(""));
    }

    @Test
    public void testCheckCountryValidCountryTest() {
        assertTrue(JsonValidator.checkCountry("Россия"));
    }

    @Test
    public void testCheckCountryInvalidCountryTest() {
        assertFalse(JsonValidator.checkCountry("invalid"));
    }
}
