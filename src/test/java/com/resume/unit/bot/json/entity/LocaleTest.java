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

        Locale locale = new Locale(id, name, current);

        assertEquals(locale.getCurrent(), current);
        assertEquals(locale.getId(), id);
        assertEquals(locale.getName(), name);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Locale("en_US", "English (US)", null));
        assertThrows(NullPointerException.class, () -> new Locale(null, "English (US)", true));
        assertThrows(NullPointerException.class, () -> new Locale("en_US", null, true));
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
        Locale locale1 = new Locale("en_US", "English (US)", true);
        Locale locale2 = new Locale("en_US", "English (US)", true);
        Locale locale3 = new Locale("fr_FR", "French (France)", false);


        assertEquals(locale1, locale2);
        assertNotEquals(locale1, locale3);


        assertEquals(locale1.hashCode(), locale2.hashCode());
        assertNotEquals(locale1.hashCode(), locale3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Locale locale = new Locale("en_US", "English (US)", true);

        String expectedToString = "Locale(id=en_US, name=English (US), current=true)";
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
