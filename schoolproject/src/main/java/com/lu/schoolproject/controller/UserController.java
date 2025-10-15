package com.lu.schoolproject.controller;

import cn.dev33.satoken.util.SaResult;
import com.lu.schoolproject.commment.Valida;
import com.lu.schoolproject.entitys.LoginUser;
import com.lu.schoolproject.entitys.User;
import com.lu.schoolproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;//业务层

    @PostMapping("login")
    public SaResult login(@RequestBody LoginUser user) {
        return userService.login(user);
    }

    @PostMapping("sava")
    public SaResult sava(@Validated(Valida.Create.class) @RequestBody User user) {
        return userService.savaUser(user);
    }
    
    @PostMapping("update")
    public SaResult updateUser(@Validated(Valida.Update.class) @RequestBody User user){
        return userService.updataUser(user);
    }


















}
