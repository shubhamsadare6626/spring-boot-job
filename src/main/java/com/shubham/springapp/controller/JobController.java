package com.shubham.springapp.controller;

import com.shubham.springapp.entities.JobPost;
import com.shubham.springapp.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/rest/api/job")
@Tag(
    name = "Job Post API's",
    description =
        "Job Posting Create,Update,Dalete operations and Fetch with specification and pagination")
public class JobController {

  @Autowired private JobService jobService;

  @Operation(
      summary = "Add a new job posting",
      description = "This endpoint allows to create a new job posting.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "Job posting created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data provided"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PostMapping("/jobPost")
  public JobPost createJob(@RequestBody JobPost jobPost) {

    log.info("POST /jobPost - Payload: {}", jobPost);
    jobService.createJob(jobPost);
    return jobService.getJob(jobPost.getPostId());
  }

  @Operation(
      summary = "Load Data in Database",
      description = "This endpoint allows to load defined new Data.")
  @GetMapping("/load")
  public String loadData() {
    log.info("GET /load - Loading data...");
    jobService.load();
    log.info("GET /load - Data loaded...");
    return "Data loaded Success";
  }

  @Operation(
      summary = "Update an existing job posting",
      description = "This endpoint allows users to update an existing job posting.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Job posting updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data provided"),
        @ApiResponse(responseCode = "404", description = "Job posting not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @PutMapping("/jobPost")
  public JobPost updateJob(@RequestBody JobPost jobPost) {
    log.info("PUT /jobPost - Payload: {}", jobPost);
    jobService.updateJob(jobPost);
    return jobService.getJob(jobPost.getPostId());
  }

  @Operation(
      summary = "Get job posting by ID",
      description = "This endpoint retrieves the job posting details for a given job post ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Job posting retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Job posting not found")
      })
  @GetMapping("/jobPost/{postId}")
  public JobPost getJob(@PathVariable int postId) {
    log.info("GET /jobPosts/{} ", postId);
    return jobService.getJob(postId);
  }

  @GetMapping("/jobPosts/keyword/{keyword}")
  public List<JobPost> searchByKeyword(@PathVariable("keyword") String keyword) {
    log.info("GET /jobPosts/keyword/{} ", keyword);
    return jobService.search(keyword);
  }

  @Operation(summary = "List all job posts", description = "This endpoint list all job posts")
  @GetMapping("/jobPosts")
  public List<JobPost> getAllJobs() {
    log.info("GET /jobPosts ");
    return jobService.getAllJobs();
  }

  @Operation(
      summary = "Retrieve all job postings",
      description =
          "This endpoint retrieves a paginated list of all job postings sorted by specified fields.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of job postings retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @GetMapping("/all")
  public Page<JobPost> getAll(
      @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int number,
      @RequestParam(value = "pageSize", defaultValue = "5", required = false) int size,
      @RequestParam(value = "sortBy", defaultValue = "postId", required = false) String sortBy,
      @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {
    log.info(
        "GET /all number {} & size {} & sortBy {} & sortDir {}", number, size, sortBy, sortDir);
    return jobService.getAll(size, number, sortBy, sortDir);
  }

  @Operation(
      summary = "Search job postings with specifications",
      description = "This endpoint allows users to search job postings based on query parameters.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of job postings retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @GetMapping("/specification")
  public Page<JobPost> getSpecification(
      @RequestParam(value = "q", required = false) String q, Pageable pageable) {
    return jobService.getSpecification(pageable, q);
  }

  @GetMapping("/exp-greater/{exp}")
  public List<JobPost> findByExperienceGreaterThan(
      @RequestParam(value = "q", required = false) String q, @PathVariable("exp") int exp) {
    return jobService.findByExperienceGreaterThan(q, exp);
  }

  @Operation(
      summary = "Delete a job posting",
      description = "This endpoint allows users to delete a job posting by its ID.")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Job posting deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Job posting not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
      })
  @DeleteMapping("/jobPost/{postId}")
  public String deleteJob(@PathVariable int postId) {
    log.info("DELETE /jobPost/{} ", postId);
    jobService.deleteJob(postId);
    return "Deleted id : " + postId;
  }
}
