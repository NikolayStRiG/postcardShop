package com.example.postcardshop.services;

import com.example.postcardshop.data.enties.Product;
import com.example.postcardshop.data.enties.ProductImage;
import com.example.postcardshop.data.enties.ProductType;
import com.example.postcardshop.data.repositories.ProductImageRepository;
import com.example.postcardshop.data.repositories.ProductRepository;
import com.example.postcardshop.dto.ProductDto;
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
public class ProductServiceImpl implements ProductService {

  private final ProductRepository postcardRepository;
  private final ProductImageRepository imageRepository;

  @Transactional
  @Override
  public Product save(ProductDto dto) throws IOException {
    var image = new ProductImage();
    image.setFile(dto.getFile().getBytes());
    image.setName(dto.getFile().getOriginalFilename());
    image = imageRepository.save(image);

    var product = new Product();
    product.setAuthor(dto.getAuthor());
    product.setBrand(dto.getBrand());
    product.setCategory(dto.getCategory());
    product.setDescription(dto.getDescription());
    product.setImage(image.getId().toString());
    product.setName(dto.getName());
    product.setCreateDate(ZonedDateTime.now());
    product.setTags(dto.getTags());
    product.setVendorCode(dto.getVendorCode());
    product.setPrice(dto.getPrice());
    product.setType(ProductType.POSTCARD);
    product = postcardRepository.save(product);
    log.info("Save new product with id {}", product.getId());
    return product;
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

  private ByteArrayResourceCustome compression(ProductImage postcardImage, float quality)
      throws IOException {
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
  public Optional<Product> findById(Long id) {
    return postcardRepository.findById(id);
  }

  @Override
  public Page<Product> findPage(PageRequest pageRequest) {
    return postcardRepository.findAll(
        (root, query, criteriaBuilder) -> criteriaBuilder.and(), pageRequest);
  }

  @Override
  public Page<Product> findPage(ProductFilterDto filter, PageRequest pageRequest) {
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

    @Override
    public boolean equals(Object other) {
      return super.equals(other);
    }

    @Override
    public int hashCode() {
      return super.hashCode();
    }
  }
}
