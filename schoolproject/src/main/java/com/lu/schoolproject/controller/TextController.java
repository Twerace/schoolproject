package com.lu.schoolproject.controller;


import ch.qos.logback.core.util.FileUtil;
import cn.dev33.satoken.util.SaResult;
import com.lu.schoolproject.utiles.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/text")
@RequiredArgsConstructor
public class TextController {
    private final FileUtils fileUtils;

    @PostMapping("/file")
        public SaResult testFile(@RequestParam("file")MultipartFile file) {
        Map<String,Object> upload = fileUtils.upload(file);
        return SaResult.ok(upload.get("fileName").toString());
    }
}
