package com.animal.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传工具类
 */
@Component
public class FileUploadUtil {

    private static String uploadPath;

    @Value("${file.upload-path}")
    public void setUploadPath(String uploadPath) {
        FileUploadUtil.uploadPath = uploadPath;
    }

    /**
     * 上传文件到服务器
     */
    public static String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("文件为空");
        }

        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isImageFile(originalFilename)) {
            throw new IOException("只支持图片格式：jpg, jpeg, png, gif");
        }

        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 生成唯一文件名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;

        // 保存文件
        Path filePath = Paths.get(uploadPath, newFileName);
        Files.copy(file.getInputStream(), filePath);

        // 返回访问路径
        return "/uploads/" + newFileName;
    }

    /**
     * 验证是否为图片文件
     */
    private static boolean isImageFile(String filename) {
        String suffix = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        return suffix.equals("jpg") || suffix.equals("jpeg") || 
               suffix.equals("png") || suffix.equals("gif");
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        if (filePath != null && filePath.startsWith("/uploads/")) {
            String fileName = filePath.substring(9);
            File file = new File(uploadPath + fileName);
            if (file.exists()) {
                file.delete();
            }
        }
    }
}

