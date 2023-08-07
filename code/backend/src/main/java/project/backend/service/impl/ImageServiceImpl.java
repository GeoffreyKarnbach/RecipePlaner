package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import project.backend.service.ImageService;

import java.io.OutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Override
    public String uploadImage(String imageSource) {
        String fileExtension = imageSource.substring(11, imageSource.indexOf(";"));
        String fileName = java.util.UUID.randomUUID().toString() + "." + fileExtension;

        // Generate the URL of the image to be saved in the database
        String url = "http://localhost:8080/api/v1/image/" + fileName;

        byte[] imageBytes = Base64.decodeBase64(imageSource.substring(imageSource.indexOf(",") + 1));
        try (OutputStream stream = new java.io.FileOutputStream("src/main/resources/static/" + fileName)) {
            stream.write(imageBytes);
        } catch (Exception e) {
            log.error("Error saving image to file system: " + e.getMessage());
            throw new RuntimeException("Error saving image to file system: " + e.getMessage());
        }

        return url;
    }
}
