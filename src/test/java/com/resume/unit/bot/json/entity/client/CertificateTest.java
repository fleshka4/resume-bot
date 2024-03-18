package com.resume.unit.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Certificate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateTest {
    @Test
    public void gettersAndSettersTest() {
        String achievedAt = "2022-01-01";
        String owner = "John Doe";
        String title = "Java Certification";
        String type = "Professional";
        String url = "https://example.com/certificate";

        Certificate certificate = new Certificate();
        certificate.setAchievedAt(achievedAt);
        certificate.setOwner(owner);
        certificate.setTitle(title);
        certificate.setType(type);
        certificate.setUrl(url);

        assertEquals(certificate.getAchievedAt(), achievedAt);
        assertEquals(certificate.getOwner(), owner);
        assertEquals(certificate.getTitle(), title);
        assertEquals(certificate.getType(), type);
        assertEquals(certificate.getUrl(), url);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Certificate certificate = new Certificate();

        assertThrows(NullPointerException.class, () -> certificate.setAchievedAt(null));
        assertDoesNotThrow(() -> certificate.setOwner(null));
        assertThrows(NullPointerException.class, () -> certificate.setTitle(null));
        assertThrows(NullPointerException.class, () -> certificate.setType(null));
        assertDoesNotThrow(() -> certificate.setUrl(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String achievedAt = "2022-02-15";
        String owner = "Jane Doe";
        String title = "Python Certification";
        String type = "Advanced";
        String url = "https://example.com/python-certificate";

        Certificate certificate = new Certificate(achievedAt, owner, title, type, url);

        assertEquals(certificate.getAchievedAt(), achievedAt);
        assertEquals(certificate.getOwner(), owner);
        assertEquals(certificate.getTitle(), title);
        assertEquals(certificate.getType(), type);
        assertEquals(certificate.getUrl(), url);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Certificate(null, "John Doe", "Java Certification", "Professional", null));
        assertDoesNotThrow(() -> new Certificate("2022-01-01", null, "Java Certification", "Professional", null));
        assertThrows(NullPointerException.class, () -> new Certificate("2022-01-01", "John Doe", null, "Professional", null));
        assertThrows(NullPointerException.class, () -> new Certificate("2022-01-01", "John Doe", "Java Certification", null, null));
    }

    @Test
    public void noArgsConstructorTest() {
        Certificate certificate = new Certificate();

        assertNull(certificate.getAchievedAt());
        assertNull(certificate.getOwner());
        assertNull(certificate.getTitle());
        assertNull(certificate.getType());
        assertNull(certificate.getUrl());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Certificate certificate1 = new Certificate("2022-01-01", "John Doe", "Java Certification", "Professional", null);
        Certificate certificate2 = new Certificate("2022-01-01", "John Doe", "Java Certification", "Professional", null);
        Certificate certificate3 = new Certificate("2022-02-01", "Jane Doe", "Python Certification", "Advanced", null);

        assertEquals(certificate1, certificate2);
        assertNotEquals(certificate1, certificate3);

        assertEquals(certificate1.hashCode(), certificate2.hashCode());
        assertNotEquals(certificate1.hashCode(), certificate3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Certificate certificate = new Certificate("2022-01-01", "John Doe", "Java Certification", "Professional", null);

        String expectedToString = "Certificate(achievedAt=2022-01-01, owner=John Doe, title=Java Certification, type=Professional, url=null)";
        assertEquals(expectedToString, certificate.toString());
    }

    @Test
    public void createCertificateFromJsonTest() {
        String json = """
                {
                  "achieved_at": "2022-03-01",
                  "owner": "Alice",
                  "title": "Web Development Certification",
                  "type": "Intermediate",
                  "url": "https://example.com/web-dev-certificate"
                }
                """;

        Certificate certificate = JsonProcessor.createEntityFromJson(json, Certificate.class);

        assertEquals(certificate.getAchievedAt(), "2022-03-01");
        assertEquals(certificate.getOwner(), "Alice");
        assertEquals(certificate.getTitle(), "Web Development Certification");
        assertEquals(certificate.getType(), "Intermediate");
        assertEquals(certificate.getUrl(), "https://example.com/web-dev-certificate");
    }

    @Test
    public void createCertificateWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Certificate.class));
    }
}
