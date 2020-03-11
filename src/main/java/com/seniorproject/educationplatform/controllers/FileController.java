package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.exception.CustomException;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.services.FileService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/static")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private FileService fileService;
    private CourseRepo courseRepo;

    public FileController(FileService fileService, CourseRepo courseRepo) {
        this.fileService = fileService;
        this.courseRepo = courseRepo;
    }

    @GetMapping
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("Static files api route");
    }

    /*
    *  @RequestParam String type
    *  types: logo, avatar, file, video
    * */
    @PostMapping("/files")
    public ResponseEntity<Object> uploadFile(@RequestPart(value = "file") MultipartFile file, @RequestParam String type, @RequestParam Long courseId) {
        String fileName = this.fileService.storeFile(file, type, courseId);
        return ResponseEntity.ok("Uploaded, filename: " + fileName);
    }

    @PostMapping("/files/v2")
    public ResponseEntity<Object> uploadCommonFile(@RequestPart(value = "file") MultipartFile file) {
        String fileName = this.fileService.storeCommonFile(file);
        return ResponseEntity.ok("Uploaded, filename: " + fileName);
    }

    @GetMapping("/files/{name:.+}")
    public ResponseEntity<Resource> loadFileAsResource(@PathVariable String name) throws IOException {
        Resource resource = fileService.loadFileAsResource(name);
        String filePath = resource.getFile().getAbsolutePath();
        String mimeType = this.getFileMimeType(name);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(Files.size(Paths.get(filePath)));
        headers.setContentType(MediaType.parseMediaType(mimeType));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // throws exception when reading recently uploaded large-size image
    @GetMapping("/v1/files/{name:.+}")
    public ResponseEntity<byte[]> loadFileBytes(@PathVariable String name) throws IOException {
        String mimeType = this.getFileMimeType(name);
        byte[] file = fileService.loadFileBytes(name);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(file);
    }

    // dynamic files
    @GetMapping("/image/{id}")
    public ResponseEntity<Resource> getLogoByCourseId(@PathVariable Long id) throws IOException {
        Course course = courseRepo.findById(id).orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        String filePath = course.getImage_path() + "/" + course.getImage_name();
        String mimeType = this.getFileMimeType(course.getImage_name());

        final InputStream is = new FileInputStream(new File(filePath));
        Resource resource = new InputStreamResource(is);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(Files.size(Paths.get(filePath)));
        headers.setContentType(MediaType.parseMediaType(mimeType));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    // for downloading files
    @GetMapping("/download/{name:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String name) throws IOException {
        String mimeType = this.getFileMimeType(name);
        byte[] file = fileService.loadFileBytes(name);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
            .contentType(MediaType.parseMediaType(mimeType))
            .body(file);
    }

    private String getFileMimeType(String fileName) {
        File file = new File(fileName);
        return URLConnection.guessContentTypeFromName(file.getName());
    }

}
