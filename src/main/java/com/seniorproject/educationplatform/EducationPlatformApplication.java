package com.seniorproject.educationplatform;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication // (exclude={DataSourceAutoConfiguration.class})
public class EducationPlatformApplication  {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public static void main(String[] args) {
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
