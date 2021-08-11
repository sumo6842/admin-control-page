package com.duc.smallproject.modaldialog.util;

import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtils.class);

    //uploadDir:
    public static void saveFile(String uploadDir, String filename,
                                MultipartFile multipartFile) {
        Path upload = Paths.get(uploadDir);
        try {
            if (!Files.exists(upload)) {
                Files.createDirectories(upload);
            }
            Path file = upload.resolve(filename);
            try (InputStream input = multipartFile.getInputStream()) {
                Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            LOGGER.error("Could not save file: " + e);
            LOGGER.error("file: " + uploadDir);
        }
    }

    public static void clearDir(String dir) {
        Path dirPath = Paths.get(dir);
        if (Files.exists(dirPath)) {
            try {
                Files.walk(dirPath, 1)
                    .filter(path -> !path.equals(dirPath))
                    .forEach(Unchecked.consumer(Files::delete));
            } catch (IOException e) {
                LOGGER.error("Could Not delete file: " + e);
            }
        }
    }

    public static void remove(String cateDir) {
        clearDir(cateDir);
        try {
            Files.delete(Path.of(cateDir));
        } catch (IOException e) {
            LOGGER.error("Could not remove directory: " + cateDir);
        }
    }

    public void init() {

    }

}
