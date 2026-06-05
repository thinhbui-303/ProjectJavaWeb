package com.orderseat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileStorageUtil {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path targetPath = Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
            
            // Đảm bảo thư mục tồn tại
            Files.createDirectories(targetPath.getParent());
            
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "/images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Please try again!", e);
        }
    }
}
