package com.example.postcardshop.services;

import com.example.postcardshop.data.enties.Product;
import com.example.postcardshop.data.enties.ProductImage;
import com.example.postcardshop.data.enties.ProductType;
import com.example.postcardshop.data.repositories.ProductImageRepository;
import com.example.postcardshop.data.repositories.ProductRepository;
import com.example.postcardshop.dto.ProductDto;
import com.example.postcardshop.dto.ProductFilterDto;
import jakarta.transaction.Transactional;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

  private static final int IMG_WIDTH_1000 = 1000;
  private static final int IMG_HEIGHT_668 = 668;
  private static final int IMG_WIDTH_500 = 500;
  private static final int IMG_HEIGHT_334 = 334;

  private final ProductRepository postcardRepository;
  private final ProductImageRepository imageRepository;

  @Transactional
  @Override
  public Product save(ProductDto dto) throws IOException {
    var image = new ProductImage();
    var file = dto.getFile().getBytes();
    var img = resizeFile(file, IMG_WIDTH_1000, IMG_HEIGHT_668);
    image.setFile(img);
    image.setName(dto.getFile().getOriginalFilename());
    image = imageRepository.save(image);

    var product = new Product();
    product.setAuthor(dto.getAuthor());
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

  @Cacheable({"ProductServiceImpl_loadAsResource"})
  @Override
  public Optional<Resource> loadAsResource(Long id) {
    return imageRepository
        .findById(id)
        .map(
            postcardImage -> {
              try {
                if (postcardImage.getFile().length > 32000) {
                  var result = resizeFile(postcardImage.getFile(), IMG_WIDTH_500, IMG_HEIGHT_334);
                  return new ByteArrayResourceCustome(result, postcardImage.getName());
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

  @Override
  public Optional<Resource> loadAsResourceFull(Long id) {
    return imageRepository
        .findById(id)
        .map(img -> new ByteArrayResourceCustome(img.getFile(), img.getName()));
  }

  public static byte[] resizeFile(byte[] fileToRead, int width, int height) throws IOException {
    BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(fileToRead));
    int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
    var image = resizeImage(originalImage, width, height, type);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(image, "jpg", os);
    return os.toByteArray();
  }

  private static BufferedImage resizeImage(
      BufferedImage originalImage, int width, int height, int type) {
    BufferedImage resizedImage = new BufferedImage(width, height, type);
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(originalImage, 0, 0, width, height, null);
    g.dispose();
    return resizedImage;
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
