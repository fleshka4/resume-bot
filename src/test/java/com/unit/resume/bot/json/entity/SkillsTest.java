package com.unit.resume.bot.json.entity;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Skills;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SkillsTest {
    @Test
    public void gettersAndSettersTest() {
        List<Type> items = Arrays.asList(new Type("1", "Java"), new Type("2", "Python"));

        Skills skills = new Skills();
        skills.setItems(items);

        assertEquals(skills.getItems(), items);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Skills skills = new Skills();

        assertThrows(NullPointerException.class, () -> skills.setItems(null));
    }

    @Test
    public void constructorWithArgsTest() {
        List<Type> items = Arrays.asList(new Type("1", "Java"), new Type("2", "Python"));

        Skills skills = new Skills(items);

        assertEquals(skills.getItems(), items);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Skills(null));
    }

    @Test
    public void noArgsConstructorTest() {
        Skills skills = new Skills();

        assertNull(skills.getItems());
    }

    @Test
    public void equalsAndHashCodeTest() {
        List<Type> items1 = Arrays.asList(new Type("1", "Java"), new Type("2", "Python"));
        List<Type> items2 = Arrays.asList(new Type("1", "Java"), new Type("2", "Python"));
        List<Type> items3 = Arrays.asList(new Type("3", "JavaScript"));

        Skills skills1 = new Skills(items1);
        Skills skills2 = new Skills(items2);
        Skills skills3 = new Skills(items3);

        
        assertEquals(skills1, skills2);
        assertNotEquals(skills1, skills3);

        
        assertEquals(skills1.hashCode(), skills2.hashCode());
        assertNotEquals(skills1.hashCode(), skills3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        List<Type> items = Arrays.asList(new Type("1", "Java"), new Type("2", "Python"));
        Skills skills = new Skills(items);

        String expectedToString = "Skills(items=[Type(id=1, name=Java), Type(id=2, name=Python)])";
        assertEquals(expectedToString, skills.toString());
    }

    @Test
    public void createSkillsFromJsonTest() {
        String json = """
                {
                  "items": [
                    {
                      "id": "1",
                      "name": "Java"
                    },
                    {
                      "id": "2",
                      "name": "Python"
                    }
                  ]
                }
                """;

        Skills skills = JsonProcessor.createEntityFromJson(json, Skills.class);

        List<Type> expectedItems = Arrays.asList(new Type("1", "Java"), new Type("2", "Python"));

        assertEquals(skills.getItems(), expectedItems);
    }

    @Test
    public void createSkillsWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Skills.class));
    }
}
