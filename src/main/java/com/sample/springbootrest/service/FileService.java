package com.sample.springbootrest.service;

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
  public String uploadFileToS3(byte[] image, String contentType) {
    Map<String, String> userMetadata = new HashMap<>();
    userMetadata.put("created-by", "shubham");
    userMetadata.put("created-at", LocalDateTime.now().toString());
    userMetadata.put(CONTENT_TYPE, contentType);
    var metadata = new ObjectMetadata();
    metadata.setContentLength(image.length);
    metadata.setUserMetadata(userMetadata);

    var fileName = UUID.randomUUID().toString();
    var putObjectRequest =
        new PutObjectRequest(awsBucketName, fileName, new ByteArrayInputStream(image), metadata);
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
    String contentType = objectMetadata.getContentType();
    contentType = objectMetadata.getUserMetadata().get(CONTENT_TYPE);
    S3ObjectInputStream objectContent = s3Object.getObjectContent();
    return FileResponse.builder()
        .byteData(IOUtils.toByteArray(objectContent))
        .contentType(contentType)
        .build();
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
