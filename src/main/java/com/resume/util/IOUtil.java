package com.resume.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@UtilityClass
public class IOUtil {
    public static String loadFileContent(String filePath) {
        return loadFileContent(Paths.get(filePath));
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
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
