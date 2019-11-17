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

@RestController
class HelloController {

    @GetMapping("/")
    public String hello() {
        return "Hello there!";
    }

    @GetMapping("/api")
    public String api() {
        return "API works!";
    }

    @PostMapping("/")
    public ResponseEntity postMain() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        return ResponseEntity.ok().headers(responseHeaders).body("Response with header using ResponseEntity");
    }

}
