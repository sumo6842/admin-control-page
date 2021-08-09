package com.duc.smallproject.modaldialog;

import com.duc.smallproject.modaldialog.config.FileProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableConfigurationProperties(FileProperties.class)
@EnableSwagger2
public class ModalDialogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModalDialogApplication.class, args);
    }

}
