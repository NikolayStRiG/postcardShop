package com.example.postcardshop.controllers;

import com.example.postcardshop.data.enties.Postcard;
import com.example.postcardshop.services.PostcardService;
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

  private final PostcardService postcardService;

  @GetMapping({"/", "index", "index.html", "/home"})
  public String index(final ModelMap model) {

    List<Postcard> prods =
        postcardService.findPage(PageRequest.of(0, 10, Direction.DESC, "createDate")).getContent();
    model.put("prods", prods);
    return "index";
  }
}
