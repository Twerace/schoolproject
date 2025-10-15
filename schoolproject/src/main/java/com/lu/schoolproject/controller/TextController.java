package com.lu.schoolproject.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/")
@RequiredArgsConstructor
public class TextController {

    @GetMapping("hello")
    public SaResult hello() {
        return SaResult.ok("Hello World!");
    }
}