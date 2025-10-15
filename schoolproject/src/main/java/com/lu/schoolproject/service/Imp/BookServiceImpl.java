package com.lu.schoolproject.service.Imp;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lu.schoolproject.entitys.User;
import com.lu.schoolproject.entitys.properties.FileProperties;
import com.lu.schoolproject.mapper.BookMapper;
import com.lu.schoolproject.mapper.FileMapper;
import com.lu.schoolproject.mapper.UserMapper;
import com.lu.schoolproject.service.BookService;
import com.lu.schoolproject.utiles.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Book;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    private final BookMapper bookMapper;
    private final FileUtils filesUtils;
    private final UserMapper userMapper;
    private final FileProperties fileConfigProperties;
    private final FileMapper fileMapper;

    /**
     * 保存书籍信息
     * @param files
     * @param book
     * @return
     */
    @Override
    public SaResult savaBook(MultipartFile[] files, Book book) {
        ArrayList<String> filePath=new ArrayList<>();
        for (MultipartFile file : files) {
            Map<String, Object> upload = filesUtils.upload(file);
            if(upload.get("filePath")==null) return SaResult.error("请传递图片格式为.jpg....");
            filePath.add(upload.get("filePath").toString());
        }
        book.setCoverImage(filePath.get(0));
        book.setBackImage(filePath.get(1));
        //提交用户
        String loginId = StpUtil.getLoginId().toString();
        LambdaQueryWrapper<User> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone,loginId);
        User user = userMapper.selectOne(wrapper);
        book.setSubmitUser(user.getId());
        return bookMapper.insert(book)>0?SaResult.ok("保存成功"):SaResult.error("保存失败");
    }

    /**
     * 查询书籍内容
     * @param id
     * @return
     */
    @Override
    public SaResult getBookById(Long id) {
        Book book = bookMapper.selectById(id);
        if (book==null)return SaResult.error("未查询出书籍信息");
        book.setCoverImage(book.getCoverImage().replace(fileConfigProperties.getImgAddress(),fileConfigProperties.getImgHttpAddress()));
        book.setBackImage(book.getBackImage().replace(fileConfigProperties.getImgAddress(),fileConfigProperties.getImgHttpAddress()));
        //todo书籍内容填充
        LambdaQueryWrapper<File> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(File::getBookId,book.getId());
        File file = fileMapper.selectOne(wrapper);
        book.setFileName(file);
        return SaResult.data(book);
    }

    /**
     * 保存书籍内容
     * @param file
     * @param bookId
     * @return
     */
    @Override
    public SaResult savaBookContent(MultipartFile file, Long bookId) {
        Map<String, Object> upload = filesUtils.upload(file);
        if(upload.get("filePath")==null) return SaResult.error("请传递文件格式为.pdf....");
        File file1 = File.builder().bookId(bookId).pathFileName(upload.get("filePath").toString()).build();
        return fileMapper.insert(file1)>0?SaResult.ok("保存成功"):SaResult.error("保存失败");
    }

    /**
     * 修改图片
     * @param files
     * @param book
     * @return
     */

    @Cacheable(value = "bookCache",key = "#id")  //查看
    public SaResult selectBook(Long id) {
        Book book = bookMapper.selectById(id);
        if (book==null) return SaResult.error("未查询出书籍信息");
        book.setCoverImage(book.getCoverImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
        book.setBackImage(book.getBackImage().replace(fileProperties.getImgAddress(), fileProperties.getImgHttpAddress()));
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBookId, id);
        File file = fileMapper.selectOne(queryWrapper);
        if (file!=null) book.setFileName(file.getPathFileName().replace(fileProperties.getPdfAddress(),fileProperties.getPdfHttpAddress()));
        return SaResult.data(book);
    }

    @CachePut(value = "bookCache",key = "#book.id")     //修改
    public SaResult updateBook(MultipartFile[] files, Book book) {
        Book book1 = bookMapper.selectById(book.getId());
        if(book1==null) return  SaResult.error("未查询出要修改的书籍信息");
        if (files!=null&&files[0]!=null){
            Map<String, Object> upload = filesUtils.upload(files[0]);
            if (upload.get("filePath") == null) return SaResult.error("未按照规定格式传递文件");
            book.setCoverImage(upload.get("filePath").toString());
        }
        if (files!=null&&files[1]!=null){
            Map<String, Object> upload = filesUtils.upload(files[0]);
            if (upload.get("filePath") == null) return SaResult.error("未按照规定格式传递文件");
            book.setBackImage(upload.get("filePath").toString());
        }
        int result = bookMapper.updateById(book);
        if(result > 0) {
            Book updated = bookMapper.selectById(book.getId());
            return SaResult.data(updated);
        }
        return SaResult.error("修改失败");
    }

    @Override
    @CacheEvict(value = "bookCache",key = "#id")   //删除
    public SaResult deleteBook(Long id) {
        Book book = bookMapper.selectById(id);
        if(book==null) return SaResult.error("未找到需要删除的书籍信息");
        String coverImage = book.getCoverImage();
        String backImage = book.getBackImage();
        filesUtils.deleteFile(coverImage);
        filesUtils.deleteFile(backImage);
        //查询是否有上传书籍内容信息
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBookId,book.getId());
        File file = fileMapper.selectOne(queryWrapper);
        if(file!=null){
            filesUtils.deleteFile(file.getPathFileName());
            fileMapper.delete(queryWrapper);
        }
        bookMapper.deleteById(id);
        return SaResult.ok("删除成功");
    }

    /**
     * 根据书籍id查询书籍内容
     * @param id
     * @return
     */
    @Override
    public SaResult fileByBookId(Long id) {
        LambdaQueryWrapper<File> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(File::getBookId,id);
        File file = fileMapper.selectOne(wrapper);
        return SaResult.data(file);
    }
    //1.思考，定时任务的执行时间？                       cron="0 0 1 * * ?"   秒（0-59） 分（0-59） 时（0-23） 日（1-31） 月（1-12） 星期（1-7） 年（可选）



    //3.在BookServiceImpl实现类中实现getImagesNames方法
    public List<String> getImagesNames() {
        List<String> list = new ArrayList<>();
        List<Book> books = bookMapper.selectList(null);
        for (Book book : books) {
            list.add(book.getBackImage().substring(book.getBackImage().lastIndexOf("/") + 1));
            list.add(book.getCoverImage().substring(book.getCoverImage().lastIndexOf("/") + 1));
        }
        return list;
    }

    @Override
    public SaResult updateBook(MultipartFile[] files, com.lu.schoolproject.entitys.Book book) {
        return null;
    }

    @Override
    public SaResult updateBookContent(MultipartFile file, Long bookId) {
        return null;
    }

    @Override
    public boolean saveBatch(Collection<com.lu.schoolproject.entitys.Book> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<com.lu.schoolproject.entitys.Book> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean updateBatchById(Collection<com.lu.schoolproject.entitys.Book> entityList, int batchSize) {
        return false;
    }

    @Override
    public boolean saveOrUpdate(com.lu.schoolproject.entitys.Book entity) {
        return false;
    }

    @Override
    public com.lu.schoolproject.entitys.Book getOne(Wrapper<com.lu.schoolproject.entitys.Book> queryWrapper, boolean throwEx) {
        return null;
    }

    @Override
    public Map<String, Object> getMap(Wrapper<com.lu.schoolproject.entitys.Book> queryWrapper) {
        return Map.of();
    }

    @Override
    public <V> V getObj(Wrapper<com.lu.schoolproject.entitys.Book> queryWrapper, Function<? super Object, V> mapper) {
        return null;
    }

    //4.重新编写ImgQuartzJob定时任务方法
    @RequiredArgsConstructor
    @Slf4j
    public class ImgQuartzJob implements Job {
        private final BookService bookService;
        private final FilesUtils filesUtils;
        private final FileProperties fileProperties;
        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            try {
                List<String> imagesNames = bookService.getImagesNames();
                if (imagesNames.isEmpty()) return;
                List<String> imgs = filesUtils.listFileNames(fileProperties.getImgAddress());
                if (imgs.isEmpty()) return;
                // 找出 imagesNames 中有，但 strings 中没有的
                List<String> difference = new ArrayList<>(imgs);
                difference.removeAll(imagesNames);
                for (String name : difference) {
                    filesUtils.deleteFile(fileProperties.getImgAddress()+name);
                }
            } catch (Exception e) {
                log.error(e.toString());
                throw new RuntimeException(e);
            }
        }
    }
}
