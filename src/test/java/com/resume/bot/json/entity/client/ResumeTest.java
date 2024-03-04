package com.resume.bot.json.entity.client;

import com.resume.bot.json.entity.client.education.Course;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.client.education.ElementaryEducation;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Id;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResumeTest {

    @Test
    public void gettersAndSettersTest() {
        Resume resume = new Resume();

        resume.setAlternateUrl("alternate_url");
        resume.setDownload(new Download(new Pdf("url")));
        resume.setBirthDate("2022-01-01");
        resume.setDriverLicenseTypes(Collections.singletonList(new Id("1")));
        resume.setEmployments(Collections.singletonList(new Type("1", "employment")));
        resume.setFirstName("John");
        resume.setHasVehicle(true);
        resume.setLastName("Doe");
        resume.setMiddleName("Middle");
        resume.setProfessionalRoles(Collections.singletonList(new Id("1")));
        resume.setRecommendation(Collections.singletonList(new Recommendation("contact", "name", "organization", "position")));
        resume.setSalary(new Salary(50000L, "USD"));
        resume.setSchedules(Collections.singletonList(new Type("1", "schedule")));
        resume.setSkillSet(new HashSet<>(Collections.singletonList("Java")));
        resume.setSkills("Java, Python");
        resume.setTitle("Software Developer");
        resume.setTotalExperience(new TotalExperience(12L));
        resume.setArea(new Id("1"));
        resume.setEducation(new Education(List.of(new Course("course1", "org1", "A", 2021)),
                List.of(new Course("course2", "org2", "B", 2022)),
                List.of(new ElementaryEducation("elementary1", 2020)),
                new Type("UNDERGRAD", "Undergraduate"),
                List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
        ));
        resume.setExperience(Collections.singletonList(new Experience(null, "Company", "1", "company_url",
                "Description", new Employer("alternate_url", "1", null, "Company", "url"),
                "2022-01-01", Collections.singletonList(new Type("1", "industry")), null, "Software Engineer", "2020-01-01")));

        
        assertEquals("alternate_url", resume.getAlternateUrl());
        assertNotNull(resume.getDownload());
        assertEquals("2022-01-01", resume.getBirthDate());
        assertEquals(Collections.singletonList(new Id("1")), resume.getDriverLicenseTypes());
        assertEquals(Collections.singletonList(new Type("1", "employment")), resume.getEmployments());
        assertEquals("John", resume.getFirstName());
        assertEquals(true, resume.getHasVehicle());
        assertEquals("Doe", resume.getLastName());
        assertEquals("Middle", resume.getMiddleName());
        assertEquals(Collections.singletonList(new Id("1")), resume.getProfessionalRoles());
        assertEquals(Collections.singletonList(new Recommendation("contact", "name", "organization", "position")), resume.getRecommendation());
        assertEquals(new Salary(50000L, "USD"), resume.getSalary());
        assertEquals(Collections.singletonList(new Type("1", "schedule")), resume.getSchedules());
        assertEquals(new HashSet<>(Collections.singletonList("Java")), resume.getSkillSet());
        assertEquals("Java, Python", resume.getSkills());
        assertEquals("Software Developer", resume.getTitle());
        assertEquals(new TotalExperience(12L), resume.getTotalExperience());
        assertEquals(new Id("1"), resume.getArea());
        assertEquals(new Education(List.of(new Course("course1", "org1", "A", 2021)),
                List.of(new Course("course2", "org2", "B", 2022)),
                List.of(new ElementaryEducation("elementary1", 2020)),
                new Type("UNDERGRAD", "Undergraduate"),
                List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
        ), resume.getEducation());
        assertEquals(Collections.singletonList(new Experience(null, "Company", "1", "company_url",
                "Description", new Employer("alternate_url", "1", null, "Company", "url"),
                "2022-01-01", Collections.singletonList(new Type("1", "industry")), null, "Software Engineer", "2020-01-01")), resume.getExperience());
    }

    @Test
    public void allArgsConstructorTest() {
        
        Resume resume = new Resume(
                "alternate_url", new Download(new Pdf("url")), "2022-01-01",
                Collections.singletonList(new Id("1")), Collections.singletonList(new Type("1", "employment")),
                "John", true, "Doe", "Middle", Collections.singletonList(new Id("1")),
                Collections.singletonList(new Recommendation("contact", "name", "organization", "position")),
                new Salary(50000L, "USD"), Collections.singletonList(new Type("1", "schedule")),
                new HashSet<>(Collections.singletonList("Java")), "Java, Python", "Software Developer",
                new TotalExperience(12L), new Id("1"),
                new Education(List.of(new Course("course1", "org1", "A", 2021)),
                        List.of(new Course("course2", "org2", "B", 2022)),
                        List.of(new ElementaryEducation("elementary1", 2020)),
                        new Type("UNDERGRAD", "Undergraduate"),
                        List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
                ),
                Collections.singletonList(new Experience(null, "Company", "1", "company_url",
                        "Description", new Employer("alternate_url", "1", null, "Company", "url"),
                        "2022-01-01", Collections.singletonList(new Type("1", "industry")), null, "Software Engineer", "2020-01-01")),
                new Id("1"), Collections.singletonList(new Language("1", "English", new Type("1", "Advanced"))),
                "contacts");

        
        assertEquals("alternate_url", resume.getAlternateUrl());
        assertNotNull(resume.getDownload());
        assertEquals("2022-01-01", resume.getBirthDate());
        assertEquals(Collections.singletonList(new Id("1")), resume.getDriverLicenseTypes());
        assertEquals(Collections.singletonList(new Type("1", "employment")), resume.getEmployments());
        assertEquals("John", resume.getFirstName());
        assertEquals(true, resume.getHasVehicle());
        assertEquals("Doe", resume.getLastName());
        assertEquals("Middle", resume.getMiddleName());
        assertEquals(Collections.singletonList(new Id("1")), resume.getProfessionalRoles());
        assertEquals(Collections.singletonList(new Recommendation("contact", "name", "organization", "position")), resume.getRecommendation());
        assertEquals(new Salary(50000L, "USD"), resume.getSalary());
        assertEquals(Collections.singletonList(new Type("1", "schedule")), resume.getSchedules());
        assertEquals(new HashSet<>(Collections.singletonList("Java")), resume.getSkillSet());
        assertEquals("Java, Python", resume.getSkills());
        assertEquals("Software Developer", resume.getTitle());
        assertEquals(new TotalExperience(12L), resume.getTotalExperience());
        assertEquals(new Id("1"), resume.getArea());
        assertEquals(new Education(List.of(new Course("course1", "org1", "A", 2021)),
                List.of(new Course("course2", "org2", "B", 2022)),
                List.of(new ElementaryEducation("elementary1", 2020)),
                new Type("UNDERGRAD", "Undergraduate"),
                List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
        ), resume.getEducation());
        assertEquals(Collections.singletonList(new Experience(null, "Company", "1", "company_url",
                "Description", new Employer("alternate_url", "1", null, "Company", "url"),
                "2022-01-01", Collections.singletonList(new Type("1", "industry")), null, "Software Engineer", "2020-01-01")), resume.getExperience());
        assertEquals(new Id("1"), resume.getGender());
        assertEquals(Collections.singletonList(new Language("1", "English", new Type("1", "Advanced"))), resume.getLanguage());
        assertEquals("contacts", resume.getContacts());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Resume resume1 = new Resume(
                "alternate_url", new Download(new Pdf("url")), "2022-01-01",
                Collections.singletonList(new Id("1")), Collections.singletonList(new Type("1", "employment")),
                "John", true, "Doe", "Middle", Collections.singletonList(new Id("1")),
                Collections.singletonList(new Recommendation("contact", "name", "organization", "position")),
                new Salary(50000L, "USD"), Collections.singletonList(new Type("1", "schedule")),
                new HashSet<>(Collections.singletonList("Java")), "Java, Python", "Software Developer",
                new TotalExperience(12L), new Id("1"),
                new Education(List.of(new Course("course1", "org1", "A", 2021)),
                        List.of(new Course("course2", "org2", "B", 2022)),
                        List.of(new ElementaryEducation("elementary1", 2020)),
                        new Type("UNDERGRAD", "Undergraduate"),
                        List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
                ),
                Collections.singletonList(new Experience(null, "Company", "1", "company_url",
                        "Description", new Employer("alternate_url", "1", null, "Company", "url"),
                        "2022-01-01", Collections.singletonList(new Type("1", "industry")), null, "Software Engineer", "2020-01-01")),
                new Id("1"), Collections.singletonList(new Language("1", "English", new Type("1", "Advanced"))),
                "contacts");

        Resume resume2 = new Resume(
                "alternate_url", new Download(new Pdf("url")), "2022-01-01",
                Collections.singletonList(new Id("1")), Collections.singletonList(new Type("1", "employment")),
                "John", true, "Doe", "Middle", Collections.singletonList(new Id("1")),
                Collections.singletonList(new Recommendation("contact", "name", "organization", "position")),
                new Salary(50000L, "USD"), Collections.singletonList(new Type("1", "schedule")),
                new HashSet<>(Collections.singletonList("Java")), "Java, Python", "Software Developer",
                new TotalExperience(12L), new Id("1"),
                new Education(List.of(new Course("course1", "org1", "A", 2021)),
                        List.of(new Course("course2", "org2", "B", 2022)),
                        List.of(new ElementaryEducation("elementary1", 2020)),
                        new Type("UNDERGRAD", "Undergraduate"),
                        List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
                ),
                Collections.singletonList(new Experience(null, "Company", "1", "company_url",
                        "Description", new Employer("alternate_url", "1", null, "Company", "url"),
                        "2022-01-01", Collections.singletonList(new Type("1", "industry")), null, "Software Engineer", "2020-01-01")),
                new Id("1"), Collections.singletonList(new Language("1", "English", new Type("1", "Advanced"))),
                "contacts");

        assertEquals(resume1, resume2);
        assertEquals(resume1.hashCode(), resume2.hashCode());
    }

    @Test
    public void toStringTest() {
        Resume resume = new Resume(
                "alternate_url", new Download(new Pdf("url")), "2022-01-01",
                Collections.singletonList(new Id("1")), Collections.singletonList(new Type("1", "employment")),
                "John", true, "Doe", "Middle", Collections.singletonList(new Id("1")),
                Collections.singletonList(new Recommendation("contact", "name", "organization", "position")),
                new Salary(50000L, "USD"), Collections.singletonList(new Type("1", "schedule")),
                new HashSet<>(Collections.singletonList("Java")), "Java, Python", "Software Developer",
                new TotalExperience(12L), new Id("1"),
                new Education(List.of(new Course("course1", "org1", "A", 2021)),
                        List.of(new Course("course2", "org2", "B", 2022)),
                        List.of(new ElementaryEducation("elementary1", 2020)),
                        new Type("UNDERGRAD", "Undergraduate"),
                        List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
                ),
                Collections.singletonList(new Experience(null, "Company", "1", "company_url",
                        "Description", new Employer("alternate_url", "1", null, "Company", "url"),
                        "2022-01-01", Collections.singletonList(new Type("1", "industry")), null, "Software Engineer", "2020-01-01")),
                new Id("1"), Collections.singletonList(new Language("1", "English", new Type("1", "Advanced"))),
                "contacts");

        String expectedToString = "Resume(alternateUrl=alternate_url, download=Download(pdf=Pdf(url=url))," +
                " birthDate=2022-01-01, driverLicenseTypes=[Id(id=1)], employments=[Type(id=1, name=employment)], " +
                "firstName=John, hasVehicle=true, lastName=Doe, middleName=Middle, professionalRoles=[Id(id=1)]," +
                " recommendation=[Recommendation(contact=contact, name=name, organization=organization, position=position)]," +
                " salary=Salary(amount=50000, currency=USD), schedules=[Type(id=1, name=schedule)]," +
                " skillSet=[Java], skills=Java, Python, title=Software Developer, totalExperience=TotalExperience(months=12)," +
                " area=Id(id=1), education=Education(additional=[Course(name=course1, organization=org1, result=A, year=2021)]," +
                " attestation=[Course(name=course2, organization=org2, result=B, year=2022)]," +
                " elementary=[ElementaryEducation(name=elementary1, year=2020)], level=Type(id=UNDERGRAD, name=Undergraduate), " +
                "primary=[PrimaryEducation(name=primary1, nameId=1, organization=org1, organizationId=1, result=A, resultId=2, " +
                "year=2021)]), experience=[Experience(area=null, company=Company, companyId=1, companyUrl=company_url, " +
                "description=Description, employer=Employer(alternateUrl=alternate_url, id=1, logoUrls=null, name=Company," +
                " url=url), end=2022-01-01, industries=[Type(id=1, name=industry)], industry=null, position=Software Engineer," +
                " start=2020-01-01)], gender=Id(id=1), language=[Language(id=1, name=English, level=Type(id=1, name=Advanced))], contacts=contacts)";

        assertEquals(expectedToString, resume.toString());
    }





























//

//
































}
