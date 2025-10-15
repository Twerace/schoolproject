package com.lu.schoolproject.utiles;

import com.lu.schoolproject.entitys.properties.FileProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FileUtils {
    private final FileProperties fileProperties;
    /**
     * 上传文件
     *
     * @param file 上传的文件
     * @return 包含文件信息的 Map
     */
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        if (file == null || file.isEmpty()) {
            result.put("error", "文件为空");
            return result;
        }
        //1. 获取文件原始名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            result.put("error", "文件名无效");
            return result;
        }

        //2. 获取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

        //3. 生成新的文件名，避免重复
        String newFileName = UUID.randomUUID().toString() + suffix;
        //4. 拼接完整路径
        String fullPath="";
        if (suffix.equals(".jpg") || suffix.equals(".jpeg") || suffix.equals(".png")) {
            fullPath=fileProperties.getImgAddress()+newFileName;
        }else if (suffix.equals(".pdf")) {
            fullPath=fileProperties.getPdfAddress()+newFileName;
        }else{
            return  result;
        }

        File newFile = new File(fullPath);
        //5. 判断目录是否存在，不存在则创建
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }

        //6. 保存文件
        try {
            file.transferTo(newFile);
            result.put("fileName", newFileName);
            result.put("filePath", fullPath);
            result.put("size", file.getSize());
        } catch (IOException e) {
            result.put("error", "文件上传失败: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
    /**
     * 删除文件
     * @param filePath 文件完整路径，例如 "C:/file/image/64dc19a9-64d6-4eb5-875e-57b4a46050cc.jpg"
     * @return true 删除成功，false 删除失败
     */
    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            // 文件不存在
            return false;
        }

        return file.delete();
    }
    /**
     * 获取指定目录下的所有文件名
     *
     * @param dirPath 目录路径，例如 "C:/file/image/"
     * @return 文件名数组（不包含路径），如果目录不存在或不是文件夹，返回空数组
     */
    public List<String> listFileNames(String dirPath) {
        if (dirPath == null || dirPath.isEmpty()) {
            return List.of(); // Java 9+，返回空列表
        }

        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return List.of();
        }

        String[] fileArray = dir.list();
        if (fileArray == null) {
            return List.of();
        }

        // 转成 List<String>
        return Arrays.asList(fileArray);
    }
}
