package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author yafei
 * @Date 2023/4/13 17:06
 * @注释
 */
public interface PdfToImageService {


    byte[] convertPDFToImage(MultipartFile file) throws IOException;
}

