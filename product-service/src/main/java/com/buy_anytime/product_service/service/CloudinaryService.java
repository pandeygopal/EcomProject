package com.buy_anytime.product_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j // This annotation provides a logger instance named 'log'
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Async
    public void uploadFile(MultipartFile file, String publicId) {
        try {
            // Read the image from the MultipartFile into a BufferedImage
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            // Resize the image, for example, to a width of 800px (maintaining aspect ratio)
            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, 800);

            // Convert the resized image into a byte[] array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", outputStream);
            byte[] resizedBytes = outputStream.toByteArray();

            // Upload the resized image to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(resizedBytes, ObjectUtils.asMap(
                    "public_id", publicId,
                    "quality", "auto:good" // Use automatic compression with good quality
            ));

            // Create a URL without the version for convenience
            String url = cloudinary.url().generate(uploadResult.get("public_id").toString());

            log.info("Uploaded file URL: {}", url);

        } catch (IOException e) {
            log.error("IO Exception during file upload", e);
        } catch (Exception e) {
            log.error("Unexpected exception during file upload", e);
        }
    }
}
