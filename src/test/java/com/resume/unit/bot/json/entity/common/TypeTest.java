package com.resume.unit.bot.json.entity.common;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TypeTest {
    @Test
    public void gettersAndSettersTest() {
        String id = "1";
        String name = "ExampleType";

        Type type = new Type();
        type.setId(id);
        type.setName(name);

        assertEquals(type.getId(), id);
        assertEquals(type.getName(), name);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Type type = new Type();

        assertThrows(NullPointerException.class, () -> type.setId(null));
        assertThrows(NullPointerException.class, () -> type.setName(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String id = "2";
        String name = "AnotherType";

        Type type = new Type(id, name);

        assertEquals(type.getId(), id);
        assertEquals(type.getName(), name);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Type(null, "ExampleType"));
        assertThrows(NullPointerException.class, () -> new Type("1", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Type type = new Type();

        assertNull(type.getId());
        assertNull(type.getName());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Type type1 = new Type("1", "ExampleType");
        Type type2 = new Type("1", "ExampleType");
        Type type3 = new Type("2", "AnotherType");

        assertEquals(type1, type2);
        assertNotEquals(type1, type3);

        assertEquals(type1.hashCode(), type2.hashCode());
        assertNotEquals(type1.hashCode(), type3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Type type = new Type("1", "ExampleType");

        String expectedToString = "Type(id=1, name=ExampleType)";
        assertEquals(expectedToString, type.toString());
    }

    @Test
    public void createTypeFromJsonTest() {
        String json = """
                {
                  "id": "1",
                  "name": "ExampleType"
                }
                """;

        Type type = JsonProcessor.createEntityFromJson(json, Type.class);

        assertEquals(type.getId(), "1");
        assertEquals(type.getName(), "ExampleType");
    }

    @Test
    public void createTypeWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Type.class));
    }
}
