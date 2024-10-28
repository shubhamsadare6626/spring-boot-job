package com.shubham.springapp.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  @Value("${s3.bucket.name:null}")
  private String awsBucketName;

  private static final String CONTENT_TYPE = "content-type";
  @Autowired private AmazonS3 awsClient;

  /**
   * Uploads a file to AWS S3 bucket.
   *
   * @param image byte array representing the image file
   * @return the generated file name
   */
  public String uploadFileToS3(MultipartFile file) throws IOException {
    // Convert MultipartFile to byte[]
    byte[] byteFile = file.getBytes();
    Map<String, String> userMetadata = new HashMap<>();
    userMetadata.put("created-by", "shubham");
    userMetadata.put("created-at", LocalDateTime.now().toString());
    userMetadata.put(CONTENT_TYPE, file.getContentType());
    var objMetadata = new ObjectMetadata();
    objMetadata.setContentLength(byteFile.length);
    objMetadata.setUserMetadata(userMetadata);

    var fileName = UUID.randomUUID().toString();
    var putObjectRequest =
        new PutObjectRequest(
            awsBucketName, fileName, new ByteArrayInputStream(byteFile), objMetadata);
    awsClient.putObject(putObjectRequest);
    log.info("File uploaded with id: {}", fileName);
    return fileName;
  }

  /**
   * Downloads a file from AWS S3 bucket.
   *
   * @param fileName the name of the file to download
   * @return File Response
   */
  public FileResponse downloadFile(String fileName) throws IOException {
    S3Object s3Object = awsClient.getObject(awsBucketName, fileName);
    ObjectMetadata objectMetadata = s3Object.getObjectMetadata();
    String contentType = objectMetadata.getUserMetadata().get(CONTENT_TYPE);
    S3ObjectInputStream objectContent = s3Object.getObjectContent();
    return FileResponse.builder()
        .byteData(IOUtils.toByteArray(objectContent))
        .contentType(contentType)
        .build();
  }

  /**
   * Delete file.
   *
   * @param fileName the file name
   */
  public void deleteFile(String fileName) {
    try {
      awsClient.deleteObject(awsBucketName, fileName);
    } catch (Exception e) {
      throw new NotFoundException("Error while Deleting file: " + e.getMessage());
    }
    log.info("File Deleted Successfully : {}", fileName);
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FileResponse {
    private byte[] byteData;
    private String contentType;
  }
}
