package project.backend.service.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.dto.ValidationErrorRestDto;
import project.backend.entity.Ingredient;
import project.backend.entity.IngredientCategory;
import project.backend.exception.NotFoundException;
import project.backend.exception.ValidationException;
import project.backend.mapper.IngredientMapper;
import project.backend.repository.IngredientCategoryRepository;
import project.backend.dto.ValidationErrorDto;
import project.backend.repository.IngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class IngredientValidator {

    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final IngredientRepository ingredientRepository;
    private final IngredientMapper ingredientMapper;

    public void validateIngredientDtoForCreation(IngredientCreationDto ingredientDto) {

        List<String> validationErrors = new ArrayList<>();

        if (ingredientDto.getName() == null || ingredientDto.getName().isEmpty() || ingredientDto.getName().isBlank()) {
            validationErrors.add("Ingredient name cannot be empty");
        }

        if (ingredientDto.getImageSource() != null && (ingredientDto.getImageSource().isEmpty() || ingredientDto.getImageSource().isBlank())) {
            validationErrors.add("Ingredient image source cannot be blank");
        }

        if (ingredientDto.getImageSource() != null &&
            !ingredientDto.getImageSource().startsWith("data:image/") &&
            !ingredientDto.getImageSource().startsWith("http://localhost:8080/api/v1/image/") // In case the image is already uploaded
        ) {
            validationErrors.add("Ingredient image source must be a base64 encoded image");
        } else if (ingredientDto.getImageSource() != null &&
            !ingredientDto.getImageSource().startsWith("http://localhost:8080/api/v1/image/")) {
            // Check if image is valid Base64
            try {
                String base64Image = ingredientDto.getImageSource().split(",")[1];
                byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Image);
            } catch (Exception e) {
                validationErrors.add("Ingredient image source must be a valid base64 encoded image");
            }
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

    public void validateIngredientDtoForEdit(IngredientDto ingredientDto, Long id){

        if (ingredientDto.getId() == null) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Ingredient", List.of(new ValidationErrorDto(0L, "Ingredient id cannot be null", null))));
        }

        if (!ingredientDto.getId().equals(id)) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Ingredient", List.of(new ValidationErrorDto(0L, "Ingredient id in path and body do not match", null))));
        }

        Optional<Ingredient> ingredient = ingredientRepository.findById(ingredientDto.getId());

        if (ingredient.isEmpty()) {
            throw new NotFoundException("Ingredient with id '" + ingredientDto.getId() + "' does not exist");
        }

        IngredientCreationDto dtoToValidate = ingredientMapper.mapIngredientDtoToIngredientCreationDto(ingredientDto);
        this.validateIngredientDtoForCreation(dtoToValidate);
    }
}
