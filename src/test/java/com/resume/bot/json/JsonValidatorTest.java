package com.resume.bot.json;

import com.resume.bot.json.entity.area.Area;
import com.resume.bot.json.entity.area.Country;
import com.resume.util.Constants;
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
    public void testCheckGraduationYearWithYearValidYearTestTest() {
        assertTrue(JsonValidator.checkGraduationYear("2022"));
    }

    @Test
    public void testCheckGraduationYearWithYearNotIntegerYearTestTest() {
        assertFalse(JsonValidator.checkGraduationYear("invalid"));
    }

    @Test
    public void testCheckGraduationYearWithYearInvalidYearTestTest() {
        assertFalse(JsonValidator.checkGraduationYear("1700"));
    }

    @Test
    public void testCheckSymbolsLimitValidTextTestTest() {
        assertTrue(JsonValidator.checkSymbolsLimit("Lorem ipsum dolor sit amet", 50));
    }

    @Test
    public void testCheckSymbolsLimitInvalidTextTestTest() {
        assertFalse(JsonValidator.checkSymbolsLimit("Lorem ipsum dolor sit amet", 10));
    }

    @Test
    public void testCheckLinkFormatValidUrlTestTest() {
        assertTrue(JsonValidator.checkLinkFormat("https://example.com"));
    }

    @Test
    public void testCheckLinkFormatInvalidUrlTestTest() {
        assertFalse(JsonValidator.checkLinkFormat("example.com"));
    }


    @Test
    public void testCheckBirthdayValidDateTestTest() {
        assertTrue(JsonValidator.checkBirthday("01-01-1990"));
    }

    @Test
    public void testCheckBirthdayInvalidDateTestTest() {
        assertFalse(JsonValidator.checkBirthday("31-02-1700"));
    }

    @Test
    public void testCheckBirthdayNotIntegerDateTestTest() {
        assertFalse(JsonValidator.checkBirthday("invalid"));
    }

    @Test
    public void testCheckExperienceValidDatesTestTest() {
        assertTrue(JsonValidator.checkExperience("01-2020 - 02-2021", "01-01-1990"));
    }

    @Test
    public void testCheckExperienceInvalidDatesTestTest() {
        assertFalse(JsonValidator.checkExperience("01-01-2022 - 01-01-2021", "01-01-1990"));
    }

    @Test
    public void testCheckAlphaFormatValidAlphaTestTest() {
        assertTrue(JsonValidator.checkAlphaFormat("Lorem"));
    }

    @Test
    public void testCheckAlphaFormatInvalidAlphaTestTest() {
        assertFalse(JsonValidator.checkAlphaFormat("123"));
    }

    @Test
    public void testCheckAlphaSpaceFormatValidAlphaSpaceTestTest() {
        assertTrue(JsonValidator.checkAlphaSpaceFormat("John Doe"));
    }

    @Test
    public void testCheckAlphaSpaceFormatInvalidAlphaSpaceTestTest() {
        assertFalse(JsonValidator.checkAlphaSpaceFormat("John123"));
    }

    @Test
    public void testCheckStringWithPunctuationValidStringTestTest() {
        assertTrue(JsonValidator.checkStringWithPunctuation("Hello world"));
    }

    @Test
    public void testCheckStringWithPunctuationInvalidStringTestTest() {
        assertFalse(JsonValidator.checkStringWithPunctuation("123@abc"));
    }

    @Test
    public void testCheckAlphanumericFormatValidAlphanumericTestTest() {
        assertTrue(JsonValidator.checkAlphanumericFormat("abc123"));
    }

    @Test
    public void testCheckAlphanumericFormatInvalidAlphanumericTestTest() {
        assertFalse(JsonValidator.checkAlphanumericFormat("abc@123"));
    }

    @Test
    public void testCheckNumericFormatValidNumericTestTest() {
        assertTrue(JsonValidator.checkNumericFormat("123456"));
    }

    @Test
    public void testCheckNumericFormatInvalidNumericTestTest() {
        assertFalse(JsonValidator.checkNumericFormat("123abc"));
    }

    @Test
    public void testCheckDateFormatValidDateTestTest() {
        assertTrue(JsonValidator.checkDateFormat("01-01-2022"));
    }

    @Test
    public void testCheckDateFormatInvalidDateTestTest() {
        assertFalse(JsonValidator.checkDateFormat("2022-01-01")); // Invalid date format
    }

    @Test
    public void testCheckPhoneFormatValidPhoneTestTest() {
        assertTrue(JsonValidator.checkPhoneFormat("12345678901"));
    }

    @Test
    public void testCheckPhoneFormatInvalidPhoneTestTest() {
        assertFalse(JsonValidator.checkPhoneFormat("12345"));
    }

    @Test
    public void testCheckEmailFormatValidEmailTestTest() {
        assertTrue(JsonValidator.checkEmailFormat("test@example.com"));
    }

    @Test
    public void testCheckEmailFormatInvalidEmailTestTest() {
        assertFalse(JsonValidator.checkEmailFormat("test@example"));
    }

    @Test
    public void testCheckVisibilityTypeValidTypeTestTest() {
        assertTrue(JsonValidator.checkVisibilityType("не видно никому"));
    }

    @Test
    public void testCheckVisibilityTypeInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkVisibilityType("invalid"));
    }

    @Test
    public void testCheckTripReadinessValidTypeTestTest() {
        assertTrue(JsonValidator.checkTripReadiness("готов к командировкам"));
    }

    @Test
    public void testCheckTripReadinessInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkTripReadiness("unavailable"));
    }

    @Test
    public void testCheckDriverLicenseTypeValidTypeTestTest() {
        assertTrue(JsonValidator.checkDriverLicenseType("B"));
    }

    @Test
    public void testCheckDriverLicenseTypeInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkDriverLicenseType("invalid"));
    }

    @Test
    public void testCheckEmploymentTypeValidTypeTestTest() {
        assertTrue(JsonValidator.checkEmploymentType("Полная занятость"));
    }

    @Test
    public void testCheckEmploymentTypeInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkEmploymentType("part-time"));
    }

    @Test
    public void testCheckHiddenFieldValidTypeTestTest() {
        assertTrue(JsonValidator.checkHiddenField("ФИО и фотографию"));
    }

    @Test
    public void testCheckHiddenFieldInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkHiddenField("visible"));
    }

    @Test
    public void testCheckScheduleTypeValidTypeTestTest() {
        assertTrue(JsonValidator.checkScheduleType("Полный день"));
    }

    @Test
    public void testCheckScheduleTypeInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkScheduleType("fixed"));
    }

    @Test
    public void testCheckTravelTimeValidTypeTestTest() {
        assertTrue(JsonValidator.checkTravelTime("Не имеет значения"));
    }

    @Test
    public void testCheckTravelTimeInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkTravelTime("one-hour"));
    }

    @Test
    public void testCheckSexValidTypeTestTest() {
        assertTrue(JsonValidator.checkSex("Мужской"));
    }

    @Test
    public void testCheckSexInvalidTypeTestTest() {
        assertFalse(JsonValidator.checkSex("female"));
    }

    @Test
    public void testCheckRelocationReadinessValidTypeTestTest() {
        assertTrue(JsonValidator.checkRelocationReadiness("не готов к переезду"));
    }

    @Test
    public void testCheckRelocationReadinessInvalidTypeTestTest() {
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
    public void testCheckStringWithPunctuationInvalidStringTest() {
        assertFalse(JsonValidator.checkStringWithPunctuation("This*is*not*valid"));
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
