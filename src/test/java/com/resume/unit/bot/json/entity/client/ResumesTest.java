package com.resume.unit.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Resumes;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResumesTest {
    @Test
    public void gettersAndSettersTest() {
        Resumes resumes = new Resumes();

        resumes.setFound(10);
        resumes.setPage(1);
        resumes.setPages(2);
        resumes.setPerPage(5);
        resumes.setItems(Collections.emptyList());

        assertEquals(10, resumes.getFound());
        assertEquals(1, resumes.getPage());
        assertEquals(2, resumes.getPages());
        assertEquals(5, resumes.getPerPage());
        assertEquals(Collections.emptyList(), resumes.getItems());
    }

    @Test
    public void allArgsConstructorTest() {
        Resumes resumes = new Resumes(10, 1, 2, 5, Collections.emptyList());

        assertEquals(10, resumes.getFound());
        assertEquals(1, resumes.getPage());
        assertEquals(2, resumes.getPages());
        assertEquals(5, resumes.getPerPage());
        assertEquals(Collections.emptyList(), resumes.getItems());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Resumes resumes1 = new Resumes(10, 1, 2, 5, Collections.emptyList());
        Resumes resumes2 = new Resumes(10, 1, 2, 5, Collections.emptyList());

        assertEquals(resumes1, resumes2);
        assertEquals(resumes1.hashCode(), resumes2.hashCode());
    }

    @Test
    public void toStringTest() {
        Resumes resumes = new Resumes(10, 1, 2, 5, Collections.emptyList());

        assertNotNull(resumes.toString());
    }

    @Test
    public void createEntityFromJsonTest() {
        String json = "{\"found\":10,\"page\":1,\"pages\":2,\"per_page\":5,\"items\":[]}";

        Resumes resumes = JsonProcessor.createEntityFromJson(json, Resumes.class);

        assertEquals(10, resumes.getFound());
        assertEquals(1, resumes.getPage());
        assertEquals(2, resumes.getPages());
        assertEquals(5, resumes.getPerPage());
        assertEquals(Collections.emptyList(), resumes.getItems());
    }
}
