package com.resume.util;

import org.assertj.core.util.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IOUtilTest {
    private static final Path resources = Paths.get("src", "test", "resources");
    private static final Path IOUtilResources = resources.resolve("com").resolve("resume").resolve("util");

    @Test
    public void loadFileContentPathTest() {
        Path correctPath = IOUtilResources.resolve("test.tex");
        Path incorrectPath = correctPath.resolve("10");

        Assertions.assertNotNull(IOUtil.loadFileContent(correctPath));
        Assertions.assertNull(IOUtil.loadFileContent(incorrectPath));
    }

    @Test
    public void loadFileContentStringTest() {
        String correctPath = IOUtilResources.resolve("test.tex").toString();
        String incorrectPath = correctPath + "10";

        Assertions.assertNotNull(IOUtil.loadFileContent(correctPath));
        Assertions.assertNull(IOUtil.loadFileContent(incorrectPath));
    }

    @Test
    public void writeStringToFileTest() throws IOException {
        String textToWrite = "It's the test!";
        Path correctFile = IOUtilResources.resolve("writeTest.txt");
        Path incorrectFile = correctFile.resolve("12032");

        Assertions.assertTrue(correctFile.toFile().createNewFile());
        Assertions.assertNotNull(correctFile.toFile());

        IOUtil.writeStringToFile(textToWrite, correctFile);
        String fileContent = Files.contentOf(correctFile.toFile(), Charset.defaultCharset());
        Assertions.assertNotNull(fileContent);
        Assertions.assertEquals(fileContent, textToWrite);

        correctFile.toFile().deleteOnExit();

        IOUtil.writeStringToFile(textToWrite, correctFile);
        Assertions.assertFalse(incorrectFile.toFile().exists());
    }
}
