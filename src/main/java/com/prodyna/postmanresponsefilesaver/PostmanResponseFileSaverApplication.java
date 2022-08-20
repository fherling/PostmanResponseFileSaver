package com.prodyna.postmanresponsefilesaver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.prodyna.postmanresponsefilesaver.config")
public class PostmanResponseFileSaverApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostmanResponseFileSaverApplication.class, args);
    }

}
