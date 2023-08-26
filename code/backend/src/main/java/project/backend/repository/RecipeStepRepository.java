package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.entity.RecipeStep;

import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

    @Query("SELECT rs FROM RecipeStep rs WHERE rs.recipe.id = ?1")
    List<RecipeStep> getRecipeStepByRecipeId(Long recipeId);
}
