package com.duc.smallproject.modaldialog.util;

import com.duc.smallproject.modaldialog.config.FileProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class FileUploadUtilsTest {
    private final Path path;

    @Autowired
    FileUploadUtilsTest(FileProperties properties) {
        this.path = Paths.get(properties.getUrl());
    }

    @Test
    public void init() throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Test
    public void createFile() throws IOException {
        FileUploadUtils.saveFile("photo-user/1", "test.txt",
                new MockMultipartFile("test.txt", "Hello-wolrd".getBytes(StandardCharsets.UTF_8)));
    }
    @Test
    public void deleteFile() throws IOException {
        FileUploadUtils.clearDir("photo-user");
    }

}