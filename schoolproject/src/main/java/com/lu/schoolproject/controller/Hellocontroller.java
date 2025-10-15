package com.lu.schoolproject.controller;

import cn.dev33.satoken.util.SaResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello/")
@RequiredArgsConstructor
public class Hellocontroller {

    @GetMapping("world")
    public SaResult world() {
        return SaResult.ok("Hello World from Hellocontroller!");
    }
}