package com.seniorproject.educationplatform.controllers;

import com.seniorproject.educationplatform.exceptions.CustomException;
import com.seniorproject.educationplatform.models.Course;
import com.seniorproject.educationplatform.repositories.CourseRepo;
import com.seniorproject.educationplatform.services.MultipartFileSender;
import com.seniorproject.educationplatform.services.VideoService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/static")
public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    private CourseRepo courseRepo;
    private VideoService videoService;

    public VideoController(CourseRepo courseRepo, VideoService videoService) {
        this.courseRepo = courseRepo;
        this.videoService = videoService;
    }

    @GetMapping("/video/process")
    public void encodeVideo() throws IOException {
//        videoService.processVideo("lesson1.mp4");
    }

    @GetMapping("/video/info")
    public void getVideoMetaInfo() throws IOException {
        videoService.getMediaInformation("lesson1.mp4");
    }

    @GetMapping("/video/thumbnail")
    public void createVideoThumbnail() throws IOException {
//        videoService.createVideoThumbnail("lesson1");
    }

    @PostMapping("/video")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        OutputStream os = new FileOutputStream(new File("videos", file.getOriginalFilename()));
        readAndWrite(file.getInputStream(), os);
    }

    @GetMapping("/video/sample")
    public void serveVideo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = "/home/abylay/IdeaProjects/edu-marketplace/src/main/resources/static/lesson1.mp4";
        MultipartFileSender.fromPath(Paths.get(path))
                .with(request)
                .with(response)
                .serveResource();
    }

    // serves video by byte range
    @GetMapping("/video/{name}")
    public void serveVideo(@PathVariable String name, @RequestParam Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Course course = courseRepo.findById(id).orElseThrow(() -> new CustomException("Course not found", HttpStatus.NOT_FOUND));
        String username = course.getInstructor().getUserName();
        String courseTitle = course.getTitle();
        String path = "/home/abylay/IdeaProjects/edu-marketplace/src/main/resources/static/" + username + "/" + courseTitle + "/videos/" + name + ".mp4";
        MultipartFileSender.fromPath(Paths.get(path))
                .with(request)
                .with(response)
                .serveResource();
    }

    @GetMapping("/video1/{name:.+}")
    public ResponseEntity<byte[]> getVideo(@PathVariable String name) throws IOException {
        InputStream in = getClass().getResourceAsStream("/static/" + name);
        byte[] image = null;
        try {
            image = IOUtils.toByteArray(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(image);
    }

    @GetMapping("/video2/{videoName:.+}")
    public StreamingResponseBody getVideo1(@PathVariable String videoName) throws IOException {
//        File videoFile = new File(videoName);
//        final InputStream videoFileStream = new FileInputStream(videoFile);
        final InputStream videoFileStream = getClass().getResourceAsStream("/static/" + videoName);
        return (os) -> {
            readAndWrite(videoFileStream, os);
        };
    }

    private void readAndWrite(final InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[2048];
        int read = 0;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }

}
