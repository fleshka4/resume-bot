package com.resume.bot.json.entity.metro.station;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StationTest {
    @Test
    public void gettersAndSettersTest() {
        String id = "1";
        Double lat = 40.7128;
        Line line = new Line("#ff0000", "1", "Red Line");
        Double lng = -74.0060;
        String name = "Station A";
        Long order = 1L;

        Station station = new Station();
        station.setId(id);
        station.setLat(lat);
        station.setLine(line);
        station.setLng(lng);
        station.setName(name);
        station.setOrder(order);

        assertEquals(station.getId(), id);
        assertEquals(station.getLat(), lat);
        assertEquals(station.getLine(), line);
        assertEquals(station.getLng(), lng);
        assertEquals(station.getName(), name);
        assertEquals(station.getOrder(), order);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Station station = new Station();

        assertThrows(NullPointerException.class, () -> station.setId(null));
        assertThrows(NullPointerException.class, () -> station.setLat(null));
        assertThrows(NullPointerException.class, () -> station.setLine(null));
        assertThrows(NullPointerException.class, () -> station.setLng(null));
        assertThrows(NullPointerException.class, () -> station.setName(null));
        assertThrows(NullPointerException.class, () -> station.setOrder(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String id = "2";
        Double lat = 34.0522;
        Line line = new Line("#00ff00", "2", "Green Line");
        Double lng = -118.2437;
        String name = "Station B";
        Long order = 2L;

        Station station = new Station(id, lat, line, lng, name, order);

        assertEquals(station.getId(), id);
        assertEquals(station.getLat(), lat);
        assertEquals(station.getLine(), line);
        assertEquals(station.getLng(), lng);
        assertEquals(station.getName(), name);
        assertEquals(station.getOrder(), order);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Station(null, 40.7128, new Line("#ff0000", "1", "Red Line"), -74.0060, "Station A", 1L));
        assertThrows(NullPointerException.class, () -> new Station("1", null, new Line("#ff0000", "1", "Red Line"), -74.0060, "Station A", 1L));
        assertThrows(NullPointerException.class, () -> new Station("1", 40.7128, null, -74.0060, "Station A", 1L));
        assertThrows(NullPointerException.class, () -> new Station("1", 40.7128, new Line("#ff0000", "1", "Red Line"), null, "Station A", 1L));
        assertThrows(NullPointerException.class, () -> new Station("1", 40.7128, new Line("#ff0000", "1", "Red Line"), -74.0060, null, 1L));
        assertThrows(NullPointerException.class, () -> new Station("1", 40.7128, new Line("#ff0000", "1", "Red Line"), -74.0060, "Station A", null));
    }

    @Test
    public void noArgsConstructorTest() {
        Station station = new Station();

        assertNull(station.getId());
        assertNull(station.getLat());
        assertNull(station.getLine());
        assertNull(station.getLng());
        assertNull(station.getName());
        assertNull(station.getOrder());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Line line1 = new Line("#ff0000", "1", "Red Line");
        Line line2 = new Line("#ff0000", "1", "Red Line");
        Line line3 = new Line("#00ff00", "2", "Green Line");

        Station station1 = new Station("1", 40.7128, line1, -74.0060, "Station A", 1L);
        Station station2 = new Station("1", 40.7128, line2, -74.0060, "Station A", 1L);
        Station station3 = new Station("2", 34.0522, line3, -118.2437, "Station B", 2L);

        assertEquals(station1, station2);
        assertNotEquals(station1, station3);

        assertEquals(station1.hashCode(), station2.hashCode());
        assertNotEquals(station1.hashCode(), station3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Line line = new Line("#ff0000", "1", "Red Line");
        Station station = new Station("1", 40.7128, line, -74.0060, "Station A", 1L);

        String expectedToString = "Station(id=1, lat=40.7128, line=Line(hexColor=#ff0000, id=1, name=Red Line), lng=-74.006, name=Station A, order=1)";
        assertEquals(expectedToString, station.toString());
    }

    @Test
    public void createStationFromJsonTest() {
        String json = """
                {
                  "id": "1",
                  "lat": 40.7128,
                  "line": {
                    "hex_color": "#ff0000",
                    "id": "1",
                    "name": "Red Line"
                  },
                  "lng": -74.0060,
                  "name": "Station A",
                  "order": 1
                }
                """;

        Station station = JsonProcessor.createEntityFromJson(json, Station.class);

        assertEquals(station.getId(), "1");
        assertEquals(station.getLat(), 40.7128);
        assertEquals(station.getLine(), new Line("#ff0000", "1", "Red Line"));
        assertEquals(station.getLng(), -74.0060);
        assertEquals(station.getName(), "Station A");
        assertEquals(station.getOrder(), 1L);
    }

    @Test
    public void createStationWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Station.class));
    }

    @Test
    public void createStationWithNullArgsTest() {
        String json = """
                {
                  "id": "null",
                  "lat": null,
                  "line": null,
                  "lng": null,
                  "name": null,
                  "order": null
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(json, Station.class));
    }
}
