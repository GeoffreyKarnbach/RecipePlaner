package project.backend.service.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.dto.RecipeCreationDto;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecipeValidator {

    public void validateRecipeForCreation(RecipeCreationDto recipeDto) {

    }
}
