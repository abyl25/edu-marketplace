//package com.seniorproject.educationplatform.services;
//
//import com.amazonaws.AmazonServiceException;
//import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Region;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.*;
//import com.amazonaws.util.IOUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class AmazonS3Service {
//    private static final Logger logger = LoggerFactory.getLogger(AmazonS3Service.class);
//    private final AmazonS3 s3client;
//    @Value("${endpointUrl}")
//    private String endpointUrl;
//    @Value("${bucketName}")
//    private String bucketName;
//
//    @Autowired
//    public AmazonS3Service(AmazonS3 s3client) {
//        this.s3client = s3client;
//    }
//
//    public void uploadFile(MultipartFile multipartFile) {
//        String fileName1 = multipartFile.getOriginalFilename();
//        String fileUrl = "";
//        String status = "";
//        try {
//            File file = convertMultiPartToFile(multipartFile);
//            String fileName = generateFileName(multipartFile);
//            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
//            status = uploadFileTos3bucket(fileName1, file);
//            file.delete();
//        } catch (Exception e) {
//            logger.info("error [" + e.getMessage() + "] occurred while uploading [" + fileName1 + "] ");
//            e.printStackTrace();
////            return "error occurred while uploading!";
//        }
////        return fileName1 + " successfully uploaded!";
//    }
//
//    private String uploadFileTos3bucket(String fileName, File file) {
//        logger.info("LOG: uploading file to S3 bucket: " + fileName);
//        try {
//            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
//        } catch(AmazonServiceException e) {
//            return "uploadFileTos3bucket().Uploading failed :" + e.getMessage();
//        }
//        return "Uploading successful -> ";
//    }
//
//    public void deleteFileFromS3Bucket(String fileUrl) {
//        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
//        logger.info("LOG: deleting file from S3 bucket: " + fileName);
//        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
////        return "Successfully deleted";
//    }
//
//    public ResponseEntity<byte[]> downloadFile(String key) throws UnsupportedEncodingException {
//        logger.info("LOG: downloading file: " + key);
//        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
//        boolean fileExists = s3client.doesObjectExist(bucketName, key);
//        if (!fileExists) {
//            logger.info("LOG: File does not exist: " + key);
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        S3Object s3object = s3client.getObject(getObjectRequest);
//        S3ObjectInputStream objectInputStream = s3object.getObjectContent();
//        byte[] bytes = new byte[0];
//        try {
//            bytes = IOUtils.toByteArray(objectInputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String fileName = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20"); // StandardCharsets.UTF_8
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        httpHeaders.setContentLength(bytes.length);
//        httpHeaders.setContentDispositionFormData("attachment", fileName);
//
//        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
//    }
//
//    public List<S3ObjectSummary> getBucketObjects() {
//        ListObjectsV2Result result = s3client.listObjectsV2(bucketName);
//        List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
//        for (S3ObjectSummary obj: objectSummaries) {
//            System.out.println("key: " + obj.getKey());
//        }
//        return objectSummaries;
//    }
//
////    public byte[] displayImage(String key) {
////        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
////        S3Object s3object = s3client.getObject(getObjectRequest);
////        S3ObjectInputStream objectInputStream = s3object.getObjectContent();
////        byte[] bytes = new byte[0];
////        try {
////            bytes = IOUtils.toByteArray(objectInputStream);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return bytes;
////    }
//
//    private File convertMultiPartToFile(MultipartFile file) throws IOException {
//        File convFile = new File(file.getOriginalFilename());
//        FileOutputStream fos = new FileOutputStream(convFile);
//        fos.write(file.getBytes());
//        fos.close();
//        return convFile;
//    }
//
//    private String generateFileName(MultipartFile multiPart) {
//        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
//    }
//}
