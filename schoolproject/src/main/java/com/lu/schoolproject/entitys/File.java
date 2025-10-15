package com.lu.schoolproject.entitys;

import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDateTime;

public class File implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 书籍id
     */
    private Long bookId;
    /**
     *地址文件名
     */
    private String pathFileName;
    /**
     * 文件类型
     */
    private Integer type;
    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

}
