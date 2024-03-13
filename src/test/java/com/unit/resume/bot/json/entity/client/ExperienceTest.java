package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Area;
import com.resume.bot.json.entity.client.Employer;
import com.resume.bot.json.entity.client.Experience;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class ExperienceTest {



//













    @Test
    public void createExperienceWithValidJsonTest() {
        String json = """
                {
                  "area": {
                    "id": "456",
                    "name": "Example Area"
                  },
                  "company": "Example Company",
                  "company_id": "789",
                  "company_url": "https://example.com/company",
                  "description": "Example Description",
                  "employer": {
                    "alternate_url": "https://example.com/employer_alternate",
                    "id": "123",
                    "logo_urls": {
                      "90": "https://example.com/logo_90.png",
                      "240": "https://example.com/logo_240.png",
                      "original": "https://example.com/logo_original.png"
                    },
                    "name": "Example Employer",
                    "url": "https://example.com/employer"
                  },
                  "end": "2022-12-31",
                  "industries": [
                    {"id": "1", "name": "Industry1"},
                    {"id": "2", "name": "Industry2"}
                  ],
                  "industry": {"id": "1", "name": "Industry1"},
                  "position": "Example Position",
                  "start": "2022-01-01"
                }
                """;

        Experience experience = JsonProcessor.createEntityFromJson(json, Experience.class);

        assertNotNull(experience.getArea());
        assertEquals(experience.getArea().getId(), "456");
        assertEquals(experience.getArea().getName(), "Example Area");
        assertEquals(experience.getCompany(), "Example Company");
        assertEquals(experience.getCompanyId(), "789");
        assertEquals(experience.getCompanyUrl(), "https://example.com/company");
        assertEquals(experience.getDescription(), "Example Description");
        assertNotNull(experience.getEmployer());
        assertEquals(experience.getEmployer().getAlternateUrl(), "https://example.com/employer_alternate");
        assertEquals(experience.getEmployer().getId(), "123");
        assertNotNull(experience.getEmployer().getLogoUrls());
        assertEquals(experience.getEmployer().getLogoUrls().getNinety(), "https://example.com/logo_90.png");
        assertEquals(experience.getEmployer().getLogoUrls().getTwoForty(), "https://example.com/logo_240.png");
        assertEquals(experience.getEmployer().getLogoUrls().getOriginal(), "https://example.com/logo_original.png");
        assertEquals(experience.getEmployer().getName(), "Example Employer");
        assertEquals(experience.getEmployer().getUrl(), "https://example.com/employer");
        assertEquals(experience.getEnd(), "2022-12-31");
        assertNotNull(experience.getIndustries());
        assertEquals(experience.getIndustries().size(), 2);
        assertEquals(experience.getIndustries().get(0).getId(), "1");
        assertEquals(experience.getIndustries().get(0).getName(), "Industry1");
        assertEquals(experience.getIndustries().get(1).getId(), "2");
        assertEquals(experience.getIndustries().get(1).getName(), "Industry2");
        assertNotNull(experience.getIndustry());
        assertEquals(experience.getIndustry().getId(), "1");
        assertEquals(experience.getIndustry().getName(), "Industry1");
        assertEquals(experience.getPosition(), "Example Position");
        assertEquals(experience.getStart(), "2022-01-01");
    }

    @Test
    public void createExperienceWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Experience.class));
    }

    @Test
    public void gettersAndSettersTest() {
        Experience experience = new Experience();
        experience.setArea(new Area());
        experience.setCompany("TestCompany");
        experience.setCompanyId("1");
        experience.setCompanyUrl("http://example.com");
        experience.setDescription("TestDescription");
        experience.setEmployer(new Employer());
        experience.setEnd("2024-01-01");
        experience.setIndustries(Collections.singletonList(new Type("1", "TestIndustry")));
        experience.setIndustry(new Type("2", "AnotherIndustry"));
        experience.setPosition("TestPosition");
        experience.setStart("2022-01-01");

        assertEquals("TestCompany", experience.getCompany());
        assertEquals("1", experience.getCompanyId());
        assertEquals("http://example.com", experience.getCompanyUrl());
        assertEquals("TestDescription", experience.getDescription());
        assertEquals("TestPosition", experience.getPosition());
        assertEquals("2022-01-01", experience.getStart());
        assertEquals("2024-01-01", experience.getEnd());

        assertEquals("TestIndustry", experience.getIndustries().get(0).getName());
        assertEquals("AnotherIndustry", experience.getIndustry().getName());

        Experience anotherExperience = new Experience();
        anotherExperience.setArea(new Area());
        anotherExperience.setCompany("AnotherCompany");
        anotherExperience.setCompanyId("2");
        anotherExperience.setCompanyUrl("http://another-example.com");
        anotherExperience.setDescription("AnotherDescription");
        anotherExperience.setEmployer(new Employer());
        anotherExperience.setEnd("2025-01-01");
        anotherExperience.setIndustries(Collections.singletonList(new Type("3", "YetAnotherIndustry")));
        anotherExperience.setIndustry(new Type("4", "LastIndustry"));
        anotherExperience.setPosition("AnotherPosition");
        anotherExperience.setStart("2023-01-01");

        assertNotEquals(anotherExperience, experience);
    }

    @Test
    public void constructorsTest() {
        Experience experience = new Experience(new Area(), "TestCompany", "1", "http://example.com",
                "TestDescription", new Employer(), "2024-01-01",
                Collections.singletonList(new Type("1", "TestIndustry")),
                new Type("2", "AnotherIndustry"), "TestPosition", "2022-01-01");

        assertEquals("TestCompany", experience.getCompany());
        assertEquals("1", experience.getCompanyId());
        assertEquals("http://example.com", experience.getCompanyUrl());
        assertEquals("TestDescription", experience.getDescription());
        assertEquals("TestPosition", experience.getPosition());
        assertEquals("2022-01-01", experience.getStart());
        assertEquals("2024-01-01", experience.getEnd());

        assertEquals("TestIndustry", experience.getIndustries().get(0).getName());
        assertEquals("AnotherIndustry", experience.getIndustry().getName());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Experience experience1 = new Experience(new Area(), "TestCompany", "1", "http://example.com",
                "TestDescription", new Employer(), "2024-01-01",
                Collections.singletonList(new Type("1", "TestIndustry")),
                new Type("2", "AnotherIndustry"), "TestPosition", "2022-01-01");

        Experience experience2 = new Experience(new Area(), "TestCompany", "1", "http://example.com",
                "TestDescription", new Employer(), "2024-01-01",
                Collections.singletonList(new Type("1", "TestIndustry")),
                new Type("2", "AnotherIndustry"), "TestPosition", "2022-01-01");

        Experience experience3 = new Experience(new Area(), "AnotherCompany", "2", "http://another-example.com",
                "AnotherDescription", new Employer(), "2025-01-01",
                Collections.singletonList(new Type("3", "YetAnotherIndustry")),
                new Type("4", "LastIndustry"), "AnotherPosition", "2023-01-01");

        assertEquals(experience1, experience2);
        assertEquals(experience1.hashCode(), experience2.hashCode());

        assertNotEquals(experience1, experience3);
        assertNotEquals(experience1.hashCode(), experience3.hashCode());
    }

    @Test
    public void toStringTest() {
        Experience experience = new Experience(new Area(), "TestCompany", "1", "http://example.com",
                "TestDescription", new Employer(), "2024-01-01",
                Collections.singletonList(new Type("1", "TestIndustry")),
                new Type("2", "AnotherIndustry"), "TestPosition", "2022-01-01");

        String expected = "Experience(area=Area(id=null, name=null, url=/areas/null), company=TestCompany, companyId=1," +
                " companyUrl=http://example.com, description=TestDescription, employer=Employer(alternateUrl=null, id=null," +
                " logoUrls=null, name=null, url=null), end=2024-01-01, industries=[Type(id=1, name=TestIndustry)], " +
                "industry=Type(id=2, name=AnotherIndustry), position=TestPosition, start=2022-01-01)";

        assertEquals(expected, experience.toString());
    }

    @Test
    public void createExperienceTest() {
        Experience experience = Experience.createExperience(new Area(), "TestCompany", "1", "http://example.com",
                "TestDescription", new Employer(), "2024-01-01",
                Arrays.asList(new Type("1", "TestIndustry"), new Type("2", "AnotherIndustry")),
                new Type("3", "YetAnotherIndustry"), "TestPosition", "2022-01-01");

        assertEquals("TestCompany", experience.getCompany());
        assertEquals("1", experience.getCompanyId());
        assertEquals("http://example.com", experience.getCompanyUrl());
        assertEquals("TestDescription", experience.getDescription());
        assertEquals("TestPosition", experience.getPosition());
        assertEquals("2022-01-01", experience.getStart());
        assertEquals("2024-01-01", experience.getEnd());

        assertEquals("TestIndustry", experience.getIndustries().get(0).getName());
    }
}
