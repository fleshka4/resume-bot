package com.unit.resume.bot.json.entity.area;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.area.Country;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CountryTest {
    @Test
    public void fullCountryFill() {
        String id = "12";
        String name = "example";
        String url = "https://example.com/12";

        Country entity = JsonProcessor.createEntityFromJson("""
                {
                  "id": "%s",
                  "name": "%s",
                  "url": "%s"
                }
                """.formatted(id, name, url), Country.class);

        assertEquals(entity.getClass(), Country.class);
        assertEquals(entity.getId(), id);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getUrl(), url);
    }

    @Test
    public void partialCountryFill() {
        String name = "example";
        String url = "https://example.com/12";

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                  "name": "%s",
                  "url": "%s"
                }
                """.formatted(name, url), Country.class));
    }

    @Test
    public void gettersAndSettersTest() {
        String id = "12";
        String name = "example";
        String url = "https://example.com/12";

        Country entity = new Country();
        entity.setId(id);
        entity.setName(name);
        entity.setUrl(url);

        assertEquals(entity.getId(), id);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getUrl(), url);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Country entity = new Country();

        assertThrows(NullPointerException.class, () -> entity.setId(null));
        assertThrows(NullPointerException.class, () -> entity.setName(null));
        assertThrows(NullPointerException.class, () -> entity.setUrl(null));
    }

    @Test
    public void constructorWithNullArgumentsTest() {
        assertThrows(NullPointerException.class, () -> new Country(null, "Name", "https://example.com"));
        assertThrows(NullPointerException.class, () -> new Country("1", null, "https://example.com"));
        assertThrows(NullPointerException.class, () -> new Country("1", "Name", null));
    }

    @Test
    public void equalsAndHashCodeTest() {
        Country country1 = new Country("1", "Country1", "https://example.com/country1");
        Country country2 = new Country("1", "Country1", "https://example.com/country1");
        Country country3 = new Country("2", "Country2", "https://example.com/country2");

        
        assertEquals(country1, country2);
        assertNotEquals(country1, country3);

        
        assertEquals(country1.hashCode(), country2.hashCode());
        assertNotEquals(country1.hashCode(), country3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        String id = "12";
        String name = "example";
        String url = "https://example.com/12";

        Country entity = new Country(id, name, url);

        String expectedToString = "Country(id=12, name=example, url=https://example.com/12)";
        assertEquals(expectedToString, entity.toString());
    }
}
