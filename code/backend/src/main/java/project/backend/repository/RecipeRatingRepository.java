package project.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.entity.RecipeRating;

import java.util.List;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {

    @Query("SELECT r FROM RecipeRating r WHERE r.recipe.id = ?1")
    Page<RecipeRating> findAllByRecipeId(Pageable pageable, Long recipeId);

    @Query("SELECT COUNT(*) FROM RecipeRating r WHERE r.recipe.id = ?1")
    int countByRecipeId(Long recipeId);

    @Query("SELECT r FROM RecipeRating r WHERE r.recipe.id = ?1")
    List<RecipeRating> findAllByRecipeId(Long recipeId);
}
