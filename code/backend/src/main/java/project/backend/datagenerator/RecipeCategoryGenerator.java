package project.backend.datagenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.backend.entity.RecipeCategory;
import project.backend.repository.RecipeCategoryRepository;

@Profile("generateData")
@Component
@Slf4j
@RequiredArgsConstructor
public class RecipeCategoryGenerator {
    private final RecipeCategoryRepository recipeCategoryRepository;

    @PostConstruct
    private void generateData() {

        log.info("Generate recipe categories");

        if (recipeCategoryRepository.count() != 0) {
            log.info("Recipe categories already generated");
            return;
        }

        String[] recipeCategories = new String[]{
            "Suppe",
            "Vorspeise",
            "Salat",
            "Hauptspeise",
            "Dessert",
            "Getränk"
        };

        String originalPath = System.getProperty("user.dir") + "/src/main/resources/icons/recipe_categories/";

        for (int i = 0; i < recipeCategories.length; i++){
            // Copy the image to the static folder
            String imagePath = originalPath + (i + 1) + ".png";
            String targetPath = System.getProperty("user.dir") + "/src/main/resources/static/" + (i + 1) + "_rec.png";

            try {
                java.nio.file.Files.copy(java.nio.file.Paths.get(imagePath), java.nio.file.Paths.get(targetPath));
            } catch (java.io.IOException e) {
                log.warn("Could not copy image from {} to {}", imagePath, targetPath);
            }

            RecipeCategory category = RecipeCategory.builder()
                .name(recipeCategories[i])
                .iconSource("http://localhost:8080/api/v1/image/" + (i + 1) + "_rec.png")
                .build();
            recipeCategoryRepository.save(category);
        }


    }
}
