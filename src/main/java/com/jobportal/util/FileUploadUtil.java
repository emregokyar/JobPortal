package com.jobportal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//Creating Util to upload cv and picture
public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir); //Converting string into path and if no exist, create one
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path path = uploadPath.resolve(fileName);
            /*
            System.out.println("FilePath " + path);
            System.out.println("FileName " + fileName);
             */
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING); //Reading the picture and copying it into photos directory if exists replace
        } catch (IOException e) {
            throw new IOException("Couldn't save image file: " + fileName, e);
        }
    }
}
