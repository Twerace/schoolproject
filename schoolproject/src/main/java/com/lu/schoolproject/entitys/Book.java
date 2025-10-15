package com.lu.schoolproject.entitys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("book")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 简介
     */
    private String introduction;
    /**
     * 日期
     */

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    /**
     * 分类
     */
    private Integer type;
    /**
     * 图片
     */
    @TableField(exist = false)
    private String image;
    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 背面图片
     */
    private String backImage;

    /**
     *上传用户id
     */
    private Long submitUser;

    /**
     * 书籍内容信息
     */
    @TableField(exist = false)
    private File fileName;
}
