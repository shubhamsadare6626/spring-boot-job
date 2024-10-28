package com.shubham.springapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(
            new Info()
                .title("Job Posting Management API")
                .description(
                    "API for managing job postings with CRUD operations, including experience and skills tracking. Supports file uploads and downloads with S3 integration.")
                .version("1.0.0")
                .contact(
                    new Contact()
                        .name("Java Developer")
                        .url("https://github.com/shubhamsadare6626")))
        .servers(List.of(new Server().url("http://localhost:8081").description("local")))
        .tags(List.of(new Tag().name("Job Post API's")));
  }
}
