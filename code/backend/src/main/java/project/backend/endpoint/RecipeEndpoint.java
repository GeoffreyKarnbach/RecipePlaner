package project.backend.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
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
    @PostMapping
    @Operation(summary = "Creates a new recipe")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto createRecipe(@RequestBody RecipeCreationDto recipeDto) {
        log.info("POST /api/v1/recipe");

        return recipeService.createRecipe(recipeDto);
    }
}
