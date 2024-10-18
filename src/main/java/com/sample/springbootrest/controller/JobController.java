package com.sample.springbootrest.controller;

import com.sample.springbootrest.entities.JobPost;
import com.sample.springbootrest.service.JobService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/rest/api/job")
public class JobController {

  @Autowired private JobService jobService;

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
