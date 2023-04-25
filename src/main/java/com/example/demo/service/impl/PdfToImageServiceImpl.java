package com.example.demo.service.impl;

import com.example.demo.service.PdfToImageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * @Author yafei
 * @Date 2023/4/13 17:06
 * @注释
 */
@Service
public class PdfToImageServiceImpl implements PdfToImageService {
    private static final String IMAGE_FORMAT = "png";
    @Override
    public byte[] convertPDFToImage(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        List<BufferedImage> bufferedImages = new ArrayList<>();
        for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
            bufferedImages.add(bufferedImage);
        }

        BufferedImage stitchedImage = stitchImages(bufferedImages);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(stitchedImage, IMAGE_FORMAT, outputStream);

        document.close();
        return outputStream.toByteArray();
    }

    @Override
    public void pdfToImage(MultipartFile pdfFile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        PDDocument document = PDDocument.load(pdfFile.getInputStream());
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        List<BufferedImage> bufferedImages = new ArrayList<>();
        for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
            bufferedImages.add(bufferedImage);
        }
        BufferedImage stitchedImage = stitchImages(bufferedImages);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(stitchedImage, "png", outputStream);
        document.close();

        response.setContentType("image/png");
        response.setContentLength(outputStream.size());
        response.setHeader("Content-Disposition", "attachment; filename=\"output.png\"");
        response.getOutputStream().write(outputStream.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }



    private BufferedImage stitchImages(List<BufferedImage> images) {
        int totalHeight = 0;
        int maxWidth = 0;
        for (BufferedImage image : images) {
            totalHeight += image.getHeight();
            maxWidth = Math.max(maxWidth, image.getWidth());
        }

        BufferedImage stitchedImage = new BufferedImage(maxWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        int y = 0;
        for (BufferedImage image : images) {
            stitchedImage.createGraphics().drawImage(image, 0, y, null);
            y += image.getHeight();
        }
        return stitchedImage;
    }
}