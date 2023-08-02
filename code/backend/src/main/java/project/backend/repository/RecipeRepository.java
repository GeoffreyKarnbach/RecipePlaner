package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.entity.Ingredient;
import project.backend.entity.Recipe;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
