package com.seniorproject.educationplatform.services;

import com.seniorproject.educationplatform.components.VideoProcessingProducer;
import com.seniorproject.educationplatform.dto.file.FileRespDto;
import com.seniorproject.educationplatform.dto.file.VideoDto;
import com.seniorproject.educationplatform.exceptions.CustomException;
import com.seniorproject.educationplatform.exceptions.MyFileNotFoundException;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.models.CourseFile;
import com.seniorproject.educationplatform.models.CourseLecture;
import com.seniorproject.educationplatform.models.Video;
import com.seniorproject.educationplatform.repositories.*;
import com.seniorproject.educationplatform.security.JwtUser;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private CourseLectureRepo courseLectureRepo;
    private CourseFileRepo courseFileRepo;
    private CourseOrderRepo courseOrderRepo;
    private VideoProcessingProducer producer;
    private VideoService videoService;
    private VideoRepo videoRepo;
    private ModelMapper modelMapper;
    private Path fileStorageLocation;

    public FileService(AuthService authService, CourseRepo courseRepo, CourseLectureRepo courseLectureRepo, CourseFileRepo courseFileRepo, CourseOrderRepo courseOrderRepo, VideoProcessingProducer producer, VideoService videoService, VideoRepo videoRepo, ModelMapper modelMapper,  @Value("${storage-dir}") String dir) {
        this.authService = authService;
        this.courseRepo = courseRepo;
        this.courseLectureRepo = courseLectureRepo;
        this.courseFileRepo = courseFileRepo;
        this.courseOrderRepo = courseOrderRepo;
        this.producer = producer;
        this.videoService = videoService;
        this.videoRepo = videoRepo;
        this.modelMapper = modelMapper;
        this.fileStorageLocation = Paths.get(dir).toAbsolutePath().normalize();
        this.createDirectory(fileStorageLocation);
    }

    // Store videos, files, course and user images
    public FileRespDto storeFile(MultipartFile file, String type, Long courseId, Long lectureId) {
        logger.info("storeFile(), thread name: " + Thread.currentThread().getName());
//        String userName = authService.getLoggedInUser().getUsername();
        Course course = courseRepo.findById(courseId).orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        String permaLink = course.getPermaLink();

//        Path path = this.fileStorageLocation.resolve(userName).resolve(courseTitle).resolve(type);
        Path path = this.fileStorageLocation.resolve(permaLink + "-" + course.getId()).resolve(type);
        this.createDirectory(path);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = this.getFileExtension(originalFileName).get();
        String fileName = this.getExtensionlessFileName(originalFileName).get();

        try {
            if (originalFileName.contains("..")) {
                throw new CustomException("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
            }

            Path targetLocation = path.resolve(originalFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return this.saveFileMetaToDB(course, type, lectureId, fileName, fileExtension, path);
        } catch (IOException ex) {
            System.out.println("exception: " + ex.getMessage());
            ex.printStackTrace();
            throw new CustomException("Could not store file " + fileName + ". Please try again!", HttpStatus.BAD_REQUEST);
        }
    }

    private FileRespDto saveFileMetaToDB(Course course, String type, Long lectureId, String fileName, String extension, Path path) {
        FileRespDto fileRespDto = new FileRespDto();
        switch (type) {
            case "logo":
                course.setImageName(fileName);
                course.setImageFormat(extension);
                course.setImagePath(path.toString());
                courseRepo.save(course);
                break;
            case "avatar":
                break;
            case "files": {
                CourseLecture lecture = courseLectureRepo.findById(lectureId).orElseThrow(() -> new CustomException("Course lecture not found", HttpStatus.NOT_FOUND));
                CourseFile courseFile = new CourseFile();
                courseFile.setCourseLecture(lecture);
                courseFile.setFileName(fileName);
                courseFile.setFilePath(path.toString());
                courseFile.setFileFormat(extension);
                courseFile = courseFileRepo.save(courseFile);

                fileRespDto.setId(courseFile.getId());
                fileRespDto.setLectureId(lectureId);
                fileRespDto.setFileName(fileName);
                fileRespDto.setFileFormat(extension);
                fileRespDto.setType(type);
                break;
            }
            case "videos": {
                VideoDto videoDto = null;
                String videoName = fileName + "." + extension;
                try {
                    videoDto = videoService.getMediaInformation(videoName, path);
                    videoService.createVideoThumbnail(path, fileName, extension, "png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("videoDto: " + videoDto);

                CourseLecture lecture = courseLectureRepo.findById(lectureId).orElseThrow(() -> new CustomException("Course lecture not found", HttpStatus.NOT_FOUND));
                Video newVideo = modelMapper.map(videoDto, Video.class);
                newVideo.setName(fileName);
                newVideo.setPath(path.toString());
                newVideo.setExtension(extension);
                newVideo.setThumbnail(fileName + ".png");
                newVideo.setCourseLecture(lecture);

                Optional<Video> videoOptional = videoRepo.findById(lectureId);
                if (videoOptional.isPresent()) {
                    Video video = videoOptional.get();
                    this.deleteFileFromFS(Paths.get(video.getFullName()));
                    this.deleteFileFromFS(Paths.get(video.getPath() + "/" + video.getThumbnail()));
                    newVideo.setId(video.getId());
                    modelMapper.map(newVideo, video);
                    videoRepo.save(video);
                } else {
                    videoRepo.save(newVideo);
                }

                // set response dto
                fileRespDto.setId(newVideo.getId());
                fileRespDto.setLectureId(lectureId);
                fileRespDto.setFileName(fileName);
                fileRespDto.setFileFormat(extension);
//                fileRespDto.setVideoThumbnail(lecture.getVideoThumbnail());
                fileRespDto.setType(type);

                // send message to queue
//                VideoProcessMessage message = new VideoProcessMessage();
//                message.setVideoName(fileName);
//                message.setPath(path.toString());
//                message.setExtension(extension);
//                message.setWidth(1920);
//                message.setHeight(1080);
//                producer.produceMessage(message);
                break;
            }
        }
        return fileRespDto;
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

    public void deleteFile(Long fileId, String type) {
        switch (type) {
            case "files":
                CourseFile file = courseFileRepo.findById(fileId).orElseThrow(() -> new CustomException("Course file not found", HttpStatus.NOT_FOUND));
                String fileName = file.getFileName() + "." + file.getFileFormat();
                String fullPath = file.getFilePath() + "/" + fileName;
                Path path = Paths.get(fullPath);
                this.deleteFileFromFS(path);
                courseFileRepo.deleteById(fileId);
                break;
            case "videos":
                Video video = videoRepo.findById(fileId).orElseThrow(() -> new CustomException("Video not found", HttpStatus.NOT_FOUND));
                videoRepo.deleteById(fileId);
                this.deleteFileFromFS(Paths.get(video.getFullName()));
                this.deleteFileFromFS(Paths.get(video.getPath() + "/" + video.getThumbnail()));
                break;
        }
    }

    public Resource loadFileAsResource(String fileName, String absPath, Long courseId) {
//        boolean hasAccess = authorizeUserForFileAccess(courseId);
//        if (!hasAccess) {
//            throw new CustomException("You can't access file: " + fileName, HttpStatus.FORBIDDEN);
//        }

        try {
            Path filePath;
            if (absPath == null) {
                filePath = this.fileStorageLocation.resolve(fileName);
            } else {
                filePath = Paths.get(absPath).normalize().resolve(fileName);
            }
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
        return new InputStreamResource(is);
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
            ex.printStackTrace();
            throw new CustomException("Could not create directory: " + path.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean deleteFileFromFS (Path path) {
        boolean deleted = false;
        try {
            deleted = Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deleted;
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

    private Optional<String> getExtensionlessFileName(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(0, filename.lastIndexOf(".")));
    }

    private boolean authorizeUserForFileAccess(Long courseId) {
        JwtUser user = authService.getLoggedInUser();
        if (user == null) {
            return false;
        }
        Long userId = user.getId();
        if (courseId != null) {
            boolean studentHasAccessToFile = courseOrderRepo.existsByStudentIdAndCourseId(userId, courseId);
            if (!studentHasAccessToFile) {
                return false;
            }
        }
        return true;
    }
}
