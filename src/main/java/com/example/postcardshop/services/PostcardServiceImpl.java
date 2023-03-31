package com.example.postcardshop.services;

import com.example.postcardshop.data.enties.Postcard;
import com.example.postcardshop.data.enties.PostcardImage;
import com.example.postcardshop.data.repositories.PostcardImageRepository;
import com.example.postcardshop.data.repositories.PostcardRepository;
import com.example.postcardshop.dto.PostcardDto;
import com.example.postcardshop.dto.ProductFilterDto;
import jakarta.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.Optional;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
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
    postcard.setPrice(dto.getPrice());
    postcard = postcardRepository.save(postcard);
    log.info("Save new postcard with id {}", postcard.getId());
    return postcard;
  }

  @Override
  public Optional<Resource> loadAsResource(Long id) {
    return imageRepository
        .findById(id)
        .map(
            postcardImage -> {
              try {
                if (postcardImage.getFile().length > 512000) {
                  return compression(postcardImage, 0.1f);
                } else if (postcardImage.getFile().length > 204800) {
                  return compression(postcardImage, 0.3f);
                } else if (postcardImage.getFile().length > 102400) {
                  return compression(postcardImage, 0.5f);
                } else {
                  return new ByteArrayResourceCustome(
                      postcardImage.getFile(), postcardImage.getName());
                }

              } catch (IOException e) {
                log.error(e.getMessage(), e);
                return new ByteArrayResourceCustome(
                    postcardImage.getFile(), postcardImage.getName());
              }
            });
  }

  private ByteArrayResourceCustome compression(PostcardImage postcardImage, float quality) throws IOException {
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(postcardImage.getFile()));

    ByteArrayOutputStream os = new ByteArrayOutputStream();

    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
    ImageWriter writer = writers.next();

    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
    writer.setOutput(ios);

    ImageWriteParam param = writer.getDefaultWriteParam();

    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(quality);
    writer.write(null, new IIOImage(image, null, null), param);
    writer.dispose();
    return new ByteArrayResourceCustome(os.toByteArray(), postcardImage.getName());
  }

  @Override
  public Optional<Postcard> findById(Long id) {
    return postcardRepository.findById(id);
  }

  @Override
  public Page<Postcard> findPage(PageRequest pageRequest) {
    return postcardRepository.findAll(
        (root, query, criteriaBuilder) -> criteriaBuilder.and(), pageRequest);
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
