package com.resume.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLoader {

    public static String loadFileContent(String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] fileBytes = Files.readAllBytes(path);
            return new String(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
