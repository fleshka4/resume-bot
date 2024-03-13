package com.unit.resume.latex;

import com.resume.bot.json.entity.area.Country;
import com.resume.bot.json.entity.client.Area;
import com.resume.bot.json.entity.client.Experience;
import com.resume.bot.json.entity.client.Resume;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import com.resume.latex.Placeholder;
import com.resume.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderTest {
    private static Resume resume;
    private static final String firstName = "Ivan";
    private static final String middleName = "Ivanovich";
    private static final String lastName = "Ivanov";
    private static final String birthDate = "12-04-1961";
    private static final String title = "Programmer";
    private static final String phoneContacts = "88005553535";
    private static final String emailContacts = "gagag@xxx.ru";
    private static final Id areaId = new Id("1");
    private static final String skills = "Good programmer.";

    private static final com.resume.bot.json.entity.area.Area city =
            new com.resume.bot.json.entity.area.Area("5", "2", "Город", List.of());
    private static final com.resume.bot.json.entity.area.Area region =
            new com.resume.bot.json.entity.area.Area("2", "113", "Регион", List.of(city));
    private static final com.resume.bot.json.entity.area.Area moscow =
            new com.resume.bot.json.entity.area.Area("1", "113", "Москва", List.of());

    private static final Education education = new Education(null,
            null,
            null,
            new Type("bachelor", "Бакалавр"),
            List.of(new PrimaryEducation(
                    "SPbPU",
                    null,
                    "ICST",
                    null,
                    "Prog. eng.",
                    null,
                    2024))
    );

    private static final List<Experience> experience = List.of(new Experience(
            new Area("1", "Москва"),
            "Company",
            null,
            "http://nexign.com",
            "I was a programmer.",
            null,
            "2023-10-01",
            List.of(new Type("1", "Industry")),
            null,
            "Programmer",
            "2023-08-01"
    ));

    private static final List<Id> driverLicenseTypes = List.of(
            new Id("A"),
            new Id("B"),
            new Id("C"));

    @BeforeAll
    public static void setupResume() {
        resume = new Resume();
        resume.setFirstName(firstName);
        resume.setMiddleName(middleName);
        resume.setLastName(lastName);
        resume.setBirthDate(birthDate);
        resume.setHasVehicle(true);
        resume.setDriverLicenseTypes(driverLicenseTypes);
        resume.setTitle(title);
        resume.setContacts(phoneContacts);
        resume.setArea(areaId);
        resume.setSkills(skills);
        resume.setEducation(education);
        resume.setExperience(experience);

        Constants.AREAS = List.of(new com.resume.bot.json.entity.area.Area("113", null, "Россия", List.of(region, moscow)), moscow, region, city);
        Constants.COUNTRIES = List.of(new Country("113", "Россия", "url"));
    }

    @Test
    public void testReplaceValueName() {
        String expectedValue = "%s %s %s".formatted(lastName, firstName, middleName);
        Assertions.assertEquals(Placeholder.PLACE_FOR_NAME.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueBirthday() {
        String expectedValue = birthDate;
        Assertions.assertEquals(Placeholder.PLACE_FOR_BIRTHDAY.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueVehicle() {
        String expectedValue = "есть";
        Assertions.assertEquals(Placeholder.PLACE_FOR_VEHICLE.replaceValue(resume), expectedValue);

        resume.setHasVehicle(false);
        expectedValue = "нет";
        Assertions.assertEquals(Placeholder.PLACE_FOR_VEHICLE.replaceValue(resume), expectedValue);

        resume.setHasVehicle(null);
        expectedValue = "";
        Assertions.assertEquals(Placeholder.PLACE_FOR_VEHICLE.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueDriverLicense() {
        String expectedValue = driverLicenseTypes.stream().map(Id::getId).collect(Collectors.joining(", "));
        Assertions.assertEquals(Placeholder.PLACE_FOR_DRIVER_LICENSE.replaceValue(resume), expectedValue);

        resume.setDriverLicenseTypes(null);
        expectedValue = "";
        Assertions.assertEquals(Placeholder.PLACE_FOR_DRIVER_LICENSE.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValuePosition() {
        String expectedValue = title;
        Assertions.assertEquals(Placeholder.PLACE_FOR_POSITION.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueNumber() {
        resume.setContacts(phoneContacts);
        String expectedValue = phoneContacts;
        Assertions.assertEquals(Placeholder.PLACE_FOR_NUMBER.replaceValue(resume), expectedValue);

        resume.setContacts(null);
        expectedValue = "";
        Assertions.assertEquals(Placeholder.PLACE_FOR_NUMBER.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueEmail() {
        resume.setContacts(emailContacts);
        String expectedValue = emailContacts;
        Assertions.assertEquals(Placeholder.PLACE_FOR_EMAIL.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueCity() {
        String expectedValue = moscow.getName();
        Assertions.assertEquals(Placeholder.PLACE_FOR_CITY.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueCountry() {
        String expectedValue = Constants.COUNTRIES.get(0).getName();
        Assertions.assertEquals(Placeholder.PLACE_FOR_COUNTRY.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueAbout() {
        String expectedValue = skills;
        Assertions.assertEquals(Placeholder.PLACE_FOR_ABOUT.replaceValue(resume), expectedValue);

        resume.setSkills(null);
        expectedValue = "";
        Assertions.assertEquals(Placeholder.PLACE_FOR_ABOUT.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueEducation() {
        String level = "Уровень образования: %s\\\\\\\\".formatted(education.getLevel().getName());
        PrimaryEducation pe = education.getPrimary().get(0);
        String primary = """
                Высшее образование:\\\\\\\\
                Учебное заведение: %s\\\\\\\\
                Факультет: %s\\\\\\\\
                Специализация: %s\\\\\\\\
                Год окончания: %d\\\\\\\\
                """.formatted(
                pe.getName(),
                pe.getOrganization(),
                pe.getResult(),
                pe.getYear());
        String expectedValue = """
                %s
                %s
                %s
                %s
                %s
                """.formatted(level,
                primary,
                "",
                "",
                "");
        Assertions.assertEquals(Placeholder.PLACE_FOR_EDUCATION.replaceValue(resume), expectedValue);
    }

    @Test
    public void testReplaceValueExperience() {
        Experience exp = experience.get(0);
        String expectedValue = """
                    Период работы: %s\\\\\\\\
                    Компания: %s\\\\\\\\
                    Город: %s\\\\\\\\
                    Ссылка: %s\\\\\\\\
                    Должность: %s\\\\\\\\
                    Обязанности: %s\\\\\\\\
                    """.formatted("%s - %s".formatted(
                        exp.getStart(),
                        exp.getEnd()),
                exp.getCompany(),
                "%s, %s".formatted(Constants.COUNTRIES.get(0).getName(), moscow.getName()),
                exp.getCompanyUrl(),
                exp.getPosition(),
                exp.getDescription());
        Assertions.assertEquals(Placeholder.PLACE_FOR_EXPERIENCE.replaceValue(resume), expectedValue);
    }
}
