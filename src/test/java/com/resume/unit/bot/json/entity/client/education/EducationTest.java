package com.resume.unit.bot.json.entity.client.education;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.education.Course;
import com.resume.bot.json.entity.client.education.Education;
import com.resume.bot.json.entity.client.education.ElementaryEducation;
import com.resume.bot.json.entity.client.education.PrimaryEducation;
import com.resume.bot.json.entity.common.Type;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EducationTest {

    @Test
    public void createEducationFromJson() {
        String json = """
                {
                    "additional": [{"name": "additionalCourse", "organization": "org", "result": "A", "year": 2022}],
                    "attestation": [{"name": "attestationCourse", "organization": "org", "result": "A", "year": 2023}],
                    "elementary": [{"name": "elementary", "year": 2020}],
                    "level": {"id": "UNDERGRAD", "name": "Undergraduate"},
                    "primary": [{"name": "primary", "name_id": "1", "organization": "org", "organization_id": "1", "result": "A", "result_id": "2", "year": 2021}]
                }
                """;

        Education education = JsonProcessor.createEntityFromJson(json, Education.class);

        List<Course> additionalCourses = List.of(new Course("additionalCourse", "org", "A", 2022));
        List<Course> attestationCourses = List.of(new Course("attestationCourse", "org", "A", 2023));
        List<ElementaryEducation> elementaryEducations = List.of(new ElementaryEducation("elementary", 2020));
        Type level = new Type("UNDERGRAD", "Undergraduate");
        List<PrimaryEducation> primaryEducations = List.of(new PrimaryEducation("primary", "1", "org", "1", "A", "2", 2021));

        assertEquals(additionalCourses, education.getAdditional());
        assertEquals(attestationCourses, education.getAttestation());
        assertEquals(elementaryEducations, education.getElementary());
        assertEquals(level, education.getLevel());
        assertEquals(primaryEducations, education.getPrimary());
    }

    @Test
    public void equalsAndHashCode() {
        Education education1 = new Education(
                List.of(new Course("course1", "org1", "A", 2021)),
                List.of(new Course("course2", "org2", "B", 2022)),
                List.of(new ElementaryEducation("elementary1", 2020)),
                new Type("UNDERGRAD", "Undergraduate"),
                List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
        );

        Education education2 = new Education(
                List.of(new Course("course1", "org1", "A", 2021)),
                List.of(new Course("course2", "org2", "B", 2022)),
                List.of(new ElementaryEducation("elementary1", 2020)),
                new Type("UNDERGRAD", "Undergraduate"),
                List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
        );

        Education education3 = new Education(
                List.of(new Course("course3", "org3", "C", 2023)),
                List.of(new Course("course4", "org4", "D", 2024)),
                List.of(new ElementaryEducation("elementary2", 2021)),
                new Type("GRAD", "Graduate"),
                List.of(new PrimaryEducation("primary2", "2", "org2", "2", "B", "3", 2022))
        );

        assertEquals(education1, education2);
        assertNotEquals(education1, education3);
        assertEquals(education1.hashCode(), education2.hashCode());
        assertNotEquals(education1.hashCode(), education3.hashCode());
    }

    @Test
    public void setAdditional() {
        Education education = new Education();
        List<Course> additionalCourses = Arrays.asList(
                new Course("course1", "org1", "A", 2021),
                new Course("course2", "org2", "B", 2022)
        );
        education.setAdditional(additionalCourses);
        assertEquals(additionalCourses, education.getAdditional());
    }

    @Test
    public void setAttestation() {
        Education education = new Education();
        List<Course> attestationCourses = Collections.singletonList(new Course("course1", "org1", "A", 2021));
        education.setAttestation(attestationCourses);
        assertEquals(attestationCourses, education.getAttestation());
    }

    @Test
    public void setElementary() {
        Education education = new Education();
        List<ElementaryEducation> elementaryEducations = Collections.singletonList(new ElementaryEducation("elementary1", 2020));
        education.setElementary(elementaryEducations);
        assertEquals(elementaryEducations, education.getElementary());
    }

    @Test
    public void setLevel() {
        Education education = new Education();
        Type level = new Type("UNDERGRAD", "Undergraduate");
        education.setLevel(level);
        assertEquals(level, education.getLevel());
    }

    @Test
    public void setPrimary() {
        Education education = new Education();
        List<PrimaryEducation> primaryEducations = Collections.singletonList(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021));
        education.setPrimary(primaryEducations);
        assertEquals(primaryEducations, education.getPrimary());
    }

    @Test
    public void toStringMethod() {
        Education education = new Education(
                List.of(new Course("course1", "org1", "A", 2021)),
                List.of(new Course("course2", "org2", "B", 2022)),
                List.of(new ElementaryEducation("elementary1", 2020)),
                new Type("UNDERGRAD", "Undergraduate"),
                List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021))
        );

        String expectedToString = "Education(" +
                "additional=" + List.of(new Course("course1", "org1", "A", 2021)) +
                ", attestation=" + List.of(new Course("course2", "org2", "B", 2022)) +
                ", elementary=" + List.of(new ElementaryEducation("elementary1", 2020)) +
                ", level=" + new Type("UNDERGRAD", "Undergraduate") +
                ", primary=" + List.of(new PrimaryEducation("primary1", "1", "org1", "1", "A", "2", 2021)) +
                ')';

        assertEquals(expectedToString, education.toString());
    }
}
