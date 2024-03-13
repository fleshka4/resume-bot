package com.resume.unit.bot.json.entity.client.education;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.education.ElementaryEducation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ElementaryEducationTest {
    private static String name;
    private static long year;
    @BeforeAll
    public static void setUp() {
        name = "example";
        year = 2020;
    }

    @Test
    public void correctFullFill() {
        ElementaryEducation entity = JsonProcessor.createEntityFromJson("""
                {
                    "name": "%s",
                    "year": %d
                }
                """.formatted(name, year), ElementaryEducation.class);
        assertEquals(entity.getClass(), ElementaryEducation.class);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getYear(), year);
    }

    @Test
    public void onlyNameFill() {
        ElementaryEducation entity = JsonProcessor.createEntityFromJson("""
                {
                    "name": "%s"
                }
                """.formatted(name), ElementaryEducation.class);
        assertEquals(entity.getClass(), ElementaryEducation.class);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getYear(), 0);
    }

    @Test
    public void invalidFill() {
        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                    "year": %d
                }
                """.formatted(year), ElementaryEducation.class));
    }

    @Test
    public void setNameNull() {
        assertThrows(NullPointerException.class, () -> new ElementaryEducation(null, year));
        ElementaryEducation education = new ElementaryEducation(name, 2022);
        assertThrows(NullPointerException.class, () -> education.setName(null));
    }

    @Test
    public void toStringMethod() {
        ElementaryEducation education = new ElementaryEducation(name, year);
        String expectedToString = "ElementaryEducation(name=%s, year=%d)".formatted(name, year);
        assertEquals(expectedToString, education.toString());
    }

    @Test
    public void equalsAndHashCode() {
        ElementaryEducation education1 = new ElementaryEducation("name", 2022);
        ElementaryEducation education2 = new ElementaryEducation("name", 2022);
        ElementaryEducation education3 = new ElementaryEducation("differentName", 2023);

        assertEquals(education1, education2);
        assertNotEquals(education1, education3);
        assertEquals(education1.hashCode(), education2.hashCode());
        assertNotEquals(education1.hashCode(), education3.hashCode());
    }

    @Test
    public void setName() {
        ElementaryEducation education = new ElementaryEducation("oldName", 2022);
        education.setName("newName");
        assertEquals("newName", education.getName());
    }

    @Test
    public void setYear() {
        ElementaryEducation education = new ElementaryEducation("name", 2022);
        education.setYear(2023);
        assertEquals(2023, education.getYear());
    }
}
