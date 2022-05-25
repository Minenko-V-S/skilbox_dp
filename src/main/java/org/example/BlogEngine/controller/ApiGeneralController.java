package org.example.BlogEngine.controller;

import org.example.BlogEngine.response.InitResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// для прочих запросов к API
@RestController
public class ApiGeneralController {

    @Autowired
    private final InitResponse initResponse;

    public ApiGeneralController(InitResponse initResponse) {
        this.initResponse = initResponse;
    }

    @GetMapping("/api/init")

    private InitResponse init(){
        return initResponse;
    }
}
