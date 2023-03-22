package com.example.postcardshop.data.enties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class PostcardImage {

  @Id @GeneratedValue private Long id;
  @Column String name;
  @Column byte[] file;
}
