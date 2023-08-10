package project.backend.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.backend.dto.IngredientFilterDto;
import project.backend.entity.Ingredient;
import project.backend.entity.IngredientCategory;
import project.backend.enums.IngredientFilterCriterias;

import java.util.List;

@Slf4j
public class IngredientRepositoryCustomImpl implements IngredientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Ingredient> findAllFiltered(Pageable pageable, IngredientFilterDto ingredientFilterDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createTupleQuery();
        Root<Ingredient> ingredient = query.from(Ingredient.class);
        Join<Ingredient, IngredientCategory> ingredientWithCategory = ingredient.join("ingredientCategory");

        var namePred = criteriaBuilder.like(
            criteriaBuilder.upper(ingredient.get("name")),
            "%" + ingredientFilterDto.getFilterName().toUpperCase() + "%"
        );


        Predicate categoryPred;

        if (ingredientFilterDto.getFilterCategory() != null){
            categoryPred = criteriaBuilder.equal(
                ingredientWithCategory.get("name"),
                ingredientFilterDto.getFilterCategory()
            );
        } else {
            categoryPred = criteriaBuilder.equal(
                ingredient.get("id"),
                ingredient.get("id")
            );
        }

        Predicate unitPred;

        if (ingredientFilterDto.getFilterUnit() != null){
            unitPred = criteriaBuilder.equal(
                ingredient.get("unit"),
                ingredientFilterDto.getFilterUnit()
            );
        } else {
            unitPred = criteriaBuilder.equal(
                ingredient.get("id"),
                ingredient.get("id")
            );
        }

        var criteria = criteriaBuilder.and(namePred, categoryPred, unitPred);

        query.where(criteria);

        // Sort by criteria
        if (ingredientFilterDto.getFilterCriteria().equals(IngredientFilterCriterias.ALPHABETICAL_ASCENDING)){
            query.orderBy(criteriaBuilder.asc(ingredient.get("name")));
        } else if (ingredientFilterDto.getFilterCriteria().equals(IngredientFilterCriterias.ALPHABETICAL_DESCENDING)){
            query.orderBy(criteriaBuilder.desc(ingredient.get("name")));
        } else if (ingredientFilterDto.getFilterCriteria().equals(IngredientFilterCriterias.QUANTITY_ASCENDING)){
            query.orderBy(criteriaBuilder.asc(ingredient.get("count")));
        } else if (ingredientFilterDto.getFilterCriteria().equals(IngredientFilterCriterias.QUANTITY_DESCENDING)){
            query.orderBy(criteriaBuilder.desc(ingredient.get("count")));
        }

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());


        List<Ingredient> ingredients = typedQuery.getResultList().stream().map(tuple -> {
            Ingredient ingredient1 = tuple.get(0, Ingredient.class);
            return ingredient1;
        }).toList();

        return new PageImpl<>(ingredients, pageable, countQuery(ingredientFilterDto, pageable, ingredients.size()));
    }

    private int countQuery(IngredientFilterDto ingredientFilterDto, Pageable pageable, int resultSize){
        int totalCount = resultSize;

        if (totalCount >= pageable.getPageSize()) {
            CriteriaBuilder criteriaBuilderCount  = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = criteriaBuilderCount.createQuery(Long.class);

            Root<Ingredient> ingredientCount = countQuery.from(Ingredient.class);
            Join<Ingredient, IngredientCategory> ingredientWithCategoryCount = ingredientCount.join("ingredientCategory");

            var namePredCount = criteriaBuilderCount.like(
                criteriaBuilderCount.upper(ingredientCount.get("name")),
                "%" + ingredientFilterDto.getFilterName().toUpperCase() + "%"
            );

            Predicate categoryPred;

            if (ingredientFilterDto.getFilterCategory() != null){
                categoryPred = criteriaBuilderCount.equal(
                    ingredientWithCategoryCount.get("name"),
                    ingredientFilterDto.getFilterCategory()
                );
            } else {
                categoryPred = criteriaBuilderCount.equal(
                    ingredientCount.get("id"),
                    ingredientCount.get("id")
                );
            }

            Predicate unitPred;

            if (ingredientFilterDto.getFilterUnit() != null){
                unitPred = criteriaBuilderCount.equal(
                    ingredientCount.get("unit"),
                    ingredientFilterDto.getFilterUnit()
                );
            } else {
                unitPred = criteriaBuilderCount.equal(
                    ingredientCount.get("id"),
                    ingredientCount.get("id")
                );
            }

            var criteriaCount = criteriaBuilderCount.and(namePredCount, categoryPred, unitPred);

            countQuery.select(criteriaBuilderCount.count(ingredientCount));
            countQuery.where(criteriaCount);

            totalCount = Math.toIntExact(entityManager.createQuery(countQuery).getSingleResult());
        }

        return totalCount;
    }


}
