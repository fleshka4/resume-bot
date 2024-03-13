package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Pdf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PdfTest {
    @Test
    public void gettersAndSettersTest() {
        String url = "https://example.com/resume.pdf";

        Pdf pdf = new Pdf();
        pdf.setUrl(url);

        assertEquals(pdf.getUrl(), url);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Pdf pdf = new Pdf();

        assertThrows(NullPointerException.class, () -> pdf.setUrl(null));
    }

    @Test
    public void constructorWithArgsTest() {
        String url = "https://example.com/new-resume.pdf";

        Pdf pdf = new Pdf(url);

        assertEquals(pdf.getUrl(), url);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Pdf(null));
    }

    @Test
    public void noArgsConstructorTest() {
        Pdf pdf = new Pdf();

        assertNull(pdf.getUrl());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Pdf pdf1 = new Pdf("https://example.com/resume.pdf");
        Pdf pdf2 = new Pdf("https://example.com/resume.pdf");
        Pdf pdf3 = new Pdf("https://example.com/new-resume.pdf");

        assertEquals(pdf1, pdf2);
        assertNotEquals(pdf1, pdf3);

        assertEquals(pdf1.hashCode(), pdf2.hashCode());
        assertNotEquals(pdf1.hashCode(), pdf3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Pdf pdf = new Pdf("https://example.com/resume.pdf");

        String expectedToString = "Pdf(url=https://example.com/resume.pdf)";
        assertEquals(expectedToString, pdf.toString());
    }

    @Test
    public void createPdfFromJsonTest() {
        String json = """
                {
                  "url": "https://example.com/new-resume.pdf"
                }
                """;

        Pdf pdf = JsonProcessor.createEntityFromJson(json, Pdf.class);

        assertEquals(pdf.getUrl(), "https://example.com/new-resume.pdf");
    }

    @Test
    public void createPdfWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "invalid_field": "InvalidValue"
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Pdf.class));
    }
}
