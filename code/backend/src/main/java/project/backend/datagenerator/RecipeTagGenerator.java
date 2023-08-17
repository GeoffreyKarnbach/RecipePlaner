package project.backend.datagenerator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import project.backend.repository.RecipeTagRepository;

import java.util.List;

@Profile("generateData")
@Component
@Slf4j
@RequiredArgsConstructor
public class RecipeTagGenerator {

    private final RecipeTagRepository recipeTagRepository;

    @PostConstruct
    private void generateData() {
        if (recipeTagRepository.count() != 0) {
            log.info("Recipe tags already generated");
            return;
        }

        List<String> recipeTags = List.of(
            "vegetarisch",
            "vegan",
            "low carb",
            "low fat",
            "fleisch",
            "fisch",
            "mexikanisch",
            "italienisch",
            "asiatisch",
            "indisch",
            "französisch",
            "spanisch",
            "österreichisch",
            "low calory",
            "high calory"
        );

        log.info("Generate recipe tags");

        for (String recipeTag : recipeTags) {
            project.backend.entity.RecipeTag tag = project.backend.entity.RecipeTag.builder()
                .name(recipeTag)
                .build();
            recipeTagRepository.save(tag);
        }
    }
}
