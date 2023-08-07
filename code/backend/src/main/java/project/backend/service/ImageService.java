package project.backend.service;

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
}
