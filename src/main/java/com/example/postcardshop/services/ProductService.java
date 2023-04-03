package com.example.postcardshop.services;

import com.example.postcardshop.data.enties.Product;
import com.example.postcardshop.dto.ProductDto;
import com.example.postcardshop.dto.ProductFilterDto;
import java.io.IOException;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {

  Product save(ProductDto postcard) throws IOException;

  Optional<Resource> loadAsResource(Long id);

  Optional<Product> findById(Long id);

  Page<Product> findPage(PageRequest pageRequest);

  Page<Product> findPage(ProductFilterDto filter, PageRequest pageRequest);
}
