package com.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SiteTest {
    @Test
    public void gettersAndSettersTest() {
        Type type = new Type("1", "LinkedIn");
        Site site = new Site();

        site.setType(type);
        site.setUrl("https://www.linkedin.com/in/example");

        assertEquals(type, site.getType());
        assertEquals("https://www.linkedin.com/in/example", site.getUrl());
    }

    @Test
    public void constructorsTest() {
        Type type = new Type("2", "GitHub");
        Site site1 = new Site(type, "https://github.com/example");
        Site site2 = new Site();

        assertEquals(type, site1.getType());
        assertEquals("https://github.com/example", site1.getUrl());
        assertNull(site2.getType());
        assertNull(site2.getUrl());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Type type1 = new Type("3", "Personal");
        Type type2 = new Type("3", "Personal");
        Type type3 = new Type("4", "Blog");

        Site site1 = new Site(type1, "https://www.example.com");
        Site site2 = new Site(type2, "https://www.example.com");
        Site site3 = new Site(type3, "https://www.blog.com");

        assertEquals(site1, site2);
        assertEquals(site1.hashCode(), site2.hashCode());

        assertNotEquals(site1, site3);
        assertNotEquals(site1.hashCode(), site3.hashCode());
    }

    @Test
    public void toStringTest() {
        Type type = new Type("5", "Portfolio");
        Site site = new Site(type, "https://www.portfolio.com");

        String expected = "Site(type=Type(id=5, name=Portfolio), url=https://www.portfolio.com)";
        assertEquals(expected, site.toString());
    }

    @Test
    public void createEntityFromJsonTest() {
        String json = "{\"type\": {\"id\": \"6\", \"name\": \"Twitter\"}, \"url\": \"https://twitter.com/example\"}";

        Site site = JsonProcessor.createEntityFromJson(json, Site.class);

        Type expectedType = new Type("6", "Twitter");
        assertEquals(expectedType, site.getType());
        assertEquals("https://twitter.com/example", site.getUrl());
    }
}
