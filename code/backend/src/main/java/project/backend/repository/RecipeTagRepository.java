package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.entity.RecipeTag;

import java.util.Optional;

@Repository
public interface RecipeTagRepository extends JpaRepository<RecipeTag, Long> {

    Optional<RecipeTag> findRecipeTagByName(String name);
}
