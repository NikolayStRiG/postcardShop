package com.example.postcardshop.controllers;

import com.example.postcardshop.dto.PageDto;
import com.example.postcardshop.dto.ProductDto;
import com.example.postcardshop.dto.ProductFilterDto;
import com.example.postcardshop.services.ProductService;
import java.io.IOException;
import java.util.Optional;
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
  public static final String ROUTE = "route";
  public static final String INDEX = "index";
  public static final String PAGE = "page";
  public static final String FILTER = "filter";
  public static final String CATALOG = "catalog";
  private final ProductService productService;

  @GetMapping("/catalog")
  public String productCatalog(final ModelMap model) {
    var page = productService.findPage(PageRequest.of(0, 10, Direction.DESC, "createDate"));
    var dto = PageDto.of(page);
    model.put("prods", dto.getContent());
    model.addAttribute(FILTER, new ProductFilterDto());
    model.addAttribute(PAGE, dto);
    model.put(ROUTE, CATALOG);
    return INDEX;
  }

  @PostMapping("/catalog")
  public String productCatalogPost(
      @ModelAttribute(FILTER) ProductFilterDto filter, final ModelMap model) {
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
    model.addAttribute(FILTER, filter);
    model.addAttribute(PAGE, dto);
    model.put(ROUTE, CATALOG);
    return INDEX;
  }

  @GetMapping("/{id}")
  public String productDetails(@PathVariable Long id, final ModelMap model) {
    var prod = productService.findById(id);
    if (prod.isPresent()) {
      model.put(PRODUCT, prod.get());
      model.put(ROUTE, "productDetails");
    } else {
      model.put(ROUTE, CATALOG);
    }
    return INDEX;
  }

  @GetMapping("/add")
  public String addProductDetailsGet(final ModelMap model) {
    model.addAttribute(PRODUCT, new ProductDto());
    model.put(ROUTE, "productAdd");
    return INDEX;
  }

  @PostMapping("/add")
  public String addProductDetails(
      @ModelAttribute("product") ProductDto product, final ModelMap model) throws IOException {
    var result = productService.save(product);
    model.put(PRODUCT, result);
    model.put(ROUTE, "productDetails");
    return INDEX;
  }

  @GetMapping("image/{id}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable Long id) {
    var fileOp = productService.loadAsResource(id);
    if (fileOp.isPresent()) {
      var file = fileOp.get();
      return ResponseEntity.ok()
          .header(
              HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
          .body(file);
    } else {
      return ResponseEntity.of(Optional.empty());
    }
  }

  @GetMapping("image/{id}/full")
  @ResponseBody
  public ResponseEntity<Resource> getFullFile(@PathVariable Long id) {
    var fileOp = productService.loadAsResourceFull(id);
    if (fileOp.isPresent()) {
      var file = fileOp.get();
      return ResponseEntity.ok()
          .header(
              HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
          .body(file);
    } else {
      return ResponseEntity.of(Optional.empty());
    }
  }
}
