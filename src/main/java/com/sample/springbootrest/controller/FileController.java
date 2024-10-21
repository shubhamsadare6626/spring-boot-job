package com.sample.springbootrest.controller;

import com.sample.springbootrest.service.FileService;
import com.sample.springbootrest.service.FileService.FileResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest/api/file")
public class FileController {

  @Autowired private FileService fileService;

  /**
   * Upload file to S3 bucket.
   *
   * @param file the file
   * @return the response entity
   */
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> uploadFileToS3Bucket(@RequestParam("file") MultipartFile file) {
    log.info("POST /rest/api/file/upload ");
    try {
      // Call the service method to upload to S3
      String fileName = fileService.uploadFileToS3(file);
      return new ResponseEntity<>("Uploaded file with id: " + fileName, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          "Error uploading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Download file.
   *
   * @param fileName the file name
   * @return the response entity
   */
  @GetMapping("/download/{fileName}")
  public ResponseEntity<?> downloadFile(@PathVariable String fileName) throws IOException {
    log.info("GET /rest/api/file/download/{} ", fileName);
    FileResponse fileResponse = fileService.downloadFile(fileName);
    ByteArrayResource resource = new ByteArrayResource(fileResponse.getByteData());
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(fileResponse.getContentType()))
        .contentLength(fileResponse.getByteData().length)
        .body(resource);
  }

  /**
   * Delete file.
   *
   * @param fileName the file name
   * @return the response String
   */
  @DeleteMapping("/delete/{fileName}")
  public ResponseEntity<String> deleteFile(String fileName) {
    log.info("DELETE /rest/api/file/delete/{} ", fileName);
    fileService.deleteFile(fileName);
    return new ResponseEntity<>("Deleted file Successfully : " + fileName, HttpStatus.OK);
  }
}
