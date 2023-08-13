package project.backend.service.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.ValidationErrorDto;
import project.backend.dto.ValidationErrorRestDto;
import project.backend.entity.RecipeCategory;
import project.backend.exception.ValidationException;
import project.backend.repository.RecipeCategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecipeValidator {

    private final RecipeCategoryRepository recipeCategoryRepository;

    public void validateRecipeForCreation(RecipeCreationDto recipeDto) {

        List<String> validationErrors = new ArrayList<>();

        if (recipeDto.getName() == null || recipeDto.getName().isEmpty() || recipeDto.getName().isBlank()) {
            validationErrors.add("Recipe name cannot be empty");
        }

        if (recipeDto.getDescription() == null || recipeDto.getDescription().isEmpty() || recipeDto.getDescription().isBlank()) {
            validationErrors.add("Recipe description cannot be empty");
        }

        if (recipeDto.getDifficulty() < 1 || recipeDto.getDifficulty() > 5) {
            validationErrors.add("Recipe difficulty must be between 1 and 5");
        }

        if (recipeDto.getPreparationTime() < 1) {
            validationErrors.add("Recipe preparation time must be greater than 0");
        }

        Optional<RecipeCategory> recipeCategory = recipeCategoryRepository.findRecipeCategoryByName(recipeDto.getRecipeCategory());
        if (recipeCategory.isEmpty()) {
            validationErrors.add("Recipe category does not exist");
        }

        List<ValidationErrorDto> validationErrorDtos = new ArrayList<>();

        for (int i = 0; i < validationErrors.size(); i++) {
            validationErrorDtos.add(new ValidationErrorDto((long) i, validationErrors.get(i), null));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Rezepten", validationErrorDtos));
        }
    }
}
