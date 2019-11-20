package com.kurly.poc.ssedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SsedemoApplication {

  public static void main(String[] args) {
    SpringApplication.run(SsedemoApplication.class, args);
  }

}
