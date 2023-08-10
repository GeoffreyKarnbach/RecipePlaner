package project.backend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.backend.dto.IngredientFilterDto;
import project.backend.entity.Ingredient;

@Slf4j
public class IngredientRepositoryCustomImpl implements IngredientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Ingredient> findAllFiltered(Pageable pageable, IngredientFilterDto ingredientFilterDto) {
        return null;
    }
}
