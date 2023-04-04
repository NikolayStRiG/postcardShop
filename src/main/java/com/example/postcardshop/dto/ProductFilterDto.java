package com.example.postcardshop.dto;

import com.example.postcardshop.data.enties.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
public class ProductFilterDto implements Specification<Product> {

  private String vendorCode;
  private String name;
  private String author;
  private String category;
  private String tags;
  private int pageNumber;

  @Override
  public Predicate toPredicate(
      Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

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

    if (category != null && !category.isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("category"), "%" + category + "%"));
    }

    if (tags != null && !tags.isEmpty()) {
      predicate = builder.and(predicate, builder.like(root.get("tags"), "%" + tags + "%"));
    }

    return predicate;
  }
}
