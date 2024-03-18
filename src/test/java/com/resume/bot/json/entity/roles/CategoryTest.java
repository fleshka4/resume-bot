package com.resume.bot.json.entity.roles;

import com.resume.bot.json.JsonProcessor;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {
    @Test
    public void gettersAndSettersTest() {
        String id = "1";
        String name = "TestCategory";
        List<Role> roles = Arrays.asList(new Role(true, "1", false, "Role1"), new Role(false, "2", true, "Role2"));

        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setRoles(roles);

        assertEquals(category.getId(), id);
        assertEquals(category.getName(), name);
        assertEquals(category.getRoles(), roles);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Category category = new Category();

        assertThrows(NullPointerException.class, () -> category.setId(null));
        assertThrows(NullPointerException.class, () -> category.setName(null));
        assertThrows(NullPointerException.class, () -> category.setRoles(null));
    }

    @Test
    public void constructorWithNullArgumentsTest() {
        assertThrows(NullPointerException.class, () -> new Category(null, "TestCategory", List.of(new Role(true, "1", false, "Role1"))));
        assertThrows(NullPointerException.class, () -> new Category("1", null, List.of(new Role(true, "1", false, "Role1"))));
        assertThrows(NullPointerException.class, () -> new Category("1", "TestCategory", null));
    }

    @Test
    public void equalsAndHashCodeTest() {
        List<Role> roles1 = Arrays.asList(new Role(true, "1", false, "Role1"), new Role(false, "2", true, "Role2"));
        List<Role> roles2 = Arrays.asList(new Role(true, "1", false, "Role1"), new Role(false, "2", true, "Role2"));
        List<Role> roles3 = List.of(new Role(false, "3", true, "Role3"));

        Category category1 = new Category("1", "TestCategory", roles1);
        Category category2 = new Category("1", "TestCategory", roles2);
        Category category3 = new Category("2", "TestCategory2", roles3);

        assertEquals(category1, category2);
        assertNotEquals(category1, category3);

        assertEquals(category1.hashCode(), category2.hashCode());
        assertNotEquals(category1.hashCode(), category3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        String id = "1";
        String name = "TestCategory";
        List<Role> roles = Arrays.asList(new Role(true, "1", false, "Role1"), new Role(false, "2", true, "Role2"));

        Category category = new Category(id, name, roles);

        String expectedToString = "Category(id=1, name=TestCategory, roles=" + roles + ')';
        assertEquals(expectedToString, category.toString());
    }

    @Test
    public void createCategoryFromJsonTest() {
        String id = "1";
        String name = "TestCategory";
        List<Role> roles = Arrays.asList(new Role(true, "1", false, "Role1"), new Role(false, "2", true, "Role2"));

        String json = """
                {
                  "id": "1",
                  "name": "TestCategory",
                  "roles": [
                    {"accept_incomplete_resumes": true, "id": "1", "is_default": false, "name": "Role1"},
                    {"accept_incomplete_resumes": false, "id": "2", "is_default": true, "name": "Role2"}
                  ]
                }
                """;

        Category category = JsonProcessor.createEntityFromJson(json, Category.class);

        assertEquals(category.getId(), id);
        assertEquals(category.getName(), name);
        assertEquals(category.getRoles(), roles);
    }

    @Test
    public void createCategoryWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "name": "TestCategory",
                  "roles": [
                    {"accept_incomplete_resumes": true, "id": "1", "is_default": false, "name": "Role1"},
                    {"accept_incomplete_resumes": false, "id": "2", "is_default": true, "name": "Role2"}
                  ]
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Category.class));
    }

    @Test
    public void noArgsConstructorTest() {
        Category category = new Category();

        assertNull(category.getId());
        assertNull(category.getName());
        assertNull(category.getRoles());
    }
}
