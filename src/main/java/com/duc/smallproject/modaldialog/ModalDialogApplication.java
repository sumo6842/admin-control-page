package com.duc.smallproject.modaldialog;

import com.duc.smallproject.modaldialog.config.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(FileProperties.class)
public class ModalDialogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModalDialogApplication.class, args);
    }

}
