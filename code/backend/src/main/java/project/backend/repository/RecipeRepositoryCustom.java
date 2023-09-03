package project.backend.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.backend.dto.RecipeFilterDto;

import project.backend.entity.Recipe;

public interface RecipeRepositoryCustom {

    /**
     * Find all recipes filtered by recipeFilterDto in the persistent storage
     *
     * @param pageable Pageable, with page number and page size
     * @param recipeFilterDto Filter criteria for recipes
     *
     * @return Page of recipes, that match the filter criteria
     */
    Page<Recipe> findAllFiltered(Pageable pageable, RecipeFilterDto recipeFilterDto);
}
