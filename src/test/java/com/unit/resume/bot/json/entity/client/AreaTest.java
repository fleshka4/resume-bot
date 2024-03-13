package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Area;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AreaTest {
    @Test
    public void gettersAndSettersTest() {
        String id = "1";
        String name = "Software Development";

        Area area = new Area();
        area.setId(id);
        area.setName(name);

        assertEquals(area.getId(), id);
        assertEquals(area.getName(), name);
        assertEquals(area.getUrl(), "/areas/1");
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Area area = new Area();

        assertThrows(NullPointerException.class, () -> area.setId(null));
        assertThrows(NullPointerException.class, () -> area.setName(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String id = "2";
        String name = "Data Science";

        Area area = new Area(id, name);

        assertEquals(area.getId(), id);
        assertEquals(area.getName(), name);
        assertEquals(area.getUrl(), "/areas/2");
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Area(null, "Software Development"));
        assertThrows(NullPointerException.class, () -> new Area("3", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Area area = new Area();

        assertNull(area.getId());
        assertNull(area.getName());
        assertEquals(area.getUrl(), "/areas/null");
    }

    @Test
    public void equalsAndHashCodeTest() {
        Area area1 = new Area("1", "Software Development");
        Area area2 = new Area("1", "Software Development");
        Area area3 = new Area("2", "Data Science");

        assertEquals(area1, area2);
        assertNotEquals(area1, area3);

        assertEquals(area1.hashCode(), area2.hashCode());
        assertNotEquals(area1.hashCode(), area3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Area area = new Area("1", "Software Development");

        String expectedToString = "Area(id=1, name=Software Development, url=/areas/1)";
        assertEquals(expectedToString, area.toString());
    }

    @Test
    public void createAreaFromJsonTest() {
        String json = """
                {
                  "id": "3",
                  "name": "Project Management"
                }
                """;

        Area area = JsonProcessor.createEntityFromJson(json, Area.class);

        assertEquals(area.getId(), "3");
        assertEquals(area.getName(), "Project Management");
        assertEquals(area.getUrl(), "/areas/3");
    }

    @Test
    public void createAreaWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Area.class));
    }
}
