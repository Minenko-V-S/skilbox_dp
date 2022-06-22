package org.example.BlogEngine.controller;

import org.example.BlogEngine.response.InitResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefaultController {

    @RequestMapping("/")
    public String index(Model model){
        return "index";
    }
}
