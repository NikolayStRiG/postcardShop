package com.example.postcardshop.data.enties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Product {

  @Id @GeneratedValue private Long id;

  @Column private String vendorCode;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductType type;

  @Column private String author;

  @Column private String brand;

  @Column private String description;

  @Column private String image;

  @Column private String category;

  @Column private String tags;

  @Column private ZonedDateTime createDate;

  @Column private Integer price;
}
