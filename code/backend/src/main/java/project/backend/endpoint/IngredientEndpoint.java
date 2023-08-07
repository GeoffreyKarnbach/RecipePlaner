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
import project.backend.dto.IngredientCategoryDto;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.service.IngredientService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/ingredient")
@Slf4j
@RequiredArgsConstructor
public class IngredientEndpoint {

    private final IngredientService ingredientService;

    @PermitAll
    @PostMapping
    @Operation(summary = "Creates a new ingredient")
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientDto createIngredient(@RequestBody IngredientCreationDto ingredientDto) {
        log.info("POST /api/v1/ingredient");
        log.info("ingredientDto: {}", ingredientDto);

        return this.ingredientService.createIngredient(ingredientDto);
    }

    @PermitAll
    @GetMapping(value = "/categories")
    @Operation(summary =  "Returns a list of all available ingredient categories")
    @ResponseStatus(HttpStatus.OK)
    public List<IngredientCategoryDto> getIngredientCategories() {
        log.info("GET /api/v1/ingredient/categories");

        return this.ingredientService.getIngredientCategories();
    }
}
