package com.lu.schoolproject.service;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lu.schoolproject.entitys.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService extends IService<Book> {

    /**
     * 保存书籍信息
     * @param files
     * @param book
     * @return
     */
    SaResult savaBook(MultipartFile[] files, Book book);

    /**
     * 查询书籍内容
     * @param id
     * @return
     */
    SaResult getBookById(Long id);
    
    SaResult selectBook(Long id);

    /**
     * 保存书籍内容
     * @param file
     * @param bookId
     * @return
     */
    SaResult savaBookContent(MultipartFile file, Long bookId);

    //2.在BookService接口中添加方法
    List<String> getImagesNames();

    SaResult updateBook(MultipartFile[] files, Book book);

    SaResult updateBookContent(MultipartFile file,Long bookId);

    SaResult deleteBook(Long id);

    SaResult fileByBookId(Long id);
}
