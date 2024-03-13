package com.resume.unit.bot.json.entity.client.education;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrimaryEducationTest {
    private static String name;
    private static String nameId;
    private static String organization;
    private static String organizationId;
    private static String result;
    private static String resultId;
    private static long year;

    @BeforeAll
    public static void setUp() {
        name = "example";
        nameId = "1";
        organization = "exampleOrg";
        organizationId = "1";
        result = "good";
        resultId = "2";
        year = 2020;
    }

    @Test
    public void correctFill() {
        PrimaryEducation entity = JsonProcessor.createEntityFromJson("""
                {
                    "name": "%s",
                    "name_id": "%s",
                    "organization": "%s",
                    "organization_id": "%s",
                    "result": "%s",
                    "result_id": "%s",
                    "year": %d
                }
                """.formatted(name, nameId, organization, organizationId, result, resultId, year), PrimaryEducation.class);
        assertEquals(entity.getClass(), PrimaryEducation.class);
        assertEquals(entity.getName(), name);
        assertEquals(entity.getOrganization(), organization);
        assertEquals(entity.getOrganizationId(), organizationId);
        assertEquals(entity.getResult(), result);
        assertEquals(entity.getResultId(), resultId);
        assertEquals(entity.getYear(), year);
    }

    @Test
    public void invalidFill() {
        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson("""
                {
                    "name_id": "%s",
                    "organization": "%s",
                    "organization_id": "%s",
                    "result": "%s",
                    "result_id": "%s",
                    "year": %d
                }
                """.formatted(nameId, organization, organizationId, result, resultId, year), PrimaryEducation.class));
    }

    @Test
    public void equalsAndHashCode() {
        PrimaryEducation education1 = new PrimaryEducation("name", "1", "org", "1", "result", "2", 2022);
        PrimaryEducation education2 = new PrimaryEducation("name", "1", "org", "1", "result", "2", 2022);
        PrimaryEducation education3 = new PrimaryEducation("diffName", "1", "org", "1", "result", "2", 2022);

        assertEquals(education1, education2);
        assertNotEquals(education1, education3);
        assertEquals(education1.hashCode(), education2.hashCode());
        assertNotEquals(education1.hashCode(), education3.hashCode());
    }

    @Test
    public void setName() {
        PrimaryEducation education = new PrimaryEducation("oldName", "1", "org", "1", "result", "2", 2022);
        education.setName("newName");
        assertEquals("newName", education.getName());
        assertThrows(NullPointerException.class, () -> education.setName(null));
    }

    @Test
    public void setYear() {
        PrimaryEducation education = new PrimaryEducation("name", "1", "org", "1", "result", "2", 2022);
        education.setYear(2023);
        assertEquals(2023, education.getYear());
    }

    @Test
    public void setNameId() {
        PrimaryEducation education = new PrimaryEducation("name", "oldId", "org", "1", "result", "2", 2022);
        education.setNameId("newId");
        assertEquals("newId", education.getNameId());
    }

    @Test
    public void setOrganization() {
        PrimaryEducation education = new PrimaryEducation("name", "1", "oldOrg", "1", "result", "2", 2022);
        education.setOrganization("newOrg");
        assertEquals("newOrg", education.getOrganization());
    }

    @Test
    public void setOrganizationId() {
        PrimaryEducation education = new PrimaryEducation("name", "1", "org", "oldOrgId", "result", "2", 2022);
        education.setOrganizationId("newOrgId");
        assertEquals("newOrgId", education.getOrganizationId());
    }

    @Test
    public void setResult() {
        PrimaryEducation education = new PrimaryEducation("name", "1", "org", "1", "oldResult", "2", 2022);
        education.setResult("newResult");
        assertEquals("newResult", education.getResult());
    }

    @Test
    public void setResultId() {
        PrimaryEducation education = new PrimaryEducation("name", "1", "org", "1", "result", "oldResultId", 2022);
        education.setResultId("newResultId");
        assertEquals("newResultId", education.getResultId());
    }

    @Test
    public void toStringMethod() {
        PrimaryEducation education = new PrimaryEducation("name", "1", "org", "1", "result", "2", 2022);
        String expectedToString = "PrimaryEducation(name=name, nameId=1, organization=org, organizationId=1, result=result, resultId=2, year=2022)";
        assertEquals(expectedToString, education.toString());
    }
}
