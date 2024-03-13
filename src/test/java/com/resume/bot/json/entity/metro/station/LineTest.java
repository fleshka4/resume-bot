package com.resume.bot.json.entity.metro.station;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LineTest {
    @Test
    public void gettersAndSettersTest() {
        String hexColor = "#ff0000";
        String id = "1";
        String name = "Red Line";

        Line line = new Line();
        line.setHexColor(hexColor);
        line.setId(id);
        line.setName(name);

        assertEquals(line.getHexColor(), hexColor);
        assertEquals(line.getId(), id);
        assertEquals(line.getName(), name);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Line line = new Line();

        assertThrows(NullPointerException.class, () -> line.setHexColor(null));
        assertThrows(NullPointerException.class, () -> line.setId(null));
        assertThrows(NullPointerException.class, () -> line.setName(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String hexColor = "#00ff00";
        String id = "2";
        String name = "Green Line";

        Line line = new Line(hexColor, id, name);

        assertEquals(line.getHexColor(), hexColor);
        assertEquals(line.getId(), id);
        assertEquals(line.getName(), name);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Line(null, "1", "Red Line"));
        assertThrows(NullPointerException.class, () -> new Line("#ff0000", null, "Red Line"));
        assertThrows(NullPointerException.class, () -> new Line("#ff0000", "1", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Line line = new Line();

        assertNull(line.getHexColor());
        assertNull(line.getId());
        assertNull(line.getName());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Line line1 = new Line("#ff0000", "1", "Red Line");
        Line line2 = new Line("#ff0000", "1", "Red Line");
        Line line3 = new Line("#00ff00", "2", "Green Line");

        assertEquals(line1, line2);
        assertNotEquals(line1, line3);

        assertEquals(line1.hashCode(), line2.hashCode());
        assertNotEquals(line1.hashCode(), line3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Line line = new Line("#ff0000", "1", "Red Line");

        String expectedToString = "Line(hexColor=#ff0000, id=1, name=Red Line)";
        assertEquals(expectedToString, line.toString());
    }

    @Test
    public void createLineFromJsonTest() {
        String json = """
                {
                  "hex_color": "#0000ff",
                  "id": "3",
                  "name": "Blue Line"
                }
                """;

        Line line = JsonProcessor.createEntityFromJson(json, Line.class);

        assertEquals(line.getHexColor(), "#0000ff");
        assertEquals(line.getId(), "3");
        assertEquals(line.getName(), "Blue Line");
    }

    @Test
    public void createLineWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Line.class));
    }
}
