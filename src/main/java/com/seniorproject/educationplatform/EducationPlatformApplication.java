package com.seniorproject.educationplatform;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableCaching
@SpringBootApplication // (exclude={DataSourceAutoConfiguration.class})
public class EducationPlatformApplication  {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
        // Redis with ElasticSearch does not work unless set below property
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(EducationPlatformApplication.class, args);
    }

}

// Controller For Testing
@RestController
class HelloController {

    @GetMapping("/")
    public ResponseEntity hello() {
        return ResponseEntity.ok("Hello there!");
    }

    @PostMapping("/")
    public ResponseEntity postMain() {
        return ResponseEntity.ok("Response with header using ResponseEntity");
    }

    @GetMapping("/rest")
    public ResponseEntity checkRestApi() {
        return ResponseEntity.ok("REST API works!");
    }

}
