package com.example.postcardshop.controllers;

import com.example.postcardshop.data.ItemDto;
import com.example.postcardshop.dto.PostcardDto;
import com.example.postcardshop.services.PostcardService;
import java.io.IOException;import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
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
@RequestMapping("product-details")
@Controller
public class ProductDetailsController {

  private final PostcardService postcardService;

  @GetMapping("/{id}")
  public String productDetails(@PathVariable Long id, final ModelMap model) {
    model.put("product", postcardService.findById(id).get());
    model.put("route", "postcard_info");
    return "product-details";
  }

  @GetMapping("/{id}/fragment")
  public String productDetailsFragment(@PathVariable Long id, final ModelMap model) {
    model.put("product", postcardService.findById(id).get());
    return "products::postcard_info";
  }

  @GetMapping("/add-postcard")
  public String addProductDetailsGet(final ModelMap model) {
    model.addAttribute("product", new PostcardDto());
    model.put("route", "add_postcard");
    return "product-add";
  }

  @GetMapping(value = "/add-postcard/fragment")
  public String addProductDetailsGetFragment(final ModelMap model) {
    model.addAttribute("product", new PostcardDto());
    return "products::add_postcard";
  }

  @PostMapping("/add-postcard")
  public String addProductDetails(
      @ModelAttribute("product") PostcardDto postcard, final ModelMap model)throws IOException {

    var result = postcardService.save(postcard);
    model.put("product", result);
    model.put("route", "postcard_info");
    return "product-details";
  }

  @GetMapping("image/{id}")
  @ResponseBody
  public ResponseEntity<Resource> getFile(@PathVariable Long id) {
    var file = postcardService.loadAsResource(id).get();
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
        .body(file);
  }
}
