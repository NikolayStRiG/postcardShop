package com.example.postcardshop.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostcardDto {

  private Long id;
  private String vendorCode;
  private String name;
  private String author;
  private String brand;
  private String description;
  private String category;
  private String tags;
  private MultipartFile file;
}
