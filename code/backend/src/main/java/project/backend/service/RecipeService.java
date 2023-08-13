package project.backend.service;

import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.exception.ValidationException;
import project.backend.exception.NotFoundException;

import java.util.List;

public interface RecipeService {

    /**
     * Returns all recipe categories, that have been created so far.
     *
     * @return List of recipe categories
     */
    List<RecipeCategoryDto> getRecipeCategories();

    /**
     * Creates a new recipe in the persistent storage.
     * The content of the DTO is validated before the recipe is created.
     *
     * @param recipeDto The recipe to be created
     * @throws ValidationException If the DTO is not valid
     * @return The created recipe
     */
    RecipeDto createRecipe(RecipeCreationDto recipeDto);

    /**
     * Edits an existing recipe in the persistent storage.
     * The content of the DTO is validated before the recipe is edited.
     *
     * @param recipeDto The recipe to be edited with the new information
     * @param id The id of the recipe to be edited
     * @throws ValidationException If the DTO is not valid
     * @return The edited recipe
     */
    RecipeDto editRecipe(RecipeDto recipeDto, Long id);

    /**
     * Returns a specific recipe.
     *
     * @param id The id of the recipe to be returned
     * @throws NotFoundException If the given ID does not exist
     * @return The recipe with the given id
     */
    RecipeDto getRecipe(Long id);
}
