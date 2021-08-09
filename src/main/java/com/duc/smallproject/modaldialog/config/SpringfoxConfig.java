package com.duc.smallproject.modaldialog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringfoxConfig {
    @Bean
    public Docket apiDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                //create a builder, which is used to define controllers
                // and include method
                .select()
                //defines the classes
                .apis(RequestHandlerSelectors.any())
                //
                .paths(PathSelectors.any())
                //
                .build();
    }

}
