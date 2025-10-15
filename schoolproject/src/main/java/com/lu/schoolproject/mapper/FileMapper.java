package com.lu.schoolproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lu.schoolproject.entitys.File;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<File> {
}