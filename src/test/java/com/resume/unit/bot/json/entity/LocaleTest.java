package com.resume.unit.bot.json.entity;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Locale;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocaleTest {
    @Test
    public void gettersAndSettersTest() {
        Boolean current = true;
        String id = "en_US";
        String name = "English (US)";

        Locale locale = new Locale();
        locale.setCurrent(current);
        locale.setId(id);
        locale.setName(name);

        assertEquals(locale.getCurrent(), current);
        assertEquals(locale.getId(), id);
        assertEquals(locale.getName(), name);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Locale locale = new Locale();

        assertThrows(NullPointerException.class, () -> locale.setCurrent(null));
        assertThrows(NullPointerException.class, () -> locale.setId(null));
        assertThrows(NullPointerException.class, () -> locale.setName(null));
    }

    @Test
    public void constructorWithArgsTest() {
        Boolean current = false;
        String id = "fr_FR";
        String name = "French (France)";

        Locale locale = new Locale(current, id, name);

        assertEquals(locale.getCurrent(), current);
        assertEquals(locale.getId(), id);
        assertEquals(locale.getName(), name);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Locale(null, "en_US", "English (US)"));
        assertThrows(NullPointerException.class, () -> new Locale(true, null, "English (US)"));
        assertThrows(NullPointerException.class, () -> new Locale(true, "en_US", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Locale locale = new Locale();

        assertNull(locale.getCurrent());
        assertNull(locale.getId());
        assertNull(locale.getName());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Locale locale1 = new Locale(true, "en_US", "English (US)");
        Locale locale2 = new Locale(true, "en_US", "English (US)");
        Locale locale3 = new Locale(false, "fr_FR", "French (France)");

        
        assertEquals(locale1, locale2);
        assertNotEquals(locale1, locale3);

        
        assertEquals(locale1.hashCode(), locale2.hashCode());
        assertNotEquals(locale1.hashCode(), locale3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Locale locale = new Locale(true, "en_US", "English (US)");

        String expectedToString = "Locale(current=true, id=en_US, name=English (US))";
        assertEquals(expectedToString, locale.toString());
    }

    @Test
    public void createLocaleFromJsonTest() {
        String json = """
                {
                  "current": true,
                  "id": "en_US",
                  "name": "English (US)"
                }
                """;

        Locale locale = JsonProcessor.createEntityFromJson(json, Locale.class);

        assertEquals(locale.getCurrent(), true);
        assertEquals(locale.getId(), "en_US");
        assertEquals(locale.getName(), "English (US)");
    }

    @Test
    public void createLocaleWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Locale.class));
    }
}
