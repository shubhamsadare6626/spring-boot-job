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

  public List<JobPost> getAllJobs() {
    return jobRepository.findAll();
  }

  public void addJob(JobPost jobPost) {
    jobRepository.save(jobPost);
  }

  public JobPost getJob(int postId) {
    return jobRepository.findById(postId).orElse(new JobPost());
  }

  public void updateJob(JobPost jobPost) {
    jobRepository.save(jobPost);
  }

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
                    List.of("UI/UX Design", "Adobe XD", "Prototyping")),
                new JobPost(
                    6,
                    "Backend Developer",
                    "Develop scalable backend services and systems.",
                    4,
                    List.of("Node.js", "Express", "MongoDB", "API")),
                new JobPost(
                    7,
                    "DevOps Engineer",
                    "Lead and grow our DevOps team.",
                    6,
                    List.of("Docker", "Kubernetes", "CI/CD", "AWS")),
                new JobPost(
                    8,
                    "Security Analyst",
                    "Ensure the security and integrity of our systems.",
                    5,
                    List.of("Penetration Testing", "Firewalls", "Encryption")),
                new JobPost(
                    9,
                    "Database Administrator",
                    "Maintain and develop high-performance database systems.",
                    3,
                    List.of("MySQL", "PostgreSQL", "Backup", "Replication")),
                new JobPost(
                    10,
                    "Mobile Developer",
                    "Lead mobile app development initiatives.",
                    4,
                    List.of("Swift", "Kotlin", "Flutter", "API"))));

    jobRepository.saveAll(jobs);
  }

  public List<JobPost> search(String keyword) {

    return jobRepository.findByPostProfileContainingOrPostDescContaining(keyword, keyword);
  }
}
