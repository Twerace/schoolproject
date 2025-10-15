package com.lu.schoolproject;

import com.lu.schoolproject.entitys.User;
import com.lu.schoolproject.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SchoolprojectApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        User build = User.builder().phone("17323007818").name("xx").password("123456").remark("备注").status(0).build();
        userMapper.insert(build);
    }

}
