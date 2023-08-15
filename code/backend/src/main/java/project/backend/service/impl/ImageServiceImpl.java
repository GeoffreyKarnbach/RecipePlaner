package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import project.backend.dto.RecipeImagesDto;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeImage;
import project.backend.repository.RecipeImageRepository;
import project.backend.repository.RecipeRepository;
import project.backend.service.ImageService;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final RecipeImageRepository recipeImageRepository;
    private final RecipeRepository recipeRepository;

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

    @Override
    public void removeImage(String imageSource) {
        String fileName = imageSource.substring(imageSource.lastIndexOf("/") + 1);
        try {
            java.nio.file.Files.delete(java.nio.file.Paths.get("src/main/resources/static/" + fileName));
        } catch (Exception e) {
            log.error("Error deleting image from file system: " + e.getMessage());
            throw new RuntimeException("Error deleting image from file system: " + e.getMessage());
        }
    }

    @Override
    public void uploadOrUpdateImages(RecipeImagesDto recipeImagesDto) {

        Optional<Recipe> recipeOpt = recipeRepository.findById(recipeImagesDto.getRecipeId());

        List<String> whiteListedImages = new ArrayList<>();
        for (String image : recipeImagesDto.getImages()) {
            if (image.startsWith("http://localhost:8080/api/v1/image/")){
                whiteListedImages.add(image);
            }
        }

        if (recipeOpt.isEmpty()){
            throw new RuntimeException("Recipe not found");
        }
        Recipe recipe = recipeOpt.get();

        // Delete the old images from local file system and from the database
        List<RecipeImage> recipeImages = recipeImageRepository.findRecipeImageByRecipeId(recipeImagesDto.getRecipeId());

        if (!recipeImages.isEmpty()){

            for (RecipeImage recipeImage : recipeImages) {
                if (!whiteListedImages.contains(recipeImage.getImageSource())){
                    removeImage(recipeImage.getImageSource());
                }
                recipeImageRepository.delete(recipeImage);
            }
        }

        // Upload the new images to local file system and save the image sources to the database

        for (int i = 0; i < recipeImagesDto.getImages().length; i++) {
            String url;

            if (recipeImagesDto.getImages()[i].startsWith("http://localhost:8080/api/v1/image/")){
                url = recipeImagesDto.getImages()[i];
            } else {
                url = uploadImage(recipeImagesDto.getImages()[i]);
            }

            RecipeImage recipeImage = RecipeImage.builder()
                    .imageSource(url)
                    .imagePosition(i)
                    .recipe(recipe)
                    .build();

            recipeImageRepository.save(recipeImage);
        }
    }
}
