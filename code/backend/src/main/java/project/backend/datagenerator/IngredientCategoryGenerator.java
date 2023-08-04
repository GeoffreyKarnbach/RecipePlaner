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

        for (int i = 1; i <= 20; i++){
            IngredientCategory category = IngredientCategory.builder().name("Category " + i).iconSource("url").build();
            ingredientCategoryRepository.save(category);
        }

    }
}
