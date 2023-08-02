package project.backend.datagenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.entity.RecipeImage;
import project.backend.enums.MealType;
import project.backend.repository.RecipeCategoryRepository;
import project.backend.repository.RecipeImageRepository;
import project.backend.repository.RecipeRepository;

import java.util.List;

@Profile("generateData")
@Component
@Slf4j
@RequiredArgsConstructor
public class RecipeGenerator {

    private final RecipeRepository recipeRepository;
    private final RecipeCategoryRepository recipeCategoryRepository;

    @PostConstruct
    private void generateData() {

        RecipeCategory category = RecipeCategory.builder().name("Category 1").iconSource("SRC 1").build();
        recipeCategoryRepository.save(category);

        Recipe recipe = Recipe.builder()
            .id(1L)
            .name("Recipe 1")
            .description("Description 1")
            .mealType(MealType.LUNCH)
            .preparationTime(10)
            .difficulty(10)
            .recipeCategory(category)
            .build();

        recipeRepository.save(recipe);

    }
}
