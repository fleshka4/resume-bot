package com.resume.bot.json.entity.client.education;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    @Test
    public void correctFill() {
        String name = "example";
        String organization = "POLY";
        String result = "5";
        int year = 2006;

        String expected = """
                {"name":"%s","organization":"%s","result":"%s","year":%d}""".formatted(name, organization, result, year);

        Course entity = JsonProcessor.createEntityFromJson(expected, Course.class);

        assertEquals(entity.getClass(), Course.class);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getOrganization(), organization);
        assertEquals(entity.getResult(), result);
        assertEquals(entity.getYear(), year);

        String out = JsonProcessor.createJsonFromEntity(entity);
        assertEquals(expected, out);
    }

    @Test
    public void incorrectFill() {
        String name = "example";
        String result = "5";
        int year = 2006;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                    "name": "%s",
                    "result": "%s",
                    "year": "%d"
                }
                """.formatted(name, result, year), Course.class));
    }

    @Test
    public void defaultConstructor() {
        Course course = new Course();
        assertNotNull(course);
        assertNull(course.getName());
        assertNull(course.getOrganization());
        assertNull(course.getResult());
        assertEquals(0, course.getYear());
    }

    @Test
    public void gettersAndSetters() {
        Course course = new Course();

        course.setName("newName");
        assertEquals("newName", course.getName());

        course.setOrganization("newOrganization");
        assertEquals("newOrganization", course.getOrganization());

        course.setResult("newResult");
        assertEquals("newResult", course.getResult());

        course.setYear(2022);
        assertEquals(2022, course.getYear());
    }

    @Test
    public void setNameNull() {
        assertThrows(NullPointerException.class, () -> new Course(null, "org", "result", 2022));
        Course course = new Course();
        assertThrows(NullPointerException.class, () -> course.setName(null));
    }

    @Test
    public void setOrganizationNull() {
        assertThrows(NullPointerException.class, () -> new Course("name", null, "result", 2022));
        Course course = new Course();
        assertThrows(NullPointerException.class, () -> course.setOrganization(null));
    }

    @Test
    public void toStringMethod() {
        Course course = new Course("name", "organization", "result", 2022);
        String expectedToString = "Course(name=name, organization=organization, result=result, year=2022)";
        assertEquals(expectedToString, course.toString());
    }

    @Test
    public void equalsAndHashCode() {
        Course course1 = new Course("name1", "org1", "result1", 2020);
        Course course2 = new Course("name1", "org1", "result1", 2020);
        Course course3 = new Course("name2", "org2", "result2", 2021);

        assertEquals(course1, course2);
        assertNotEquals(course1, course3);
        assertEquals(course1.hashCode(), course2.hashCode());
        assertNotEquals(course1.hashCode(), course3.hashCode());
    }
}
