package project.backend.service;

import project.backend.dto.LightRecipeDto;
import project.backend.dto.PageableDto;
import project.backend.dto.PlanedRecipeCreationDto;
import project.backend.dto.PlanedRecipeDto;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.dto.RecipeFilterDto;
import project.backend.dto.RecipeIngredientListDto;
import project.backend.dto.RecipeRatingDto;
import project.backend.dto.RecipeStepsDto;
import project.backend.exception.ValidationException;
import project.backend.exception.NotFoundException;
import project.backend.exception.ConflictException;

import java.util.List;
import java.util.Map;

public interface RecipeService {

    /**
     * Returns all recipe categories, that have been created so far.
     *
     * @return List of recipe categories
     */
    List<RecipeCategoryDto> getRecipeCategories();

    /**
     * Returns all recipe tags, that have been created so far.
     *
     * @return List of recipe tags
     */
    List<String> getRecipeTags();

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

    /**
     * Returns all recipes, that have been created so far, using pagination.
     * The page number and the size of the page are specified by the parameters.
     *
     * @param page The page number
     * @param pageSize The size of the page
     * @return The recipes of the given page
     */
    PageableDto<LightRecipeDto> getRecipes(int page, int pageSize);

    /**
     * Deletes a recipe from the persistent storage.
     *
     * @param id The id of the recipe to be deleted
     * @throws NotFoundException If the given ID does not exist
     */
    void deleteRecipe(Long id);

    /**
     * Updates the ingredient list of a recipe.
     * The content of the DTO is validated before the ingredient list is updated.
     *
     * @param recipeIngredientListDto The new ingredient list
     */
    void updateIngredientList(RecipeIngredientListDto recipeIngredientListDto);

    /**
     * Returns the ingredient list of a recipe.
     *
     * @param recipeId The id of the recipe
     * @throws NotFoundException If the given ID does not exist
     * @return The ingredient list of the recipe
     */
    RecipeIngredientListDto getIngredientList(Long recipeId);

    /**
     * Updates the steps of a recipe.
     * The content of the DTO is validated before the steps are updated.
     *
     * @throws NotFoundException If the ID of the recipe does not exist
     * @param recipeStepsDto The new steps
     */
    void updateSteps(RecipeStepsDto recipeStepsDto);

    /**
     * Returns the steps of a recipe.
     *
     * @throws NotFoundException If the ID of the recipe does not exist
     * @param recipeId The id of the recipe
     * @return The steps of the recipe
     */
    RecipeStepsDto getSteps(Long recipeId);

    /**
     * Adds a rating to a recipe.
     * The content of the DTO is validated before the rating is added.
     *
     * @param recipeRatingDto The rating to be added
     * @throws NotFoundException If the ID of the recipe does not exist
     */
    void addRating(RecipeRatingDto recipeRatingDto);

    /**
     * Returns the ratings of a recipe in a pageable format.
     *
     * @param page The page number
     * @param pageSize The size of the page
     * @param recipeId The id of the recipe
     *
     * @return The ratings of the recipe
     */
    PageableDto<RecipeRatingDto> getRatings(int page, int pageSize, Long recipeId);

    /**
     * Returns all recipes, that match the filter criteria, using pagination.
     *
     * @param page The page number
     * @param pageSize The size of the page
     * @param recipeFilterDto The filter criteria
     *
     * @return The recipes of the given page, that match the filter criteria, using pagination
     */
    PageableDto<LightRecipeDto> getFilteredRecipes(int page, int pageSize, RecipeFilterDto recipeFilterDto);

    /**
     * Remove ingredients from inventory, as if the recipe was cooked.
     * The content of the DTO is validated before the ingredients are removed.
     *
     * @param recipeId The id of the recipe
     * @param recipeIngredientListDto The ingredient list of the recipe
     *
     * @throws NotFoundException If the ID of the recipe does not exist
     * @throws ValidationException If the DTO is not valid
     * @throws ConflictException If the needed ingredient is not in inventory
     */
    void cookRecipe(Long recipeId, RecipeIngredientListDto recipeIngredientListDto);

    /**
     * Plans a recipe for a specific date and meal.
     * The content of the DTO is validated before the recipe is planned.
     *
     * @param planedRecipeCreationDto The details of the planned recipe
     * @return The planned recipe
     */
    PlanedRecipeDto planNewRecipe(PlanedRecipeCreationDto planedRecipeCreationDto);

    /**
     * Returns all planned recipes of a specific month.
     *
     * @param year the year of the month
     * @param month the month to get the planned recipes of
     *
     * @return the planned recipes of the given month
     */
    Map<Integer, List<PlanedRecipeDto>> getPlannedRecipes(int year, int month);
}
