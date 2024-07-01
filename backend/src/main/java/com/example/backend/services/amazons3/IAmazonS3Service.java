package com.example.backend.services.amazons3;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IAmazonS3Service {

    String uploadFile(MultipartFile file) throws IOException;

    String getFileUrl(String fileName);
}
