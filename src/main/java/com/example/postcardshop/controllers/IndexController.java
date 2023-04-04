package com.example.postcardshop.controllers;

import com.example.postcardshop.data.enties.Product;
import com.example.postcardshop.services.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

  private final ProductService postcardService;

  @GetMapping({"/", "index", "index.html", "/home"})
  public String index(final ModelMap model) {

    List<Product> prods =
        postcardService.findPage(PageRequest.of(0, 10, Direction.DESC, "createDate")).getContent();
    model.put("prods", prods);
    model.put("route", "homePage");
    return "index";
  }
}
