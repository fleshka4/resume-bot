package com.resume.unit.bot.json.entity.metro.station;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.metro.station.Line;
import com.resume.bot.json.entity.metro.station.Station;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StationTest {
    @Test
    public void gettersAndSettersTest() {
        String id = "1";
        Double lat = 40.7128;
        Line line = new Line("1", "#ff0000", "Red Line");
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
        Line line = new Line("2", "#00ff00", "Green Line");
        Double lng = -118.2437;
        String name = "Station B";
        Long order = 2L;

        Station station = new Station(id, name, lat, lng, order, line);

        assertEquals(station.getId(), id);
        assertEquals(station.getLat(), lat);
        assertEquals(station.getLine(), line);
        assertEquals(station.getLng(), lng);
        assertEquals(station.getName(), name);
        assertEquals(station.getOrder(), order);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Station(null, "Station A", 40.7128, -74.0060, 1L, new Line("1", "#ff0000", "Red Line")));
        assertThrows(NullPointerException.class, () -> new Station("1", "Station A", null, -74.0060, 1L, new Line("1", "#ff0000", "Red Line")));
        assertThrows(NullPointerException.class, () -> new Station("1", "Station A", 40.7128, -74.0060, 1L, null));
        assertThrows(NullPointerException.class, () -> new Station("1", "Station A", 40.7128, null, 1L, new Line("1", "#ff0000", "Red Line")));
        assertThrows(NullPointerException.class, () -> new Station("1", null, 40.7128, -74.0060, 1L, new Line("1", "#ff0000", "Red Line")));
        assertThrows(NullPointerException.class, () -> new Station("1", "Station A", 40.7128, -74.0060, null, new Line("1", "#ff0000", "Red Line")));
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
        Line line1 = new Line("1", "#ff0000", "Red Line");
        Line line2 = new Line("1", "#ff0000", "Red Line");
        Line line3 = new Line("2", "#00ff00", "Green Line");

        Station station1 = new Station("1", "Station A", 40.7128, -74.0060, 1L, line1);
        Station station2 = new Station("1", "Station A", 40.7128, -74.0060, 1L, line2);
        Station station3 = new Station("2", "Station B", 34.0522, -118.2437, 2L, line3);

        assertEquals(station1, station2);
        assertNotEquals(station1, station3);

        assertEquals(station1.hashCode(), station2.hashCode());
        assertNotEquals(station1.hashCode(), station3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Line line = new Line("1", "#ff0000", "Red Line");
        Station station = new Station("1", "Station A", 40.712, -74.0060, 1L, line);

        String expectedToString = "Station(id=1, name=Station A, lat=40.712, lng=-74.006, order=1, line=Line(id=1, hexColor=#ff0000, name=Red Line))";
        assertEquals(expectedToString, station.toString());
    }

    @Test
    public void createStationFromJsonTest() {
        String json = """
                {
                  "id": "1",
                  "name": "Station A",
                  "lat": 40.7128,
                  "lng": -74.0060,
                  "order": 1,
                  "line": {
                    "id": "1",
                    "hex_color": "#ff0000",
                    "name": "Red Line"
                  }
                }
                """;

        Station station = JsonProcessor.createEntityFromJson(json, Station.class);

        assertEquals(station.getId(), "1");
        assertEquals(station.getLat(), 40.7128);
        assertEquals(station.getLine(), new Line("1", "#ff0000", "Red Line"));
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
                  "name": null,
                  "lat": null,
                  "lng": null,
                  "order": null,
                  "line": null
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(json, Station.class));
    }
}
