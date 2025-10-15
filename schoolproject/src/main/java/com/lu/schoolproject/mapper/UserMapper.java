package com.lu.schoolproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lu.schoolproject.entitys.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
