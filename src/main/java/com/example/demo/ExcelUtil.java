package com.example.demo;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author yafei
 * @Date 2023/4/25 15:39
 * @注释
 */
@Component
public class ExcelUtil {

    public static void convertPdfToImage(MultipartFile pdfFile, String imagePath) throws IOException {
        PDDocument document = PDDocument.load(pdfFile.getInputStream());
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        List<BufferedImage> bufferedImages = new ArrayList<>();
        for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(pageIndex, 300, ImageType.RGB);
            bufferedImages.add(bufferedImage);
        }
        BufferedImage stitchedImage = stitchImages(bufferedImages);
        File outputFile = new File(imagePath);
        ImageIO.write(stitchedImage, "png", outputFile);
        document.close();
    }

    private static BufferedImage stitchImages(List<BufferedImage> images) {
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
//test
    public static void main(String[] args) throws IOException {
        // 创建一个包含 PDF 文件数据的 MultipartFile 对象
        MultipartFile pdfFile = new MockMultipartFile("5页的pdf.pdf", new FileInputStream(new File("E:\\test/5页的pdf.pdf")));
        // 指定要输出的图像路径
        String imagePath = "E:\\test/image.png";
        // 调用 convertPdfToImage 方法将 PDF 文件转换为图像
        convertPdfToImage(pdfFile, imagePath);
    }
}
