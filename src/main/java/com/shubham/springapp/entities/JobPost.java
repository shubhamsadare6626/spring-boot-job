package com.shubham.springapp.entities;

import com.shubham.springapp.base.AuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_posts")
@EntityListeners(AuditingEntityListener.class)
public class JobPost extends AuditEntity<String> {

  @Id
  @Column(name = "post_id")
  private int postId;

  @Column(name = "post_profile")
  private String postProfile;

  @Column(name = "post_desc")
  private String postDesc;

  @Column(name = "req_experience")
  private Integer reqExperience;

  private List<String> postTechStack;
}
