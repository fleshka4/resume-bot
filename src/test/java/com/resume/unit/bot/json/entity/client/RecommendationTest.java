package com.resume.unit.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Recommendation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RecommendationTest {
    @Test
    public void createRecommendationWithNonNullValues() {
        String contact = "john.doe@example.com";
        String name = "John Doe";
        String organization = "ABC Corp";
        String position = "Manager";

        Recommendation recommendation = Recommendation.createRecommendation(contact, name, organization, position);

        assertNotNull(recommendation);
        assertEquals(contact, recommendation.getContact());
        assertEquals(name, recommendation.getName());
        assertEquals(organization, recommendation.getOrganization());
        assertEquals(position, recommendation.getPosition());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Recommendation recommendation1 = new Recommendation("john.doe@example.com", "John Doe", "ABC Corp", "Manager");
        Recommendation recommendation2 = new Recommendation("john.doe@example.com", "John Doe", "ABC Corp", "Manager");
        Recommendation recommendation3 = new Recommendation("jane.smith@example.com", "Jane Smith", "XYZ Inc", "Supervisor");

        assertEquals(recommendation1, recommendation2);
        assertNotEquals(recommendation1, recommendation3);

        assertEquals(recommendation1.hashCode(), recommendation2.hashCode());
        assertNotEquals(recommendation1.hashCode(), recommendation3.hashCode());
    }

    @Test
    public void toStringTest() {
        String contact = "john.doe@example.com";
        String name = "John Doe";
        String organization = "ABC Corp";
        String position = "Manager";

        Recommendation recommendation = new Recommendation(contact, name, organization, position);

        String expectedToString = "Recommendation(contact=john.doe@example.com, name=John Doe, organization=ABC Corp, position=Manager)";
        assertEquals(expectedToString, recommendation.toString());
    }

    @Test
    public void createRecommendationFromJsonTest() {
        String json = "{\"contact\":\"john.doe@example.com\",\"name\":\"John Doe\",\"organization\":\"ABC Corp\",\"position\":\"Manager\"}";

        Recommendation recommendation = JsonProcessor.createEntityFromJson(json, Recommendation.class);

        assertNotNull(recommendation);
        assertEquals("john.doe@example.com", recommendation.getContact());
        assertEquals("John Doe", recommendation.getName());
        assertEquals("ABC Corp", recommendation.getOrganization());
        assertEquals("Manager", recommendation.getPosition());
    }

    @Test
    public void gettersAndSettersTest() {
        Recommendation recommendation = new Recommendation();

        assertNull(recommendation.getContact());
        assertNull(recommendation.getName());
        assertNull(recommendation.getOrganization());
        assertNull(recommendation.getPosition());

        String newContact = "new.contact@example.com";
        String newName = "New Name";
        String newOrganization = "New Org";
        String newPosition = "New Position";

        recommendation.setContact(newContact);
        recommendation.setName(newName);
        recommendation.setOrganization(newOrganization);
        recommendation.setPosition(newPosition);

        assertEquals(newContact, recommendation.getContact());
        assertEquals(newName, recommendation.getName());
        assertEquals(newOrganization, recommendation.getOrganization());
        assertEquals(newPosition, recommendation.getPosition());
    }

    @Test
    public void noArgsConstructorTest() {
        Recommendation recommendation = new Recommendation();

        assertNull(recommendation.getContact());
        assertNull(recommendation.getName());
        assertNull(recommendation.getOrganization());
        assertNull(recommendation.getPosition());
    }

    @Test
    public void allArgsConstructorTest() {
        String contact = "john.doe@example.com";
        String name = "John Doe";
        String organization = "ABC Corp";
        String position = "Manager";

        Recommendation recommendation = new Recommendation(contact, name, organization, position);

        assertEquals(contact, recommendation.getContact());
        assertEquals(name, recommendation.getName());
        assertEquals(organization, recommendation.getOrganization());
        assertEquals(position, recommendation.getPosition());
    }
}
