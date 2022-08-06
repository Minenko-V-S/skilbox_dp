package org.example.BlogEngine.controller;


import org.example.BlogEngine.service.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiTagController {

    private final TagsService tagsService;

    public ApiTagController(TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @GetMapping("/api/tag")
    private ResponseEntity<?> getTag () {
        return tagsService.getTag();
    }

    @GetMapping("/tag/{query}")
    private ResponseEntity<?> getTag (@PathVariable("query") String query) {
        return tagsService.getTag(query);
    }
}
