package com.resume.unit.bot.json.entity;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.Industry;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IndustryTest {
    @Test
    public void gettersAndSettersTest() {
        String id = "1";
        List<Type> industries = List.of(new Type("1", "Technology"));
        String name = "IT Industry";

        Industry industry = new Industry();
        industry.setId(id);
        industry.setIndustries(industries);
        industry.setName(name);

        assertEquals(industry.getId(), id);
        assertEquals(industry.getIndustries(), industries);
        assertEquals(industry.getName(), name);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Industry industry = new Industry();

        assertThrows(NullPointerException.class, () -> industry.setId(null));
        assertThrows(NullPointerException.class, () -> industry.setIndustries(null));
        assertThrows(NullPointerException.class, () -> industry.setName(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String id = "2";
        List<Type> industries = List.of(new Type("2", "Finance"));
        String name = "Finance Sector";

        Industry industry = new Industry(id, name, industries);

        assertEquals(industry.getId(), id);
        assertEquals(industry.getIndustries(), industries);
        assertEquals(industry.getName(), name);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Industry(null, "IT Industry", List.of(new Type("1", "Technology"))));
        assertThrows(NullPointerException.class, () -> new Industry("1", "IT Industry", null));
        assertThrows(NullPointerException.class, () -> new Industry("1", null, List.of(new Type("1", "Technology"))));
    }

    @Test
    public void noArgsConstructorTest() {
        Industry industry = new Industry();

        assertNull(industry.getId());
        assertNull(industry.getIndustries());
        assertNull(industry.getName());
    }

    @Test
    public void equalsAndHashCodeTest() {
        List<Type> industries1 = List.of(new Type("1", "Technology"));
        List<Type> industries2 = List.of(new Type("1", "Technology"));
        List<Type> industries3 = List.of(new Type("2", "Finance"));

        Industry industry1 = new Industry("1", "IT Industry", industries1);
        Industry industry2 = new Industry("1", "IT Industry", industries2);
        Industry industry3 = new Industry("2", "Finance Sector", industries3);

        assertEquals(industry1, industry2);
        assertNotEquals(industry1, industry3);

        assertEquals(industry1.hashCode(), industry2.hashCode());
        assertNotEquals(industry1.hashCode(), industry3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        List<Type> industries = List.of(new Type("1", "Technology"));
        Industry industry = new Industry("1", "IT Industry", industries);

        String expectedToString = "Industry(id=1, name=IT Industry, industries=[Type(id=1, name=Technology)])";
        assertEquals(expectedToString, industry.toString());
    }

    @Test
    public void createIndustryFromJsonTest() {
        String json = """
                {
                  "id": "1",
                  "industries": [
                    {
                      "id": "1",
                      "name": "Technology"
                    }
                  ],
                  "name": "IT Industry"
                }
                """;

        Industry industry = JsonProcessor.createEntityFromJson(json, Industry.class);

        assertEquals(industry.getId(), "1");
        assertEquals(industry.getIndustries(), List.of(new Type("1", "Technology")));
        assertEquals(industry.getName(), "IT Industry");
    }

    @Test
    public void createIndustryWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Industry.class));
    }
}
