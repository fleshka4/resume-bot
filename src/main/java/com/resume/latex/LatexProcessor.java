package com.resume.latex;

import com.resume.bot.json.entity.client.Resume;
import com.resume.util.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Slf4j
public class LatexProcessor {
    private static final Path tempDir = Paths.get(System.getProperty("user.home")).resolve(".resume_bot");

    public static String compile(Resume resume, String sourcePath, String userId, String resumeName) throws IOException, InterruptedException {
        Path sourceTmpFile = Paths.get(sourcePath);
        Path outputUserDir = tempDir.resolve(userId);
        Path outputUserTex = outputUserDir.resolve(resumeName + ".txt");
        Path outputUserPdf = outputUserDir.resolve(resumeName + ".pdf");

        Files.createDirectories(outputUserDir);
        Files.copy(sourceTmpFile, outputUserTex, REPLACE_EXISTING);
        String content = IOUtil.loadFileContent(outputUserTex);
        content = replaceContent(content, resume);
        IOUtil.writeStringToFile(content, outputUserTex);

        ProcessBuilder processBuilder = new ProcessBuilder("pdflatex",
                "-output-directory=%s".formatted(outputUserDir.toString()),
                outputUserTex.toString());
        Process process = processBuilder.start();
        if (process.waitFor() != 0) {
            throw new RuntimeException("pdflatex exited with non-zero code.");
        }

        if (!outputUserTex.toFile().delete()) {
            log.warn("File with path: %s was not deleted".formatted(outputUserTex.toString()));
        }

        return outputUserPdf.toString();
    }

    private static String replaceContent(String content, Resume resume) {
        for (Placeholder ph: Placeholder.values()) {
            content = insertValue(content, ph, ph.replaceValue(resume));
        }
        return content;
    }

    private static String insertValue(String content, Placeholder placeholder, String value) {
        if (!content.contains(placeholder.getValue())) {
            return content;
        }

        if (value == null) {
            throw new RuntimeException("Value for %s is empty".formatted(placeholder.getValue()));
        }

        return content.replaceAll(placeholder.getValue(), value);
    }
}
