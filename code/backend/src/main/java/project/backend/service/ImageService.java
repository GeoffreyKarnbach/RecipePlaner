package project.backend.service;

import project.backend.dto.RecipeImagesDto;

public interface ImageService {

    /**
     * Uploads an image to the persistent storage and the name of the image.
     * @param imageSource The image to be uploaded in Base64 format
     * @return The name of the image
     */
    String uploadImage(String imageSource);

    /**
     * Removes an image from the persistent storage.
     * @param imageSource The name of the image to be removed (filename)
     */
    void removeImage(String imageSource);

    /**
     * Uploads or updates the images of a recipe.
     * If the recipe already has images, the old images will be removed and the new images will be uploaded.
     * If the recipe has no images, the new images will be uploaded.
     *
     * @param recipeImagesDto The DTO, containing the recipe id and the images
     */
    void uploadOrUpdateImages(RecipeImagesDto recipeImagesDto);
}
