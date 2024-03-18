package com.resume.unit.bot.json.entity.metro;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.metro.Line;
import com.resume.bot.json.entity.metro.Metro;
import com.resume.bot.json.entity.metro.station.Station;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MetroTest {
    @Test
    public void gettersAndSettersTest() {
        String id = "1";
        List<Line> lines = List.of(new Line("1", "#ff0000", "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line")))));
        String name = "City Metro";
        String url = "https://example.com/metro";

        Metro metro = new Metro();
        metro.setId(id);
        metro.setLines(lines);
        metro.setName(name);
        metro.setUrl(url);

        assertEquals(metro.getId(), id);
        assertEquals(metro.getLines(), lines);
        assertEquals(metro.getName(), name);
        assertEquals(metro.getUrl(), url);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Metro metro = new Metro();

        assertThrows(NullPointerException.class, () -> metro.setId(null));
        assertThrows(NullPointerException.class, () -> metro.setLines(null));
        assertThrows(NullPointerException.class, () -> metro.setName(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String id = "2";
        List<Line> lines = List.of(new Line("2", "#00ff00", "Green Line", List.of(new Station("B", "Station B", 34.0522, -118.2437, 2L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line")))));
        String name = "City Subway";
        String url = "https://example.com/subway";

        Metro metro = new Metro(id, name, url, lines);

        assertEquals(metro.getId(), id);
        assertEquals(metro.getLines(), lines);
        assertEquals(metro.getName(), name);
        assertEquals(metro.getUrl(), url);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Metro(null, "City Metro", "https://example.com/metro", List.of(new Line("1", "#ff0000", "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line")))))));
        assertThrows(NullPointerException.class, () -> new Metro("1", null, "https://example.com/metro", List.of(new Line("1", "#ff0000", "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line")))))));
        assertThrows(NullPointerException.class, () -> new Metro("1", "City Metro", "https://example.com/metro", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Metro metro = new Metro();

        assertNull(metro.getId());
        assertNull(metro.getLines());
        assertNull(metro.getName());
        assertNull(metro.getUrl());
    }

    @Test
    public void equalsAndHashCodeTest() {
        List<Line> lines1 = List.of(new Line("#ff0000", "1", "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line")))));
        List<Line> lines2 = List.of(new Line("#ff0000", "1", "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line")))));
        List<Line> lines3 = List.of(new Line("#00ff00", "2", "Green Line", List.of(new Station("B", "Station B", 34.0522, -118.2437, 2L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line")))));

        Metro metro1 = new Metro("1", "City Metro", "https://example.com/metro", lines1);
        Metro metro2 = new Metro("1", "City Metro", "https://example.com/metro", lines2);
        Metro metro3 = new Metro("2", "City Subway", "https://example.com/subway", lines3);

        assertEquals(metro1, metro2);
        assertNotEquals(metro1, metro3);

        assertEquals(metro1.hashCode(), metro2.hashCode());
        assertNotEquals(metro1.hashCode(), metro3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        List<Line> lines = List.of(new Line("1", "#ff0000", "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line("#0000ff", "3", "Blue Line")))));
        Metro metro = new Metro("1", "City Metro", "https://example.com/metro", lines);

        String expectedToString = "Metro(id=1, name=City Metro, url=https://example.com/metro, lines=[Line(id=1, hexColor=#ff0000, name=Red Line, stations=[Station(id=A, name=Station A, lat=40.7128, lng=-74.006, order=1, line=Line(id=#0000ff, hexColor=3, name=Blue Line))])])";
        assertEquals(expectedToString, metro.toString());
    }

    @Test
    public void createMetroFromJsonTest() {
        String json = """
                {
                  "id": "1",
                  "lines": [
                    {
                      "hex_color": "#ff0000",
                      "id": "1",
                      "name": "Red Line",
                      "stations": [
                        {
                          "id": "A",
                          "lat": 40.7128,
                          "line": {
                            "hex_color": "#0000ff",
                            "id": "3",
                            "name": "Blue Line"
                          },
                          "lng": -74.0060,
                          "name": "Station A",
                          "order": 1
                        }
                      ]
                    }
                  ],
                  "name": "City Metro",
                  "url": "https://example.com/metro"
                }
                """;

        Metro metro = JsonProcessor.createEntityFromJson(json, Metro.class);

        assertEquals(metro.getId(), "1");
        assertEquals(metro.getLines(), List.of(new Line("1", "#ff0000", "Red Line", List.of(new Station("A", "Station A", 40.7128, -74.0060, 1L, new com.resume.bot.json.entity.metro.station.Line("3", "#0000ff", "Blue Line"))))));
        assertEquals(metro.getName(), "City Metro");
        assertEquals(metro.getUrl(), "https://example.com/metro");
    }

    @Test
    public void createMetroWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Metro.class));
    }
}
