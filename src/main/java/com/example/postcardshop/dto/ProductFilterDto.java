package com.example.postcardshop.dto;

import com.example.postcardshop.data.enties.Postcard;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class ProductFilterDto implements Specification<Postcard> {

  private String vendorCode;
  private String name;
  private String author;
  private String brand;
  private String category;
  private String tags;
  private int pageNumber;

  @Override
  public Predicate toPredicate(
      Root<Postcard> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

    var predicate = builder.and();

    if (vendorCode != null && !vendorCode.isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("vendorCode"), "%" + vendorCode + "%"));
    }

    if (name != null && !name.isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("name"), "%" + name + "%"));
    }

    if (author != null && !author.isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("author"), "%" + author + "%"));
    }

    if (brand != null && !brand.isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("brand"), "%" + brand + "%"));
    }

    return predicate;
  }
}
