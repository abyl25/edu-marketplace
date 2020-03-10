package com.seniorproject.educationplatform.controllers;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;

@RestController
@RequestMapping("/api/static")
public class VideoController {

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
        OutputStream os = new FileOutputStream(new File("videos", file.getOriginalFilename()));
        readAndWrite(file.getInputStream(), os);
    }

    // works
    @GetMapping("/video/{name:.+}")
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

    @GetMapping("/video1/{videoName:.+}")
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
