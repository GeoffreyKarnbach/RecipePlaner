package project.backend.service;

import project.backend.dto.IngredientCategoryDto;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.exception.ValidationException;

import java.util.List;

public interface IngredientService {

    /**
     * Returns all ingredient categories, that have been created so far.
     *
     * @return List of ingredient categories
     */
    List<IngredientCategoryDto> getIngredientCategories();

    /**
     * Creates a new ingredient in the persistent storage.
     * The content of the DTO is validated before the ingredient is created.
     *
     * @param ingredientDto The ingredient to be created
     * @throws ValidationException If the DTO is not valid
     * @return The created ingredient
     */
    IngredientDto createIngredient(IngredientCreationDto ingredientDto);
}
