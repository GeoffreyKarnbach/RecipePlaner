package project.backend.service;

import project.backend.dto.IngredientCategoryDto;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.dto.IngredientFilterDto;
import project.backend.dto.PageableDto;
import project.backend.exception.ValidationException;
import project.backend.exception.NotFoundException;
import project.backend.exception.ConflictException;

import java.util.List;

public interface IngredientService {

    /**
     * Returns all ingredient categories, that have been created so far.
     *
     * @return List of ingredient categories
     */
    List<IngredientCategoryDto> getIngredientCategories();


    /**
     * Returns a specific ingredient specified by the id.
     *
     * @param id The id of the ingredient to be returned
     * @return The ingredient with the given id
     */
    IngredientDto getIngredient(Long id);

    /**
     * Returns all ingredients, that have been created so far, using pagination.
     * The page number and the size of the page are specified by the parameters.
     *
     * @param page The page number
     * @param pageSize The size of the page
     * @return The ingredients of the given page
     */
    PageableDto<IngredientDto> getIngredients(int page, int pageSize);

    /**
     * Returns all ingredients, that have been created so far, using pagination.
     * Only the ingredients, that match the filter criteria, can be returned.
     *
     * @param page The page number
     * @param pageSize The size of the page
     * @param ingredientFilterDto The filter criteria
     *
     * @return The ingredients of the given page, that match the filter criteria
     */
    PageableDto<IngredientDto> getFilteredIngredients(int page, int pageSize, IngredientFilterDto ingredientFilterDto);

    /**
     * Creates a new ingredient in the persistent storage.
     * The content of the DTO is validated before the ingredient is created.
     *
     * @param ingredientDto The ingredient to be created
     * @throws ValidationException If the DTO is not valid
     * @return The created ingredient
     */
    IngredientDto createIngredient(IngredientCreationDto ingredientDto);

    /**
     * Edits an existing ingredient in the persistent storage.
     * The content of the DTO is validated before the ingredient is edited.
     *
     * @param ingredientDto The ingredient to be edited
     * @param id The id of the ingredient to be edited
     * @throws ValidationException If the DTO is not valid
     * @throws NotFoundException If the ingredient does not exist with the given id from the DTO
     * @return The edited ingredient
     */
    IngredientDto editIngredient(IngredientDto ingredientDto, Long id);

    /**
     * Deletes an existing ingredient from the persistent storage.
     * The ingredient is specified by the id.
     *
     * @param id The id of the ingredient to be deleted
     * @throws NotFoundException If the ingredient does not exist with the given id
     * @throws ConflictException If the ingredient is used in a recipe
     */
    void deleteIngredient(Long id);

    /**
     * Returns all ingredients, that have been created so far.
     * The ingredients are not paginated.
     *
     * @return List of ingredients
     */
    List<IngredientDto> getAllIngredients();


}
