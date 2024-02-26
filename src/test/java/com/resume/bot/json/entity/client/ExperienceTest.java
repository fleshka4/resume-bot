package com.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExperienceTest {
    @Test
    public void gettersAndSettersTest() {
        Experience experience = new Experience();

        assertDoesNotThrow(() -> experience.setArea(null));
        assertDoesNotThrow(() -> experience.setCompany(null));
        assertDoesNotThrow(() -> experience.setCompanyId(null));
        assertDoesNotThrow(() -> experience.setCompanyUrl(null));
        assertDoesNotThrow(() -> experience.setDescription(null));
        assertDoesNotThrow(() -> experience.setEmployer(null));
        assertDoesNotThrow(() -> experience.setEnd(null));
        assertThrows(NullPointerException.class, () -> experience.setIndustries(null));
        assertDoesNotThrow(() -> experience.setIndustry(null));
        assertDoesNotThrow(() -> experience.setPosition(null));
        assertThrows(NullPointerException.class, () -> experience.setStart(null));
    }

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
}
