package com.resume.unit.bot.json.entity.roles;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.roles.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {
    private static boolean acceptIncompleteResumes;
    private static String id;
    private static boolean isDefault;
    private static String name;

    @BeforeAll
    public static void setUp() {
        acceptIncompleteResumes = true;
        id = "exampleId";
        isDefault = false;
        name = "example";
    }

    @Test
    public void correctFill() {
        Role entity = JsonProcessor.createEntityFromJson("""
                {
                    "accept_incomplete_resumes": %b,
                    "id": "%s",
                    "is_default": %b,
                    "name": "%s"
                }
                """.formatted(acceptIncompleteResumes, id, isDefault, name), Role.class);
        assertEquals(entity.getClass(), Role.class);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getId(), id);
    }

    @Test
    public void invalidFill() {
        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                    "accept_incomplete_resumes": %b,
                    "is_default": %b,
                    "name": "%s"
                }
                """.formatted(acceptIncompleteResumes, isDefault, name), Role.class));
    }

    @Test
    public void defaultConstructor() {
        Role role = new Role();
        assertNotNull(role);
        assertFalse(role.isAcceptIncompleteResumes());
        assertNull(role.getId());
        assertFalse(role.isDefault());
        assertNull(role.getName());
    }

    @Test
    public void gettersAndSetters() {
        Role role = new Role();

        role.setAcceptIncompleteResumes(true);
        assertTrue(role.isAcceptIncompleteResumes());

        role.setId("newId");
        assertEquals("newId", role.getId());

        role.setDefault(true);
        assertTrue(role.isDefault());

        role.setName("newName");
        assertEquals("newName", role.getName());
    }

    @Test
    public void equalsAndHashCode() {
        Role role1 = new Role("id1", "name1", true, false);
        Role role2 = new Role("id1", "name1", true, false);
        Role role3 = new Role("id2", "name1", true, false);

        assertEquals(role1, role2);
        assertNotEquals(role1, role3);
        assertEquals(role1.hashCode(), role2.hashCode());
        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    public void createRoleWithJsonCreator() {
        Role role = Role.createRole("jsonId", "jsonName", true, false);

        assertTrue(role.isAcceptIncompleteResumes());
        assertEquals("jsonId", role.getId());
        assertFalse(role.isDefault());
        assertEquals("jsonName", role.getName());
    }

    @Test
    public void setNull() {
        Role role = new Role();
        assertThrows(NullPointerException.class, () -> role.setId(null));
        assertThrows(NullPointerException.class, () -> role.setName(null));
    }

    @Test
    public void toStringMethod() {
        Role role = new Role("id1", "name1", true, false);
        String expectedToString = "Role(acceptIncompleteResumes=true, id=id1, isDefault=false, name=name1)";
        assertEquals(expectedToString, role.toString());
    }
}
