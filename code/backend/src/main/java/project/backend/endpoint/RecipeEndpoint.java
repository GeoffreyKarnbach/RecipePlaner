package project.backend.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.backend.dto.LightRecipeDto;
import project.backend.dto.PageableDto;
import project.backend.dto.PlanedRecipeCreationDto;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.dto.RecipeFilterDto;
import project.backend.dto.RecipeIngredientListDto;
import project.backend.dto.RecipeRatingDto;
import project.backend.dto.RecipeStepsDto;
import project.backend.service.RecipeService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recipe")
@Slf4j
@RequiredArgsConstructor
public class RecipeEndpoint {

    private final RecipeService recipeService;

    @PermitAll
    @GetMapping(value = "/categories")
    @Operation(summary =  "Returns a list of all available recipe categories")
    @ResponseStatus(HttpStatus.OK)
    public List<RecipeCategoryDto> getRecipeCategories() {
        log.info("GET /api/v1/recipe/categories");

        return recipeService.getRecipeCategories();
    }

    @PermitAll
    @GetMapping(value= "/tags")
    @Operation(summary = "Returns a list of all available recipe tags")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getRecipeTags() {
        log.info("GET /api/v1/recipe/tags");

        return recipeService.getRecipeTags();
    }

    @PermitAll
    @PostMapping
    @Operation(summary = "Creates a new recipe")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@RequestBody RecipeCreationDto recipeDto) {
        log.info("POST /api/v1/recipe");

        return recipeService.createRecipe(recipeDto);
    }

    @PermitAll
    @PutMapping("/{id}")
    @Operation(summary = "Edits an existing recipe")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDto editRecipe(@PathVariable("id") Long id, @RequestBody RecipeDto recipeDto) {
        log.info("PUT /api/v1/recipe/{}", id);

        return recipeService.editRecipe(recipeDto, id);
    }

    @PermitAll
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a recipe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable("id") Long id) {
        log.info("DELETE /api/v1/recipe/{}", id);

        recipeService.deleteRecipe(id);
    }

    @PermitAll
    @GetMapping("/{id}")
    @Operation(summary = "Returns a specific recipe")
    @ResponseStatus(HttpStatus.OK)
    public RecipeDto getRecipe(@PathVariable Long id) {
        log.info("GET /api/v1/recipe/{}", id);

        return recipeService.getRecipe(id);
    }

    @PermitAll
    @GetMapping("/all")
    @Operation(summary = "Returns all recipes using pagination")
    @ResponseStatus(HttpStatus.OK)
    public PageableDto<LightRecipeDto> getRecipes(
        @PositiveOrZero
        @RequestParam("page") int page,
        @PositiveOrZero
        @RequestParam("pageSize") int pageSize
    ) {
        log.info("GET /api/v1/recipe/all");

        return this.recipeService.getRecipes(page, pageSize);
    }

    @PermitAll
    @PostMapping("/ingredient-list")
    @Operation(summary = "Updates the ingredient list of a recipe")
    @ResponseStatus(HttpStatus.OK)
    public void updateIngredientList(@RequestBody RecipeIngredientListDto recipeIngredientListDto) {
        log.info("POST /api/v1/recipe/ingredient-list");

        recipeService.updateIngredientList(recipeIngredientListDto);
    }

    @PermitAll
    @GetMapping("/{id}/ingredient-list")
    @Operation(summary = "Returns the ingredient list of a recipe")
    @ResponseStatus(HttpStatus.OK)
    public RecipeIngredientListDto getIngredientList(@PathVariable Long id) {
        log.info("GET /api/v1/recipe/ingredient-list/{}", id);

        return recipeService.getIngredientList(id);
    }

    @PermitAll
    @PostMapping("/steps")
    @Operation(summary = "Updates the steps of a recipe")
    @ResponseStatus(HttpStatus.OK)
    public void updateSteps(@RequestBody RecipeStepsDto recipeStepsDto) {
        log.info("POST /api/v1/recipe/steps");
        log.info("{}", recipeStepsDto);

        recipeService.updateSteps(recipeStepsDto);
    }

    @PermitAll
    @GetMapping("/{id}/steps")
    @Operation(summary = "Returns the steps of a recipe")
    @ResponseStatus(HttpStatus.OK)
    public RecipeStepsDto getSteps(@PathVariable Long id) {
        log.info("GET /api/v1/recipe/steps/{}", id);

        return recipeService.getSteps(id);
    }

    @PermitAll
    @PostMapping("/rating")
    @Operation(summary = "Adds a new rating to a recipe")
    @ResponseStatus(HttpStatus.OK)
    public void addRating(@RequestBody RecipeRatingDto recipeRatingDto) {
        log.info("POST /api/v1/recipe/rating");
        log.info("{}", recipeRatingDto);

        recipeService.addRating(recipeRatingDto);
    }

    @PermitAll
    @GetMapping("/{id}/ratings")
    @Operation(summary = "Returns all ratings of a recipe")
    @ResponseStatus(HttpStatus.OK)
    public PageableDto<RecipeRatingDto> getRecipeRatings(
        @PositiveOrZero
        @RequestParam("page") int page,
        @PositiveOrZero
        @RequestParam("pageSize") int pageSize,
        @PathVariable("id") Long recipeId) {
        log.info("GET /api/v1/recipe/all");

        return this.recipeService.getRatings(page, pageSize, recipeId);
    }

    @PermitAll
    @PostMapping("/filter")
    @Operation(summary = "Returns all recipes matching the given filter using pagination")
    @ResponseStatus(HttpStatus.OK)
    public PageableDto<LightRecipeDto> getFilteredIngredients(
        @PositiveOrZero
        @RequestParam("page") int page,
        @PositiveOrZero
        @RequestParam("pageSize") int pageSize,
        @RequestBody RecipeFilterDto recipeFilterDto
    ) {
        log.info("POST /api/v1/recipe/filter");
        log.info("Filter: {}", recipeFilterDto);

        return this.recipeService.getFilteredRecipes(page, pageSize, recipeFilterDto);
    }

    @PermitAll
    @PostMapping("/cook/{id}")
    @Operation(summary = "Changes ingredient inventory count as if the recipe was cooked")
    @ResponseStatus(HttpStatus.OK)
    public void cookRecipe(@PathVariable Long id, @RequestBody RecipeIngredientListDto recipeIngredientListDto) {
        log.info("POST /api/v1/recipe/cook/{}", id);

        recipeService.cookRecipe(id, recipeIngredientListDto);
    }

    @PermitAll
    @PostMapping("/plan/{id}")
    @Operation(summary = "Plans a recipe for a given date and meal")
    @ResponseStatus(HttpStatus.OK)
    public void planNewRecipe(@RequestBody PlanedRecipeCreationDto planedRecipeCreationDto, @PathVariable String id) {
        log.info("POST /api/v1/recipe/plan/{}", id);
        log.info("{}", planedRecipeCreationDto);

        //recipeService.planNewRecipe(planedRecipeCreationDto);
    }
}
