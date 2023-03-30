package com.example.postcardshop.services;

import com.example.postcardshop.data.enties.Postcard;
import com.example.postcardshop.data.enties.PostcardImage;
import com.example.postcardshop.data.repositories.PostcardImageRepository;
import com.example.postcardshop.data.repositories.PostcardRepository;
import com.example.postcardshop.dto.PostcardDto;
import com.example.postcardshop.dto.ProductFilterDto;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostcardServiceImpl implements PostcardService {

  private final PostcardRepository postcardRepository;
  private final PostcardImageRepository imageRepository;

  @Transactional
  @Override
  public Postcard save(PostcardDto dto) throws IOException {
    var image = new PostcardImage();
    image.setFile(dto.getFile().getBytes());
    image.setName(dto.getFile().getOriginalFilename());
    image = imageRepository.save(image);

    var postcard = new Postcard();
    postcard.setAuthor(dto.getAuthor());
    postcard.setBrand(dto.getBrand());
    postcard.setCategory(dto.getCategory());
    postcard.setDescription(dto.getDescription());
    postcard.setImage(image.getId().toString());
    postcard.setName(dto.getName());
    postcard.setCreateDate(ZonedDateTime.now());
    postcard.setTags(dto.getTags());
    postcard.setVendorCode(dto.getVendorCode());
    postcard = postcardRepository.save(postcard);
    log.info("Save new postcard with id {}", postcard.getId());
    return postcard;
  }

  @Override
  public Optional<Resource> loadAsResource(Long id) {
    return imageRepository
        .findById(id)
        .map(
            postcardImage ->
                new ByteArrayResourceCustome(postcardImage.getFile(), postcardImage.getName()));
  }

  @Override
  public Optional<Postcard> findById(Long id) {
    return postcardRepository.findById(id);
  }

  @Override
  public Page<Postcard> findPage(PageRequest pageRequest) {
    return postcardRepository.findAll((root,query,criteriaBuilder) -> criteriaBuilder.and(), pageRequest);
  }

  @Override
  public Page<Postcard> findPage(ProductFilterDto filter, PageRequest pageRequest) {
    return postcardRepository.findAll(filter, pageRequest);
  }

  public static class ByteArrayResourceCustome extends ByteArrayResource {

    private final String name;

    public ByteArrayResourceCustome(byte[] byteArray, String name) {
      super(byteArray);
      this.name = name;
    }

    public ByteArrayResourceCustome(byte[] byteArray, String name, String description) {
      super(byteArray, description);
      this.name = name;
    }

    @Override
    public String getFilename() {
      return this.name;
    }
  }
}
