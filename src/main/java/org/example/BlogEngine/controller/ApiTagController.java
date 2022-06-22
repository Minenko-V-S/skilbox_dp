package org.example.BlogEngine.controller;


import org.example.BlogEngine.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTagController {

    private final TagsService tagsService;

    public ApiTagController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping("/tag")
    private ResponseEntity<?> getTag () {
        return tagsService.getTag();
    }
}
