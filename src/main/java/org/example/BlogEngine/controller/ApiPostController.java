package org.example.BlogEngine.controller;


import org.example.BlogEngine.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;

// обрабатывает все запросы /api/post/*

@RestController
public class ApiPostController {

    private final PostService postService;

    public ApiPostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/api/post")
    public ResponseEntity<?> getPosts (@RequestParam(defaultValue="0") Integer offset,
                                       @RequestParam(defaultValue="10") Integer limit,
                                       @RequestParam String mode){
        System.out.println("Method getPosts activated.");
        return postService.getPosts (offset, limit, mode);
    }

}
