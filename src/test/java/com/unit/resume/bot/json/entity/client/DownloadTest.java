package com.unit.resume.bot.json.entity.client;

import com.resume.bot.json.JsonProcessor;
import com.resume.bot.json.entity.client.Download;
import com.resume.bot.json.entity.client.Pdf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class DownloadTest {
    @Test
    public void gettersAndSettersTest() {
        Pdf pdf = new Pdf("https://example.com/resume.pdf");

        Download download = new Download();
        download.setPdf(pdf);

        assertEquals(download.getPdf(), pdf);
    }

    @Test
    public void settersWithNullArgumentsTest() {
        Download download = new Download();

        assertThrows(NullPointerException.class, () -> download.setPdf(null));
    }

    @Test
    public void constructorWithArgsTest() {
        Pdf pdf = new Pdf("https://example.com/new-resume.pdf");

        Download download = new Download(pdf);

        assertEquals(download.getPdf(), pdf);
    }

    @Test
    public void constructorWithArgsAndNullTest() {
        assertThrows(NullPointerException.class, () -> new Download(null));
    }

    @Test
    public void noArgsConstructorTest() {
        Download download = new Download();

        assertNull(download.getPdf());
    }

    @Test
    public void equalsAndHashCodeTest() {
        Pdf pdf1 = new Pdf("https://example.com/resume.pdf");
        Pdf pdf2 = new Pdf("https://example.com/resume.pdf");
        Pdf pdf3 = new Pdf("https://example.com/new-resume.pdf");

        Download download1 = new Download(pdf1);
        Download download2 = new Download(pdf2);
        Download download3 = new Download(pdf3);

        assertEquals(download1, download2);
        assertNotEquals(download1, download3);

        assertEquals(download1.hashCode(), download2.hashCode());
        assertNotEquals(download1.hashCode(), download3.hashCode());
    }

    @Test
    public void toStringMethodTest() {
        Pdf pdf = new Pdf("https://example.com/resume.pdf");
        Download download = new Download(pdf);

        String expectedToString = "Download(pdf=Pdf(url=https://example.com/resume.pdf))";
        assertEquals(expectedToString, download.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "https://example.com/resume.pdf, https://example.com/resume.pdf",
            "https://example.com/new-resume.pdf, https://example.com/new-resume.pdf"
    })
    public void createDownloadFromJsonTest(String inputUrl, String expectedUrl) {
        String json = """
                {
                  "pdf": {
                    "url": "%s"
                  }
                }
                """.formatted(inputUrl);

        Download download = JsonProcessor.createEntityFromJson(json, Download.class);

        assertNotNull(download.getPdf());
        assertEquals(download.getPdf().getUrl(), expectedUrl);
    }

    @Test
    public void createDownloadWithInvalidJsonTest() {
        String invalidJson = """
                {
                  "pdf": {
                    "invalid_field": "InvalidValue"
                  }
                }
                """;

        assertThrows(RuntimeException.class, () -> JsonProcessor.createEntityFromJson(invalidJson, Download.class));
    }
}
