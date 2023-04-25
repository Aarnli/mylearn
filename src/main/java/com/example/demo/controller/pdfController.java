package com.example.demo.controller;


import com.example.demo.ExcelUtil;
import com.example.demo.service.PdfToImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private ExcelUtil excelUtil;

    @PostMapping("/convertPdfToImage")
    public void convertPdfToImage(@RequestParam("file") MultipartFile pdfFile, @RequestParam("imagePath") String imagePath) throws IOException {
        excelUtil.convertPdfToImage(pdfFile, imagePath);
    }


    @PostMapping("/pdfToImage")

    public void pdfToImage(@RequestParam("file") MultipartFile pdfFile,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        pdfToImageService.pdfToImage(pdfFile, request, response);
    }
}
