package com.example.postcardshop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

  @GetMapping({ "/", "index", "index.html","/home"})
  public String index() {
    return "index";
  }

}
