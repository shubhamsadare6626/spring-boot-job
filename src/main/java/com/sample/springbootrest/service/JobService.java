package com.sample.springbootrest.service;

import com.sample.springbootrest.entities.JobPost;
import com.sample.springbootrest.repositories.JobRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobService {

  @Autowired public JobRepository jobRepository;

  // method to return all JobPosts
  public List<JobPost> getAllJobs() {
    return jobRepository.findAll();
  }

  // method to add a jobPost
  public void addJob(JobPost jobPost) {
    jobRepository.save(jobPost);
  }

  // method to get job by id
  public JobPost getJob(int postId) {
    return jobRepository.findById(postId).orElse(new JobPost());
  }

  // method to update job with job post object
  public void updateJob(JobPost jobPost) {
    jobRepository.save(jobPost);
  }

  // method to delete job post by id
  public void deleteJob(int postId) {
    jobRepository.deleteById(postId);
  }

  public void load() {
    // arrayList to store store JobPost objects
    List<JobPost> jobs =
        new ArrayList<>(
            List.of(
                new JobPost(
                    1,
                    "Software Engineer",
                    "Exciting opportunity for a skilled software engineer.",
                    3,
                    List.of("Java", "Spring", "SQL", "API")),
                new JobPost(
                    2,
                    "Data Scientist",
                    "Join our data science team and work on cutting-edge projects.",
                    5,
                    List.of("Python", "Machine Learning", "TensorFlow", "API")),
                new JobPost(
                    3,
                    "Frontend Developer",
                    "Create API amazing user interfaces with our talented frontend team.",
                    2,
                    List.of("JavaScript", "React", "CSS", "API")),
                new JobPost(
                    4,
                    "Network Engineer",
                    "Design and API maintain our robust network infrastructure.",
                    4,
                    List.of("Cisco", "Routing", "Firewalls")),
                new JobPost(
                    5,
                    "UX Designer",
                    "Shape the user experience with your creative design skills.",
                    3,
                    List.of("UI/UX Design", "Adobe XD", "Prototyping"))));

    jobRepository.saveAll(jobs);
  }

  public List<JobPost> search(String keyword) {

    return jobRepository.findByPostProfileContainingOrPostDescContaining(keyword, keyword);
  }
}
