package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.TotalExperience;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TotalExperienceTest {
    @Test
    public void gettersAndSettersTest() {
        TotalExperience totalExperience = new TotalExperience();

        totalExperience.setMonths(12L);

        assertEquals(12L, totalExperience.getMonths());
    }

    @Test
    public void constructorsTest() {
        TotalExperience totalExperience1 = new TotalExperience(24L);
        TotalExperience totalExperience2 = new TotalExperience();

        assertEquals(24L, totalExperience1.getMonths());
        assertNull(totalExperience2.getMonths());
    }

    @Test
    public void equalsAndHashCodeTest() {
        TotalExperience totalExperience1 = new TotalExperience(36L);
        TotalExperience totalExperience2 = new TotalExperience(36L);

        assertEquals(totalExperience1, totalExperience2);
        assertEquals(totalExperience1.hashCode(), totalExperience2.hashCode());

        totalExperience2.setMonths(48L);
        assertNotEquals(totalExperience1, totalExperience2);
        assertNotEquals(totalExperience1.hashCode(), totalExperience2.hashCode());
    }

    @Test
    public void toStringTest() {
        TotalExperience totalExperience = new TotalExperience(60L);

        String expected = "TotalExperience(months=60)";
        assertEquals(expected, totalExperience.toString());
    }

    @Test
    public void createEntityFromJsonTest() {
        String json = "{\"months\": 72}";

        TotalExperience totalExperience = JsonProcessor.createEntityFromJson(json, TotalExperience.class);

        assertEquals(72L, totalExperience.getMonths());
    }
}
