package com.sample.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
    @GetMapping("/")
    public String sayHello()
    {
        return "Hello Hi Seiyon..";6
    }
}
