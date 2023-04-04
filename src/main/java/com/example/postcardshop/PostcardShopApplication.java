package com.example.postcardshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class PostcardShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(PostcardShopApplication.class, args);
  }

}
