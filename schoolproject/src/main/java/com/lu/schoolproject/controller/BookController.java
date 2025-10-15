package com.lu.schoolproject.controller;


import cn.dev33.satoken.util.SaResult;
import com.lu.schoolproject.entitys.Book;
import com.lu.schoolproject.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/book/")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    /**
     * 保存书籍
     *
     * @param files
     * @param book
     * @return
     */
    @PostMapping("savaBook")
    public SaResult savaBook(@RequestPart("file") MultipartFile[] files, @RequestPart("book") Book book) {
        return bookService.savaBook(files, book);
    }

    /**
     * 查询书籍内容
     *
     * @param id
     * @return
     */
    @GetMapping("getById")
    public SaResult getBookById(@RequestParam Long id) {
        return bookService.getBookById(id);
    }

    /**
     * 保存书籍内容
     */
    @PostMapping("savaBookContent")
    public SaResult savaBookContent(@RequestPart("file") MultipartFile file, @RequestPart("bookId") Long bookId) {
        return bookService.savaBookContent(file, bookId);
    }

    /**
     * 修改书籍
     *
     * @param book
     * @return
     */
    @PostMapping("updateBook")
    public SaResult updateBook(@RequestPart(value = "file", required = false) MultipartFile[] files, @RequestPart("book") Book book) {
        return bookService.updateBook(files, book);
    }

    /**
     * 修改书籍内容
     */
    @PostMapping("updateBookContent")
    public SaResult updateBookContent(@RequestPart("file") MultipartFile file, @RequestPart("bookId") Long bookId) {
        return bookService.updateBookContent(file, bookId);
    }

    /**
     * 删除书籍
     *
     * @param id
     * @return
     */
    @DeleteMapping("deleteBook")
    public SaResult deleteBook(@RequestParam Long id) {
        return bookService.deleteBook(id);
    }
}