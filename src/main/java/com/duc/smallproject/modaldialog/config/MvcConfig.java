package com.duc.smallproject.modaldialog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirname = "photo-user";
        Path path = Paths.get(dirname);
        String absolutePath = path.toFile().getAbsolutePath();
        registry.addResourceHandler("/" + dirname + "/**")
                    .addResourceLocations("file:/" + absolutePath + "/");
    }
}
