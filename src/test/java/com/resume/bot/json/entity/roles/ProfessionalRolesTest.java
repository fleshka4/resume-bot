package com.resume.bot.json.entity.roles;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProfessionalRolesTest {
    @Test
    public void gettersAndSettersTest() {
        List<Category> categories = Arrays.asList(
                new Category("1", "Category1", List.of(new Role(true, "1", false, "Role1"))),
                new Category("2", "Category2", List.of(new Role(false, "2", true, "Role2")))
        );

        ProfessionalRoles professionalRoles = new ProfessionalRoles();
        professionalRoles.setCategories(categories);

        assertEquals(professionalRoles.getCategories(), categories);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        ProfessionalRoles professionalRoles = new ProfessionalRoles();

        assertThrows(NullPointerException.class, () -> professionalRoles.setCategories(null));
    }

    @Test
    public void constructorWithArgsTest() {
        List<Category> categories = List.of(
                new Category("1", "Category1", List.of(new Role(true, "1", false, "Role1")))
        );

        ProfessionalRoles professionalRoles = new ProfessionalRoles(categories);

        assertEquals(professionalRoles.getCategories(), categories);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new ProfessionalRoles(null));
    }

    @Test
    public void noArgsConstructorTest() {
        ProfessionalRoles professionalRoles = new ProfessionalRoles();

        assertNull(professionalRoles.getCategories());
    }

    @Test
    public void equalsAndHashCodeTest() {
        List<Category> categories1 = List.of(new Category("1", "Category1", List.of(new Role(false, "1", true, "Role1"))));
        List<Category> categories2 = List.of(new Category("1", "Category1", List.of(new Role(false, "1", true, "Role1"))));
        List<Category> categories3 = List.of(new Category("2", "Category2", List.of(new Role(false, "3", true, "Role3"))));

        ProfessionalRoles professionalRoles1 = new ProfessionalRoles(categories1);
        ProfessionalRoles professionalRoles2 = new ProfessionalRoles(categories2);
        ProfessionalRoles professionalRoles3 = new ProfessionalRoles(categories3);

        
        assertEquals(professionalRoles1, professionalRoles2);
        assertNotEquals(professionalRoles1, professionalRoles3);

        
        assertEquals(professionalRoles1.hashCode(), professionalRoles2.hashCode());
        assertNotEquals(professionalRoles1.hashCode(), professionalRoles3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        List<Category> categories = Arrays.asList(
                new Category("1", "Category1", List.of(new Role(true, "1", false, "Role1"))),
                new Category("2", "Category2", List.of(new Role(false, "2", true, "Role2")))
        );

        ProfessionalRoles professionalRoles = new ProfessionalRoles(categories);

        String expectedToString = "ProfessionalRoles(categories=" + categories + ')';
        assertEquals(expectedToString, professionalRoles.toString());
    }

    @Test
    public void createProfessionalRolesFromJsonTest() {
        List<Category> categories = Arrays.asList(
                new Category("1", "Category1", List.of(new Role(true, "1", false, "Role1"))),
                new Category("2", "Category2", List.of(new Role(false, "2", true, "Role2")))
        );

        String json = """
                {
                  "categories": [
                    {
                      "id": "1",
                      "name": "Category1",
                      "roles": [
                        {"accept_incomplete_resumes": true, "id": "1", "is_default": false, "name": "Role1"}
                      ]
                    },
                    {
                      "id": "2",
                      "name": "Category2",
                      "roles": [
                        {"accept_incomplete_resumes": false, "id": "2", "is_default": true, "name": "Role2"}
                      ]
                    }
                  ]
                }
                """;

        ProfessionalRoles professionalRoles = JsonProcessor.createEntityFromJson(json, ProfessionalRoles.class);

        assertEquals(professionalRoles.getCategories(), categories);
    }

    @Test
    public void createProfessionalRolesWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "categories": [
                    {
                      "name": "Category1",
                      "roles": [
                        {"accept_incomplete_resumes": true, "id": "1", "is_default": false, "name": "Role1"}
                      ]
                    }
                  ]
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, ProfessionalRoles.class));
    }

    @Test
    public void createProfessionalRolesWithMissingCategoriesTest() {
        String json = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(json, ProfessionalRoles.class));
    }
}
