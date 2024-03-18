package com.resume.bot.json.entity.area;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AreaTest {
    @Test
    public void fullAreaFill() {
        String id = "113";
        String parentId = null;
        String name = "Россия";
        List<Area> areas = null;

        Area entity = JsonProcessor.createEntityFromJson("""
                {
                  "id": "%s",
                  "parent_id": "%s",
                  "name": "%s",
                  "areas": %s
                }""".formatted(id, parentId, name, areas), Area.class);

        assertEquals(entity.getClass(), Area.class);
        assertEquals(entity.getId(), id);
        assertEquals(entity.getParentId(), "null");
        assertEquals(entity.getName(), name);
        assertEquals(entity.getAreas(), areas);
    }

    @Test
    public void invalidAreaFill() {
        String parentId = null;
        String name = "Россия";
        List<Area> areas = null;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                  "parent_id": "%s",
                  "name": "%s",
                  "areas": %s
                }""".formatted(parentId, name, areas), Area.class));
    }

    @Test
    public void gettersAndSettersTest() {
        String id = "113";
        String parentId = "1";
        String name = "Россия";
        List<Area> areas = List.of(new Area("1", null, "Area1", null));

        Area entity = new Area();
        entity.setId(id);
        entity.setParentId(parentId);
        entity.setName(name);
        entity.setAreas(areas);

        assertEquals(entity.getId(), id);
        assertEquals(entity.getParentId(), parentId);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getAreas(), areas);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Area entity = new Area();

        assertThrows(NullPointerException.class, () -> entity.setId(null));
        assertThrows(NullPointerException.class, () -> entity.setName(null));
    }

    @Test
    public void constructorWithNullArgumentsTest() {
        assertThrows(NullPointerException.class, () -> new Area(null, null, "Name", null));
        assertThrows(NullPointerException.class, () -> new Area("1", null, null, null));
    }

    @Test
    public void equalsAndHashCodeTest() {
        Area area1 = new Area("1", "parent1", "Area1", null);
        Area area2 = new Area("1", "parent1", "Area1", null);
        Area area3 = new Area("2", "parent2", "Area2", null);

        
        assertEquals(area1, area2);
        assertNotEquals(area1, area3);

        
        assertEquals(area1.hashCode(), area2.hashCode());
        assertNotEquals(area1.hashCode(), area3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        String id = "113";
        String parentId = "1";
        String name = "Россия";
        List<Area> areas = List.of(new Area("1", null, "Area1", null));

        Area entity = new Area(id, parentId, name, areas);

        String expectedToString = "Area(id=113, parentId=1, name=Россия, areas=" + areas + ')';
        assertEquals(expectedToString, entity.toString());
    }
}
