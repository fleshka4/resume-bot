package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Language;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LanguageTest {
    @Test
    public void createLanguageWithNonNullFieldsTest() {
        String id = "1";
        String name = "English";
        Type level = new Type("2", "Intermediate");

        Language language = new Language(id, name, level);

        assertNotNull(language.getId());
        assertEquals(language.getId(), id);

        assertNotNull(language.getName());
        assertEquals(language.getName(), name);

        assertNotNull(language.getLevel());
        assertEquals(language.getLevel(), level);
    }

    @Test
    public void createLanguageWithNullNonNullFieldsTest() {
        assertThrows(NullPointerException.class, () -> new Language(null, null, null));
    }

    @Test
    public void createLanguageWithJsonTest() {
        String json = """
                {
                  "id": "1",
                  "name": "English",
                  "level": {"id": "2", "name": "Intermediate"}
                }
                """;

        Language language = JsonProcessor.createEntityFromJson(json, Language.class);

        assertNotNull(language.getId());
        assertEquals(language.getId(), "1");

        assertNotNull(language.getName());
        assertEquals(language.getName(), "English");

        assertNotNull(language.getLevel());
        assertEquals(language.getLevel().getId(), "2");
        assertEquals(language.getLevel().getName(), "Intermediate");
    }

    @Test
    public void equalsAndHashCodeTest() {
        Language language1 = new Language("1", "English", new Type("2", "Intermediate"));
        Language language2 = new Language("1", "English", new Type("2", "Intermediate"));
        Language language3 = new Language("2", "Spanish", new Type("3", "Advanced"));

        assertEquals(language1, language2);
        assertNotEquals(language1, language3);
        assertEquals(language1.hashCode(), language2.hashCode());
        assertNotEquals(language1.hashCode(), language3.hashCode());
    }

    @Test
    public void toStringTest() {
        Language language = new Language("1", "English", new Type("2", "Intermediate"));
        String expected = "Language(id=1, name=English, level=Type(id=2, name=Intermediate))";

        assertEquals(expected, language.toString());
    }

    @Test
    public void noArgsConstructorTest() {
        Language language = new Language();

        assertNull(language.getId());
        assertNull(language.getName());
        assertNull(language.getLevel());
    }

    @Test
    public void allArgsConstructorTest() {
        String id = "1";
        String name = "English";
        Type level = new Type("2", "Intermediate");

        Language language = new Language(id, name, level);

        assertEquals(id, language.getId());
        assertEquals(name, language.getName());
        assertEquals(level, language.getLevel());
    }
}
