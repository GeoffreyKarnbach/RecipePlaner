package project.backend.endpoint;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.backend.dto.IngredientCategoryDto;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.dto.PageableDto;
import project.backend.service.IngredientService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/ingredient")
@Slf4j
@RequiredArgsConstructor
public class IngredientEndpoint {

    private final IngredientService ingredientService;

    @PermitAll
    @GetMapping(value = "/categories")
    @Operation(summary =  "Returns a list of all available ingredient categories")
    @ResponseStatus(HttpStatus.OK)
    public List<IngredientCategoryDto> getIngredientCategories() {
        log.info("GET /api/v1/ingredient/categories");

        return this.ingredientService.getIngredientCategories();
    }

    @PermitAll
    @GetMapping("/{id}")
    @Operation(summary = "Returns a specific ingredient")
    @ResponseStatus(HttpStatus.OK)
    public IngredientDto getIngredient(@PathVariable Long id) {
        log.info("GET /api/v1/ingredient/{}", id);

        return this.ingredientService.getIngredient(id);
    }

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
    @PutMapping("/{id}")
    @Operation(summary = "Edits an existing ingredient")
    @ResponseStatus(HttpStatus.OK)
    public IngredientDto editIngredient(@RequestBody IngredientDto ingredientDto, @PathVariable Long id) {
        log.info("PUT /api/v1/ingredient");
        log.info("ingredientDto: {}", ingredientDto);

        return this.ingredientService.editIngredient(ingredientDto, id);
    }

    @PermitAll
    @GetMapping("/all")
    @Operation(summary = "Returns all ingredients using pagination")
    @ResponseStatus(HttpStatus.OK)
    public PageableDto<IngredientDto> getIngredients(
        @PositiveOrZero
        @RequestParam("page") int page,
        @PositiveOrZero
        @RequestParam("pageSize") int pageSize
    ) {
        log.info("GET /api/v1/ingredient/all");
        log.info("page: {}", page);
        log.info("pageSize: {}", pageSize);

        return this.ingredientService.getIngredients(page, pageSize);
    }
}