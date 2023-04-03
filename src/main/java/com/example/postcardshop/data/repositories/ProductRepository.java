package com.example.postcardshop.data.repositories;

import com.example.postcardshop.data.enties.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository
    extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product> {

  List<Product> findAll();
}
