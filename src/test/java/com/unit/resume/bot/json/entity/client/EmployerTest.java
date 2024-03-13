package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Employer;
import com.resume.bot.json.entity.client.LogoUrls;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmployerTest {
    @Test
    public void gettersAndSettersTest() {
        String alternateUrl = "https://example.com/alternate";
        String id = "123";
        LogoUrls logoUrls = new LogoUrls("https://example.com/logo_90.png", "https://example.com/logo_240.png", "https://example.com/logo_original.png");
        String name = "Example Employer";
        String url = "https://example.com/employer";

        Employer employer = new Employer();
        employer.setAlternateUrl(alternateUrl);
        employer.setId(id);
        employer.setLogoUrls(logoUrls);
        employer.setName(name);
        employer.setUrl(url);

        assertEquals(employer.getAlternateUrl(), alternateUrl);
        assertEquals(employer.getId(), id);
        assertEquals(employer.getLogoUrls(), logoUrls);
        assertEquals(employer.getName(), name);
        assertEquals(employer.getUrl(), url);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Employer employer = new Employer();

        assertThrows(NullPointerException.class, () -> employer.setAlternateUrl(null));
        assertThrows(NullPointerException.class, () -> employer.setId(null));
        assertDoesNotThrow(() -> employer.setLogoUrls(null));
        assertThrows(NullPointerException.class, () -> employer.setName(null));
        assertThrows(NullPointerException.class, () -> employer.setUrl(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String alternateUrl = "https://example.com/new_alternate";
        String id = "456";
        LogoUrls logoUrls = new LogoUrls("https://example.com/new_logo_90.png", "https://example.com/new_logo_240.png", "https://example.com/new_logo_original.png");
        String name = "New Employer";
        String url = "https://example.com/new_employer";

        Employer employer = new Employer(alternateUrl, id, logoUrls, name, url);

        assertEquals(employer.getAlternateUrl(), alternateUrl);
        assertEquals(employer.getId(), id);
        assertEquals(employer.getLogoUrls(), logoUrls);
        assertEquals(employer.getName(), name);
        assertEquals(employer.getUrl(), url);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Employer(null, null, null, null, null));
    }

    @Test
    public void noArgsConstructorTest() {
        Employer employer = new Employer();

        assertNull(employer.getAlternateUrl());
        assertNull(employer.getId());
        assertNull(employer.getLogoUrls());
        assertNull(employer.getName());
        assertNull(employer.getUrl());
    }

    @Test
    public void equalsAndHashCodeTest() {
        LogoUrls logoUrls1 = new LogoUrls("https://example.com/logo_90.png", "https://example.com/logo_240.png", "https://example.com/logo_original.png");
        LogoUrls logoUrls2 = new LogoUrls("https://example.com/logo_90.png", "https://example.com/logo_240.png", "https://example.com/logo_original.png");
        LogoUrls logoUrls3 = new LogoUrls("https://example.com/new_logo_90.png", "https://example.com/new_logo_240.png", "https://example.com/new_logo_original.png");

        Employer employer1 = new Employer("https://example.com/alternate", "123", logoUrls1, "Example Employer", "https://example.com/employer");
        Employer employer2 = new Employer("https://example.com/alternate", "123", logoUrls2, "Example Employer", "https://example.com/employer");
        Employer employer3 = new Employer("https://example.com/new_alternate", "456", logoUrls3, "New Employer", "https://example.com/new_employer");

        assertEquals(employer1, employer2);
        assertNotEquals(employer1, employer3);

        assertEquals(employer1.hashCode(), employer2.hashCode());
        assertNotEquals(employer1.hashCode(), employer3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        LogoUrls logoUrls = new LogoUrls("https://example.com/logo_90.png", "https://example.com/logo_240.png", "https://example.com/logo_original.png");
        Employer employer = new Employer("https://example.com/alternate", "123", logoUrls, "Example Employer", "https://example.com/employer");

        String expectedToString = "Employer(alternateUrl=https://example.com/alternate, id=123, logoUrls=LogoUrls(ninety=https://example.com/logo_90.png, twoForty=https://example.com/logo_240.png, original=https://example.com/logo_original.png), name=Example Employer, url=https://example.com/employer)";
        assertEquals(expectedToString, employer.toString());
    }

    @Test
    public void createEmployerFromJsonTest() {
        String json = """
                {
                  "alternate_url": "https://example.com/new_alternate",
                  "id": "456",
                  "logo_urls": {
                    "90": "https://example.com/new_logo_90.png",
                    "240": "https://example.com/new_logo_240.png",
                    "original": "https://example.com/new_logo_original.png"
                  },
                  "name": "New Employer",
                  "url": "https://example.com/new_employer"
                }
                """;

        Employer employer = JsonProcessor.createEntityFromJson(json, Employer.class);

        assertEquals(employer.getAlternateUrl(), "https://example.com/new_alternate");
        assertEquals(employer.getId(), "456");
        assertNotNull(employer.getLogoUrls());
        assertEquals(employer.getLogoUrls().getNinety(), "https://example.com/new_logo_90.png");
        assertEquals(employer.getLogoUrls().getTwoForty(), "https://example.com/new_logo_240.png");
        assertEquals(employer.getLogoUrls().getOriginal(), "https://example.com/new_logo_original.png");
        assertEquals(employer.getName(), "New Employer");
        assertEquals(employer.getUrl(), "https://example.com/new_employer");
    }

    @Test
    public void createEmployerWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Employer.class));
    }
}
