package project.backend.service.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.ValidationErrorRestDto;
import project.backend.entity.IngredientCategory;
import project.backend.exception.ValidationException;
import project.backend.repository.IngredientCategoryRepository;
import project.backend.dto.ValidationErrorDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class IngredientValidator {

    private final IngredientCategoryRepository ingredientCategoryRepository;

    public void validateIngredientDtoForCreation(IngredientCreationDto ingredientDto) {

        List<String> validationErrors = new ArrayList<>();

        if (ingredientDto.getName() == null || ingredientDto.getName().isEmpty() || ingredientDto.getName().isBlank()) {
            validationErrors.add("Ingredient name cannot be empty");
        }

        if (ingredientDto.getImageSource() != null && (ingredientDto.getImageSource().isEmpty() || ingredientDto.getImageSource().isBlank())) {
            validationErrors.add("Ingredient image source cannot be blank");
        }

        if (ingredientDto.getUnit() == null) {
            validationErrors.add("Ingredient unit cannot be null");
        }

        if (ingredientDto.getCount() < 0) {
            validationErrors.add("Ingredient count cannot be less than 0");
        }

        Optional<IngredientCategory> ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory());
        if (ingredientCategory.isEmpty()) {
            validationErrors.add("Ingredient category with name '" + ingredientDto.getIngredientCategory() + "' does not exist");
        }

        List<ValidationErrorDto> validationErrorDtos = new ArrayList<>();

        for (int i = 0; i < validationErrors.size(); i++) {
            validationErrorDtos.add(new ValidationErrorDto((long) i, validationErrors.get(i), null));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Ingredient", validationErrorDtos));
        }


    }
}
