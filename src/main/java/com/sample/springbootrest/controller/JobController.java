package com.sample.springbootrest.controller;

import com.sample.springbootrest.entities.JobPost;
import com.sample.springbootrest.service.JobService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/rest/api/job")
public class JobController {

  @Autowired private JobService jobService;

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

  @GetMapping("/specification")
  public Page<JobPost> getSpecification(
      @RequestParam(value = "q", required = false) String q, Pageable pageable) {
    return jobService.getSpecification(pageable, q);
  }

  @GetMapping("/exp-greater/{exp}")
  public List<JobPost> hasExpGreaterThan(
      @RequestParam(value = "q", required = false) String q, @PathVariable("exp") int exp) {
    return jobService.hasExpGreaterThan(q, exp);
  }

  @GetMapping("/jobPosts")
  public List<JobPost> getAllJobs() {
    log.info("GET /jobPosts ");
    return jobService.getAllJobs();
  }

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

  @PostMapping("/jobPost")
  public JobPost addJob(@RequestBody JobPost jobPost) {
    log.info("POST /jobPost - Payload: {}", jobPost);
    jobService.addJob(jobPost);
    return jobService.getJob(jobPost.getPostId());
  }

  @PutMapping("/jobPost")
  public JobPost updateJob(@RequestBody JobPost jobPost) {
    log.info("PUT /jobPost - Payload: {}", jobPost);
    jobService.updateJob(jobPost);
    return jobService.getJob(jobPost.getPostId());
  }

  @DeleteMapping("/jobPost/{postId}")
  public String deleteJob(@PathVariable int postId) {
    log.info("DELETE /jobPost/{} ", postId);
    jobService.deleteJob(postId);
    return "Deleted id : " + postId;
  }

  @GetMapping("/load")
  public String loadData() {
    log.info("GET /load - Loading data...");
    jobService.load();
    log.info("GET /load - Data loaded...");
    return "Data loaded Success";
  }
}
