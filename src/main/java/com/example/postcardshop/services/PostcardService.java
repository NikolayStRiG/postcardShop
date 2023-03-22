package com.example.postcardshop.services;

import com.example.postcardshop.data.enties.Postcard;
import com.example.postcardshop.dto.PostcardDto;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.Resource;

public interface PostcardService {

  Postcard save(PostcardDto postcard) throws IOException;

  Optional<Resource> loadAsResource(Long id);

  Optional<Postcard> findById(Long id);

  List<Postcard> findAll();
}
