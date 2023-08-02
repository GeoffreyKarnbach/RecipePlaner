package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeRating;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {
}
