package com.example.postcardshop.services;

import com.example.postcardshop.data.enties.Postcard;
import com.example.postcardshop.dto.PostcardDto;
import com.example.postcardshop.dto.ProductFilterDto;
import java.io.IOException;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PostcardService {

  Postcard save(PostcardDto postcard) throws IOException;

  Optional<Resource> loadAsResource(Long id);

  Optional<Postcard> findById(Long id);

  Page<Postcard> findPage(PageRequest pageRequest);

  Page<Postcard> findPage(ProductFilterDto filter, PageRequest pageRequest);
}
