package com.shubham.springapp;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootRestApplicationTests {

  @Test
  void contextLoads() {
    String s = new String();
    assertThat(s).isNotNull();
  }
}
