package com.example.postcardshop.controllers;

import com.example.postcardshop.dto.PageDto;
import com.example.postcardshop.dto.ProductDto;
import com.example.postcardshop.dto.ProductFilterDto;
import com.example.postcardshop.services.ProductService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@RequestMapping("product")
@Controller
public class ProductController {

  public static final String PRODUCT = "product";
  private final ProductService productService;

  @GetMapping("/catalog")
  public String productCatalog(final ModelMap model) {
    var page = productService.findPage(PageRequest.of(0, 10, Direction.DESC, "createDate"));
    var dto = PageDto.of(page);
    model.put("prods", dto.getContent());
    model.addAttribute("filter", new ProductFilterDto());
    model.addAttribute("page", dto);
    return "product-catalog";
  }

  @PostMapping("/catalog")
  public String productCatalogPost(@ModelAttribute("filter") ProductFilterDto filter, final ModelMap model) {
    var page =
        productService.findPage(
            filter,
            PageRequest.of(
                filter.getPageNumber() == 0 ? 0 : filter.getPageNumber() - 1,
                10,
                Direction.DESC,
                "createDate"));
    var dto = PageDto.of(page);
    model.put("prods", dto.getContent());
    model.addAttribute("filter", filter);
    model.addAttribute("page", dto);
    return "product-catalog";
  }

  @GetMapping("/{id}")
  public String productDetails(@PathVariable Long id, final ModelMap model) {
    model.put(PRODUCT, productService.findById(id).get());
    return "product-details";
  }

  @GetMapping("/add")
  public String addProductDetailsGet(final ModelMap model) {
    model.addAttribute(PRODUCT, new ProductDto());
    return "product-add";
  }

  @PostMapping("/add")
  public String addProductDetails(
      @ModelAttribute("product") ProductDto product, final ModelMap model)throws IOException {
    var result = productService.save(product);
    model.put(PRODUCT, result);
    return "product-details";
  }

  @GetMapping("image/{id}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable Long id) {
    var file = productService.loadAsResource(id).get();
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }
}
