package project.backend.datagenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.backend.entity.IngredientCategory;
import project.backend.repository.IngredientCategoryRepository;

@Profile("generateData")
@Component
@Slf4j
@RequiredArgsConstructor
public class IngredientCategoryGenerator {
    private final IngredientCategoryRepository ingredientCategoryRepository;

    @PostConstruct
    private void generateData() {

        log.info("Generate ingredient categories");

        if (ingredientCategoryRepository.count() != 0) {
            log.info("Ingredient categories already generated");
            return;
        }

        String[] ingredientCategories = new String[]{
            "Milchprodukte",
            "Fleisch",
            "Fisch",
            "Gemüse",
            "Obst",
            "Getreide",
            "Nudeln, Reis",
            "Nüsse",
            "Gewürze",
            "Süßigkeiten",
            "Backwaren",
            "Mehlspeisen"
        };

        String originalPath = System.getProperty("user.dir") + "/src/main/resources/icons/ingredient_categories/";

        for (int i = 0; i < ingredientCategories.length; i++){
            // Copy the image to the static folder
            String imagePath = originalPath + (i + 1) + ".png";
            String targetPath = System.getProperty("user.dir") + "/src/main/resources/static/" + (i + 1) + "_ing.png";

            try {
                java.nio.file.Files.copy(java.nio.file.Paths.get(imagePath), java.nio.file.Paths.get(targetPath));
            } catch (java.io.IOException e) {
                log.warn("Could not copy image from {} to {}", imagePath, targetPath);
            }

            IngredientCategory category = IngredientCategory.builder()
                .name(ingredientCategories[i])
                .iconSource("http://localhost:8080/api/v1/image/" + (i + 1) + "_ing.png")
                .build();
            ingredientCategoryRepository.save(category);
        }


    }
}
