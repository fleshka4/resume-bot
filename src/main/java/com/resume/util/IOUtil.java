package com.resume.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@UtilityClass
public class IOUtil {
    public static String loadFileContent(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String loadFileContent(Path path) {
        try {
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static void writeStringToFile(String content, Path path) {
        if (!path.toFile().delete()) {
            log.warn("File %s was not deleted!".formatted(path.toString()));
        }

        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
