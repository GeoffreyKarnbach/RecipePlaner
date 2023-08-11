package project.backend.repository;

import org.springframework.data.domain.Page;
import project.backend.dto.IngredientFilterDto;
import project.backend.entity.Ingredient;
import org.springframework.data.domain.Pageable;

public interface IngredientRepositoryCustom {

    /**
     * Find all ingredients filtered by ingredientFilterDto in the persistent storage
     *
     * @param pageable Pageable, with page number and page size
     * @param ingredientFilterDto Filter criteria for ingredients
     *
     * @return Page of ingredients, that match the filter criteria
     */
    Page<Ingredient> findAllFiltered(Pageable pageable, IngredientFilterDto ingredientFilterDto);
}
