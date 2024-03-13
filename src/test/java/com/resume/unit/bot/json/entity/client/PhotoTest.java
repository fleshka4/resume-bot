package com.resume.unit.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Photo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PhotoTest {
    @Test
    public void createPhotoWithNonNullFieldsTest() {
        String medium = "medium-url";
        String small = "small-url";
        String id = "123";

        Photo photo = new Photo(medium, small, id);

        assertNotNull(photo.getMedium());
        assertEquals(photo.getMedium(), medium);

        assertNotNull(photo.getSmall());
        assertEquals(photo.getSmall(), small);

        assertNotNull(photo.getId());
        assertEquals(photo.getId(), id);
    }

    @Test
    public void createPhotoWithNullNonNullFieldsTest() {
        assertThrows(NullPointerException.class, () -> new Photo(null, null, null));
    }

    @Test
    public void createPhotoWithJsonTest() {
        String json = """
                {
                  "medium": "medium-url",
                  "small": "small-url",
                  "id": "123"
                }
                """;

        Photo photo = JsonProcessor.createEntityFromJson(json, Photo.class);

        assertNotNull(photo.getMedium());
        assertEquals(photo.getMedium(), "medium-url");

        assertNotNull(photo.getSmall());
        assertEquals(photo.getSmall(), "small-url");

        assertNotNull(photo.getId());
        assertEquals(photo.getId(), "123");
    }

    @Test
    public void equalsAndHashCodeTest() {
        Photo photo1 = new Photo("medium-url", "small-url", "123");
        Photo photo2 = new Photo("medium-url", "small-url", "123");
        Photo photo3 = new Photo("other-medium", "other-small", "456");

        assertEquals(photo1, photo2);
        assertNotEquals(photo1, photo3);
        assertEquals(photo1.hashCode(), photo2.hashCode());
        assertNotEquals(photo1.hashCode(), photo3.hashCode());
    }

    @Test
    public void toStringTest() {
        Photo photo = new Photo("medium-url", "small-url", "123");
        String expected = "Photo(medium=medium-url, small=small-url, id=123)";

        assertEquals(expected, photo.toString());
    }

    @Test
    public void noArgsConstructorTest() {
        Photo photo = new Photo();

        assertNull(photo.getMedium());
        assertNull(photo.getSmall());
        assertNull(photo.getId());
    }

    @Test
    public void allArgsConstructorTest() {
        String medium = "medium-url";
        String small = "small-url";
        String id = "123";

        Photo photo = new Photo(medium, small, id);

        assertEquals(medium, photo.getMedium());
        assertEquals(small, photo.getSmall());
        assertEquals(id, photo.getId());
    }
}
