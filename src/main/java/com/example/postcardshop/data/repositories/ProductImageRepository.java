package com.example.postcardshop.data.repositories;

import com.example.postcardshop.data.enties.ProductImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends CrudRepository<ProductImage, Long> {}
