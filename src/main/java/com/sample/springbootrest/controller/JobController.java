package com.sample.springbootrest.controller;

import com.sample.springbootrest.entities.JobPost;
import com.sample.springbootrest.service.JobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JobController {

  @Autowired private JobService service;

  @GetMapping("jobPosts")
  public List<JobPost> getAllJobs() {
    return service.getAllJobs();
  }

  @GetMapping("/jobPost/{postId}")
  public JobPost getJob(@PathVariable int postId) {
    return service.getJob(postId);
  }

  @GetMapping("jobPosts/keyword/{keyword}")
  public List<JobPost> searchByKeyword(@PathVariable("keyword") String keyword) {
    return service.search(keyword);
  }

  @PostMapping("jobPost")
  public JobPost addJob(@RequestBody JobPost jobPost) {
    service.addJob(jobPost);
    return service.getJob(jobPost.getPostId());
  }

  @PutMapping("jobPost")
  public JobPost updateJob(@RequestBody JobPost jobPost) {
    service.updateJob(jobPost);
    return service.getJob(jobPost.getPostId());
  }

  @DeleteMapping("jobPost/{postId}")
  public String deleteJob(@PathVariable int postId) {
    service.deleteJob(postId);
    return "Deleted";
  }

  @GetMapping("load")
  public String loadData() {
    service.load();
    return "success";
  }
}
