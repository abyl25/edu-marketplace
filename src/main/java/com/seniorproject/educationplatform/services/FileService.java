package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.exception.CustomException;
import com.seniorproject.educationplatform.exception.MyFileNotFoundException;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private AuthService authService;
    private CourseRepo courseRepo;
    private Path fileStorageLocation;

    public FileService(AuthService authService, CourseRepo courseRepo) {
        this.authService = authService; // FileStorageProperties fileStorageProperties
        this.courseRepo = courseRepo;
        this.fileStorageLocation = Paths.get("src/main/resources/static").toAbsolutePath().normalize();
        this.createDirectory(fileStorageLocation);
    }

    // Store course and user images
    public String storeFile(MultipartFile file, String type, Long courseId) {
        String userName = authService.getLoggedInUser().getUsername();
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        String courseTitle = course.getTitle();

        Path path = this.fileStorageLocation.resolve(userName).resolve(courseTitle).resolve(type);
        this.createDirectory(path);

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new CustomException("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            Path targetLocation = path.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            course.setImage_name(fileName);
            course.setImage_format(this.getFileExtension(fileName).get());
            course.setImage_path(path.toString());
            courseRepo.save(course);

            return fileName;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new CustomException("Could not store file " + fileName + ". Please try again!", HttpStatus.BAD_REQUEST);
        }
    }

    // store common files
    public String storeCommonFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new CustomException("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            throw new CustomException("Could not store file " + fileName + ". Please try again!", HttpStatus.BAD_REQUEST);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName);
        }
    }

    public Resource loadFileAsResource1(String fileName) throws IOException {
        String filePath = "/home/abylay/IdeaProjects/edu-marketplace/src/main/resources/static/" + fileName;
        String mimeType = this.getFileMimeType(fileName);
        final InputStream is = new FileInputStream(new File(filePath));
        Resource resource = new InputStreamResource(is);
        return resource;
    }

    public byte[] loadFileBytes(String fileName) {
        InputStream in = getClass().getResourceAsStream("/static/" + fileName);
        byte[] fileBytes = null;
        try {
            fileBytes = IOUtils.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new MyFileNotFoundException("File not found " + fileName);
        }
        return fileBytes;
    }

    private void createDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (Exception ex) {
            throw new CustomException("Could not create directory: " + path.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private String getFileMimeType(String fileName) {
        File file = new File(fileName);
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    private Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
