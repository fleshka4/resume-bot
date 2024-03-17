package com.resume.unit.bot.json.entity.metro;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.metro.Line;
import com.resume.bot.json.entity.metro.station.Station;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LineTest {
    @Test
    public void gettersAndSettersTest() {
        String hexColor = "#ff0000";
        String id = "1";
        String name = "Red Line";
        List<Station> stations = List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line(id, hexColor, name)));

        Line line = new Line();
        line.setHexColor(hexColor);
        line.setId(id);
        line.setName(name);
        line.setStations(stations);

        assertEquals(line.getHexColor(), hexColor);
        assertEquals(line.getId(), id);
        assertEquals(line.getName(), name);
        assertEquals(line.getStations(), stations);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Line line = new Line();

        assertThrows(NullPointerException.class, () -> line.setHexColor(null));
        assertThrows(NullPointerException.class, () -> line.setId(null));
        assertThrows(NullPointerException.class, () -> line.setName(null));
        assertThrows(NullPointerException.class, () -> line.setStations(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String hexColor = "#00ff00";
        String id = "2";
        String name = "Green Line";
        List<Station> stations = List.of(new Station("B", "Station B", 34.0522, -118.243, 2L, new com.resume.bot.json.entity.metro.station.Line(id, hexColor, name)));

        Line line = new Line(id, hexColor, name, stations);

        assertEquals(line.getHexColor(), hexColor);
        assertEquals(line.getId(), id);
        assertEquals(line.getName(), name);
        assertEquals(line.getStations(), stations);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Line("1", null, "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, null))));
        assertThrows(NullPointerException.class, () -> new Line(null, "#ff0000", "Red Line", List.of(new Station("A", "Station A", 40.712, -74.0060, 1L, null))));
        assertThrows(NullPointerException.class, () -> new Line("1", "#ff0000", null, List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, null))));
        assertThrows(NullPointerException.class, () -> new Line("1", "#ff0000", "Red Line", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Line line = new Line();

        assertNull(line.getHexColor());
        assertNull(line.getId());
        assertNull(line.getName());
        assertNull(line.getStations());
    }

    @Test
    public void equalsAndHashCodeTest() {
        String hexColor = "#00ff00";
        String id = "2";
        String name = "Green Line";
        List<Station> stations1 = List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line(id, hexColor, name)));
        List<Station> stations2 = List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line(id, hexColor, name)));
        List<Station> stations3 = List.of(new Station("B", "Station B", 34.0522, -118.2437, 2L, new com.resume.bot.json.entity.metro.station.Line(id, hexColor, name)));

        Line line1 = new Line("1", "#ff0000", "Red Line", stations1);
        Line line2 = new Line("1", "#ff0000", "Red Line", stations2);
        Line line3 = new Line("2", "#00ff00", "Green Line", stations3);

        assertEquals(line1, line2);
        assertNotEquals(line1, line3);

        assertEquals(line1.hashCode(), line2.hashCode());
        assertNotEquals(line1.hashCode(), line3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        String hexColor = "#00ff00";
        String id = "2";
        String name = "Green Line";
        List<Station> stations = List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line(id, hexColor, name)));
        Line line = new Line("1", "#ff0000", "Red Line", stations);

        String expectedToString = "Line(id=1, hexColor=#ff0000, name=Red Line, stations=[Station(id=A, lat=40.7128, line=Line(id=2, hexColor=#00ff00, name=Green Line), lng=-74.006, name=Station A, order=1)])";
        assertEquals(expectedToString, line.toString());
    }

    @Test
    public void createLineFromJsonTest() {
        String json = """
                {
                  "id": "3",
                  "hex_color": "#0000ff",
                  "name": "Blue Line",
                  "stations": [
                    {
                      "id": "C",
                      "name": "Station C",
                      "lat": 34.0522,
                      "lng": -118.2437,
                      "order": 3,
                      "line": {
                        "id": "3",
                        "hex_color": "#0000ff",
                        "name": "Blue Line"
                      }
                    }
                  ]
                }
                """;

        Line line = JsonProcessor.createEntityFromJson(json, Line.class);

        assertEquals(line.getHexColor(), "#0000ff");
        assertEquals(line.getId(), "3");
        assertEquals(line.getName(), "Blue Line");
        assertEquals(line.getStations(), List.of(new Station("C", "Station C", 34.0522, -118.243, 3L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line"))));
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