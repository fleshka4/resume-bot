package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.LogoUrls;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogoUrlsTest {
    @Test
    public void gettersAndSettersTest() {
        String ninety = "https://example.com/logo_90.png";
        String twoForty = "https://example.com/logo_240.png";
        String original = "https://example.com/logo_original.png";

        LogoUrls logoUrls = new LogoUrls();
        logoUrls.setNinety(ninety);
        logoUrls.setTwoForty(twoForty);
        logoUrls.setOriginal(original);

        assertEquals(logoUrls.getNinety(), ninety);
        assertEquals(logoUrls.getTwoForty(), twoForty);
        assertEquals(logoUrls.getOriginal(), original);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        LogoUrls logoUrls = new LogoUrls();

        assertDoesNotThrow(() -> logoUrls.setNinety(null));
        assertDoesNotThrow(() -> logoUrls.setTwoForty(null));
        assertDoesNotThrow(() -> logoUrls.setOriginal(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String ninety = "https://example.com/new_logo_90.png";
        String twoForty = "https://example.com/new_logo_240.png";
        String original = "https://example.com/new_logo_original.png";

        LogoUrls logoUrls = new LogoUrls(ninety, twoForty, original);

        assertEquals(logoUrls.getNinety(), ninety);
        assertEquals(logoUrls.getTwoForty(), twoForty);
        assertEquals(logoUrls.getOriginal(), original);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertDoesNotThrow(() -> new LogoUrls(null, null, null));
    }

    @Test
    public void noArgsConstructorTest() {
        LogoUrls logoUrls = new LogoUrls();

        assertNull(logoUrls.getNinety());
        assertNull(logoUrls.getTwoForty());
        assertNull(logoUrls.getOriginal());
    }

    @Test
    public void equalsAndHashCodeTest() {
        LogoUrls logoUrls1 = new LogoUrls("https://example.com/logo_90.png", "https://example.com/logo_240.png", "https://example.com/logo_original.png");
        LogoUrls logoUrls2 = new LogoUrls("https://example.com/logo_90.png", "https://example.com/logo_240.png", "https://example.com/logo_original.png");
        LogoUrls logoUrls3 = new LogoUrls("https://example.com/new_logo_90.png", "https://example.com/new_logo_240.png", "https://example.com/new_logo_original.png");

        assertEquals(logoUrls1, logoUrls2);
        assertNotEquals(logoUrls1, logoUrls3);

        assertEquals(logoUrls1.hashCode(), logoUrls2.hashCode());
        assertNotEquals(logoUrls1.hashCode(), logoUrls3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        LogoUrls logoUrls = new LogoUrls("https://example.com/logo_90.png", "https://example.com/logo_240.png", "https://example.com/logo_original.png");

        String expectedToString = "LogoUrls(ninety=https://example.com/logo_90.png, twoForty=https://example.com/logo_240.png, original=https://example.com/logo_original.png)";
        assertEquals(expectedToString, logoUrls.toString());
    }

    @Test
    public void createLogoUrlsFromJsonTest() {
        String json = """
                {
                  "90": "https://example.com/new_logo_90.png",
                  "240": "https://example.com/new_logo_240.png",
                  "original": "https://example.com/new_logo_original.png"
                }
                """;

        LogoUrls logoUrls = JsonProcessor.createEntityFromJson(json, LogoUrls.class);

        assertEquals(logoUrls.getNinety(), "https://example.com/new_logo_90.png");
        assertEquals(logoUrls.getTwoForty(), "https://example.com/new_logo_240.png");
        assertEquals(logoUrls.getOriginal(), "https://example.com/new_logo_original.png");
    }

    @Test
    public void createLogoUrlsWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, LogoUrls.class));
    }
}
