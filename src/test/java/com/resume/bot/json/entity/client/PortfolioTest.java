package com.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {
    @Test
    public void createPortfolioWithNonNullValues() {
        String description = "Project details";
        String medium = "medium-url";
        String small = "small-url";
        String id = "123";

        Portfolio portfolio = Portfolio.createPortfolio(description, medium, small, id);

        assertNotNull(portfolio);
        assertEquals(description, portfolio.getDescription());
        assertEquals(medium, portfolio.getMedium());
        assertEquals(small, portfolio.getSmall());
        assertEquals(id, portfolio.getId());
    }

    @Test
    public void createPortfolioWithNullDescription() {
        String medium = "medium-url";
        String small = "small-url";
        String id = "123";

        Portfolio portfolio = Portfolio.createPortfolio(null, medium, small, id);

        assertNotNull(portfolio);
        assertEquals(null, portfolio.getDescription());
        assertEquals(medium, portfolio.getMedium());
        assertEquals(small, portfolio.getSmall());
        assertEquals(id, portfolio.getId());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Portfolio portfolio1 = new Portfolio("Project details", "medium-url", "small-url", "123");
        Portfolio portfolio2 = new Portfolio("Project details", "medium-url", "small-url", "123");
        Portfolio portfolio3 = new Portfolio("Different project", "medium-url", "small-url", "123");

        assertEquals(portfolio1, portfolio2);
        assertNotEquals(portfolio1, portfolio3);

        assertEquals(portfolio1.hashCode(), portfolio2.hashCode());
        assertNotEquals(portfolio1.hashCode(), portfolio3.hashCode());
    }

    @Test
    public void toStringTest() {
        String description = "Project details";
        String medium = "medium-url";
        String small = "small-url";
        String id = "123";

        Portfolio portfolio = new Portfolio(description, medium, small, id);

        String expectedToString = "Portfolio(description=Project details, medium=medium-url, small=small-url, id=123)";
        assertEquals(expectedToString, portfolio.toString());
    }

    @Test
    public void createPortfolioFromJsonTest() {
        String json = "{\"description\":\"Project details\",\"medium\":\"medium-url\",\"small\":\"small-url\",\"id\":\"123\"}";

        Portfolio portfolio = JsonProcessor.createEntityFromJson(json, Portfolio.class);

        assertNotNull(portfolio);
        assertEquals("Project details", portfolio.getDescription());
        assertEquals("medium-url", portfolio.getMedium());
        assertEquals("small-url", portfolio.getSmall());
        assertEquals("123", portfolio.getId());
    }
}
