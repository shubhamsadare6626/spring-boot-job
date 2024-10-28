package com.shubham.springapp.controller;

import com.shubham.springapp.service.FileService;
import com.shubham.springapp.service.FileService.FileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "File API's", description = "File Op's consist upload, download and delete operations")
public class FileController {

  @Autowired private FileService fileService;

  @Operation(
      summary = "Upload a file to S3",
      description = "This endpoint allows users to upload a file to an S3 bucket.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid file input provided"),
        @ApiResponse(responseCode = "500", description = "Error occurred while uploading file")
      })
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> uploadFileToS3Bucket(
      @Parameter(description = "File to upload") @RequestParam("file") MultipartFile file) {
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

  @Operation(
      summary = "Download file from S3",
      description = "This endpoint allows users to download a file from S3 bucket.")
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

  @Operation(summary = "Delete a file from S3", description = "Delete a file from S3 bucket.")
  @DeleteMapping("/delete/{fileName}")
  public ResponseEntity<String> deleteFile(String fileName) {
    log.info("DELETE /rest/api/file/delete/{} ", fileName);
    fileService.deleteFile(fileName);
    return new ResponseEntity<>("Deleted file Successfully : " + fileName, HttpStatus.OK);
  }
}
