package com.jobportal.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Creating Util to download CV
public class FileDownloadUtil {
    private Path foundPath;

    public Resource getfFileAsResource(String downloadDir, String fileName) throws IOException {
        Path path = Paths.get(downloadDir);
        Files.list(path).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileName)) {
                foundPath = file;
            }
        });

        if (foundPath != null) {
            return new UrlResource(foundPath.toUri());
        }return null;
    }
}
