package project.backend.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.backend.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping(value = "/api/v1/image")
@Slf4j
@RequiredArgsConstructor
public class ImageEndpoint {

    private final String resourcePaths = System.getProperty("user.dir") + "/src/main/resources/static/";

    @PermitAll
    @GetMapping("/{imageId}")
    @Operation(summary = "Returns the image with the given id")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> getImage(@PathVariable String imageId)  {
        log.info("GET /api/v1/image/{}", imageId);

        String imagePath = resourcePaths + imageId;

        InputStream content;

        try {
            content = new java.io.FileInputStream(imagePath);
        } catch (IOException e) {
            throw new NotFoundException("Could not find image with name " + imageId);
        }

        String fileEnding = imageId.substring(imageId.lastIndexOf(".") + 1);
        MediaType mediaType = MediaType.IMAGE_PNG;

        if (fileEnding.equals("jpg") || fileEnding.equals("jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else if (fileEnding.equals("gif")) {
            mediaType = MediaType.IMAGE_GIF;
        } else if (!fileEnding.equals("png")) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
            .contentType(mediaType)
            .body(new InputStreamResource(content));
    }

}
