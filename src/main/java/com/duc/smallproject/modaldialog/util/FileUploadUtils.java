package com.duc.smallproject.modaldialog.util;

import org.jooq.lambda.Unchecked;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtils {
    //uploadDir:
    public static void saveFile(String uploadDir, String filename,
                                MultipartFile multipartFile) throws IOException {
        Path upload = Paths.get(uploadDir);
        if (!Files.exists(upload)) {
            Files.createDirectories(upload);
        }
        Path file = upload.resolve(filename);
        try(InputStream input = multipartFile.getInputStream()) {
            Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void clearDir(String dir) throws IOException {
        Path dirPath = Paths.get(dir);
        if (Files.exists(dirPath)) {
            Files.walk(dirPath, 1)
                .filter(path -> !path.equals(dirPath))
                .forEach(Unchecked.consumer(Files::delete));
        }
    }

    public void init() {

    }

}
