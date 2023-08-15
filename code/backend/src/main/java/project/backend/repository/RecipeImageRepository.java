package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.entity.RecipeImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long> {

    @Query("SELECT i FROM RecipeImage i WHERE i.recipe.id = ?1")
    List<RecipeImage> findRecipeImageByRecipeId(Long recipeId);

    @Query("SELECT i FROM RecipeImage i WHERE i.recipe.id = ?1 AND i.imagePosition = 0")
    Optional<RecipeImage> getFirstImageByRecipe(Long recipeId);
}
