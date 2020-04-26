//package com.seniorproject.educationplatform.controllers;
//
//import com.amazonaws.util.IOUtils;
//import com.seniorproject.educationplatform.services.AmazonS3Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/s3/files")
//public class S3Controller {
//    private AmazonS3Service amazonS3Service;
//
//    @Autowired
//    public S3Controller(AmazonS3Service amazonS3Service) {
//        this.amazonS3Service = amazonS3Service;
//    }
//
//    @GetMapping
//    public String index() {
//        return "Hi. /files route";
//    }
//
//    @PostMapping("/upload")
//    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
//        this.amazonS3Service.uploadFile(file);
//        return "successfully uploaded!";
//    }
//
//    @PostMapping("/uploadMultipleFiles")
//    public List uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        return Arrays.asList(files)
//                .stream()
//                .map(file -> uploadFile(file))
//                .collect(Collectors.toList());
//    }
//
//    @DeleteMapping
//    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
//        this.amazonS3Service.deleteFileFromS3Bucket(fileUrl);
//        return "Successfully deleted!";
//    }
//
//    @GetMapping(value = "/all")
//    public List getFiles() {
//        return this.amazonS3Service.getBucketObjects();
//    }
//
//    @GetMapping(value = "/{key}")
//    public ResponseEntity<byte[]> getFile1(@PathVariable String key) throws UnsupportedEncodingException {
//        return this.amazonS3Service.downloadFile(key);
//    }
//
////    @GetMapping(value = "/image/{key}")
////    public byte[] getImage(@PathVariable String key) {
////        return this.amazonS3Service.displayImage(key);
////    }
//
//    // returns image
//    @GetMapping(value = "/download")  // produces = MediaType.IMAGE_PNG_VALUE
//    public byte[] getFile() {  // @PathVariable String fileName
//        InputStream in = getClass().getResourceAsStream("/static/spring-boot.png");
//        byte[] b = null;
//        try {
//            b = IOUtils.toByteArray(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return b;
//    }
//}
