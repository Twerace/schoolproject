package com.lu.schoolproject.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lu.schoolproject.entitys.LoginUser;
import com.lu.schoolproject.entitys.User;

public interface UserService extends IService<User> {

    SaResult login(LoginUser user);

    SaResult selectUser(Long id);

    SaResult updataUser(User user);
    
    SaResult savaUser(User user);
}
