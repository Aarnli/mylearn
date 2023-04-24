package com.example.demo.controller;


import com.example.demo.service.PdfToImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author yafei
 * @Date 2023/4/13 17:02
 * @注释
 */
@RestController
@RequestMapping("/pdf")
public class pdfController {
    @Autowired
    private PdfToImageService pdfToImageService;

    @PostMapping(value = "/pdf-to-image", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> convertPDFToImage(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] imageBytes = pdfToImageService.convertPDFToImage(file);
        return ResponseEntity.ok().body(imageBytes);
    }
}
