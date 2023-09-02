package project.backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findAll(Pageable pageable);

    @Query(value = "SELECT COALESCE(AVG(rr.rating), 0) FROM Recipe r JOIN RecipeRating rr WHERE r.id = ?1 AND rr.recipe.id = r.id")
    int getAverageRating(Long id);

}
