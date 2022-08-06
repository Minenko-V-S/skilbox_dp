package org.example.BlogEngine.controller;


import org.example.BlogEngine.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/api/post/search")
    public ResponseEntity<?> getPostsBySearch (@RequestParam(defaultValue="0") Integer offset,
                                               @RequestParam(defaultValue="5") Integer limit,
                                               @RequestParam String query) {
        System.out.println("Method getPostsBySearch activated");
        return postService.getPostsBySearch(offset, limit, query);
    }

    @GetMapping("/api/post/byDate")
    public ResponseEntity<?> getPostsByDate (@RequestParam(defaultValue="0") Integer offset,
                                             @RequestParam(defaultValue="5") Integer limit,
                                             @RequestParam  String date){
        System.out.println("Method getPostsByDate activated by the date: " + date );
        return postService.getPostsByDate(offset, limit, date);
    }

    @GetMapping("/api/post/byTag")
    public ResponseEntity<?> getPostsByTag(@RequestParam(defaultValue="0") Integer offset,
                                           @RequestParam(defaultValue="12") Integer limit,
                                           @RequestParam String tag){
        System.out.println("Method getPostsByTag uses tag name:" + tag);
        return postService.getPostsByTag(offset, limit, tag);
    }

    @GetMapping("/api/post/{ID:\\d+}")
    public ResponseEntity<?> getPostById (@PathVariable("ID") Integer ID) {
        System.out.println("Method getPostById activated. ID requested: " + ID);
        return postService.getPostById(ID);
    }

}
