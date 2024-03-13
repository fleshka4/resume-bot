package com.unit.resume.bot.json.entity.common;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.common.Id;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdTest {
    @Test
    public void gettersAndSettersTest() {
        String idValue = "12345";

        Id id = new Id();
        id.setId(idValue);

        assertEquals(id.getId(), idValue);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Id id = new Id();

        assertThrows(NullPointerException.class, () -> id.setId(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String idValue = "67890";

        Id id = new Id(idValue);

        assertEquals(id.getId(), idValue);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Id(null));
    }

    @Test
    public void noArgsConstructorTest() {
        Id id = new Id();

        assertNull(id.getId());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Id id1 = new Id("12345");
        Id id2 = new Id("12345");
        Id id3 = new Id("67890");

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);

        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        String idValue = "12345";

        Id id = new Id(idValue);

        String expectedToString = "Id(id=12345)";
        assertEquals(expectedToString, id.toString());
    }

    @Test
    public void createIdFromJsonTest() {
        String json = """
                {
                  "id": "12345"
                }
                """;

        Id id = JsonProcessor.createEntityFromJson(json, Id.class);

        assertEquals(id.getId(), "12345");
    }

    @Test
    public void createIdWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Id.class));
    }
}
