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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import project.backend.dto.IngredientDto;
import project.backend.dto.RecipeFilterDto;
import project.backend.entity.Ingredient;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.entity.RecipeTag;
import project.backend.enums.MealType;

import java.util.ArrayList;
import java.util.List;


public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Recipe> findAllFiltered(Pageable pageable, RecipeFilterDto recipeFilterDto) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        var query = criteriaBuilder.createTupleQuery();

        Root<Recipe> recipe = query.from(Recipe.class);
        Join<Recipe, RecipeCategory> recipeWithCategory = recipe.join("recipeCategory");

        Predicate namePred;

        if (recipeFilterDto.getName() != null){
            namePred = criteriaBuilder.like(
                criteriaBuilder.upper(recipe.get("name")),
                "%" + recipeFilterDto.getName().toUpperCase() + "%"
            );
        } else {
            namePred = criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            );
        }

        Predicate categoryPred;

        if (recipeFilterDto.getCategory() != null){
            categoryPred = criteriaBuilder.equal(
                recipeWithCategory.get("name"),
                recipeFilterDto.getCategory()
            );
        } else {
            categoryPred = criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            );
        }

        Predicate mealTypePred;

        if (recipeFilterDto.getMealType() != null){

            MealType mealType = MealType.valueOf(recipeFilterDto.getMealType().toUpperCase());

            mealTypePred = criteriaBuilder.equal(
                recipe.get("mealType"),
                mealType
            );
        } else {
            mealTypePred = criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            );
        }

        Predicate maxPreparationTimePred;

        if (recipeFilterDto.getMaxPreparationTime() != 0){
            maxPreparationTimePred = criteriaBuilder.lessThanOrEqualTo(
                recipe.get("preparationTime"),
                recipeFilterDto.getMaxPreparationTime()
            );
        } else {
            maxPreparationTimePred = criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            );
        }

        Predicate minDifficultyPred;

        if (recipeFilterDto.getMinDifficulty() != 0){
            minDifficultyPred = criteriaBuilder.greaterThanOrEqualTo(
                recipe.get("difficulty"),
                recipeFilterDto.getMinDifficulty()
            );
        } else {
            minDifficultyPred = criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            );
        }

        Predicate maxDifficultyPred;

        if (recipeFilterDto.getMaxDifficulty() != 0){
            maxDifficultyPred = criteriaBuilder.lessThanOrEqualTo(
                recipe.get("difficulty"),
                recipeFilterDto.getMaxDifficulty()
            );
        } else {
            maxDifficultyPred = criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            );
        }

        List<Predicate> tagPred = new ArrayList<>();

        if (recipeFilterDto.getTags() != null){
            for (String tag : recipeFilterDto.getTags()) {
                Join<Recipe, RecipeTag> recipeWithTag = recipe.join("recipeTags");

                tagPred.add(criteriaBuilder.equal(
                    recipeWithTag.get("name"),
                    tag
                ));
            }

        } else {
            tagPred.add(criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            ));
        }

        List<Predicate> ingredientPred = new ArrayList<>();

        if (recipeFilterDto.getIngredients() != null){
            for (IngredientDto ingredient : recipeFilterDto.getIngredients()) {
                Join<Recipe, Ingredient> recipeWithIngredient = recipe.join("ingredients");

                ingredientPred.add(criteriaBuilder.equal(
                    recipeWithIngredient.get("name"),
                    ingredient.getName()
                ));
            }
        } else {
            ingredientPred.add(criteriaBuilder.equal(
                recipe.get("id"),
                recipe.get("id")
            ));
        }

        // Return results
        var criteria = criteriaBuilder.and(namePred, categoryPred, mealTypePred, maxPreparationTimePred, minDifficultyPred, maxDifficultyPred);
        criteria = criteriaBuilder.and(criteria, criteriaBuilder.and(tagPred.toArray(new Predicate[tagPred.size()])));
        criteria = criteriaBuilder.and(criteria, criteriaBuilder.and(ingredientPred.toArray(new Predicate[ingredientPred.size()])));

        query.where(criteria).distinct(true);

        TypedQuery<Tuple> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Recipe> recipes = typedQuery.getResultList().stream().map(tuple -> {
            Recipe recipe1 = tuple.get(0, Recipe.class);
            return recipe1;
        }).toList();

        return new PageImpl<>(recipes, pageable, countQuery(recipeFilterDto, pageable, recipes.size()));
    }

    private int countQuery(RecipeFilterDto recipeFilterDto, Pageable pageable, int resultSize){

        int totalCount = resultSize;

        if (totalCount >= pageable.getPageSize()){
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);

            Root<Recipe> recipe = countQuery.from(Recipe.class);
            Join<Recipe, RecipeCategory> recipeWithCategory = recipe.join("recipeCategory");

            Predicate namePred;

            if (recipeFilterDto.getName() != null){
                namePred = criteriaBuilder.like(
                    criteriaBuilder.upper(recipe.get("name")),
                    "%" + recipeFilterDto.getName().toUpperCase() + "%"
                );
            } else {
                namePred = criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                );
            }

            Predicate categoryPred;

            if (recipeFilterDto.getCategory() != null){
                categoryPred = criteriaBuilder.equal(
                    recipeWithCategory.get("name"),
                    recipeFilterDto.getCategory()
                );
            } else {
                categoryPred = criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                );
            }

            Predicate mealTypePred;

            if (recipeFilterDto.getMealType() != null){

                MealType mealType = MealType.valueOf(recipeFilterDto.getMealType().toUpperCase());

                mealTypePred = criteriaBuilder.equal(
                    recipe.get("mealType"),
                    mealType
                );
            } else {
                mealTypePred = criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                );
            }

            Predicate maxPreparationTimePred;

            if (recipeFilterDto.getMaxPreparationTime() != 0){
                maxPreparationTimePred = criteriaBuilder.lessThanOrEqualTo(
                    recipe.get("preparationTime"),
                    recipeFilterDto.getMaxPreparationTime()
                );
            } else {
                maxPreparationTimePred = criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                );
            }

            Predicate minDifficultyPred;

            if (recipeFilterDto.getMinDifficulty() != 0){
                minDifficultyPred = criteriaBuilder.greaterThanOrEqualTo(
                    recipe.get("difficulty"),
                    recipeFilterDto.getMinDifficulty()
                );
            } else {
                minDifficultyPred = criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                );
            }

            Predicate maxDifficultyPred;

            if (recipeFilterDto.getMaxDifficulty() != 0){
                maxDifficultyPred = criteriaBuilder.lessThanOrEqualTo(
                    recipe.get("difficulty"),
                    recipeFilterDto.getMaxDifficulty()
                );
            } else {
                maxDifficultyPred = criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                );
            }

            List<Predicate> tagPred = new ArrayList<>();

            if (recipeFilterDto.getTags() != null){
                for (String tag : recipeFilterDto.getTags()) {
                    Join<Recipe, RecipeTag> recipeWithTag = recipe.join("recipeTags");

                    tagPred.add(criteriaBuilder.equal(
                        recipeWithTag.get("name"),
                        tag
                    ));
                }

            } else {
                tagPred.add(criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                ));
            }

            List<Predicate> ingredientPred = new ArrayList<>();

            if (recipeFilterDto.getIngredients() != null){
                for (IngredientDto ingredient : recipeFilterDto.getIngredients()) {
                    Join<Recipe, Ingredient> recipeWithIngredient = recipe.join("ingredients");

                    ingredientPred.add(criteriaBuilder.equal(
                        recipeWithIngredient.get("name"),
                        ingredient.getName()
                    ));
                }
            } else {
                ingredientPred.add(criteriaBuilder.equal(
                    recipe.get("id"),
                    recipe.get("id")
                ));
            }

            // Return results
            var criteria = criteriaBuilder.and(namePred, categoryPred, mealTypePred, maxPreparationTimePred, minDifficultyPred, maxDifficultyPred);
            criteria = criteriaBuilder.and(criteria, criteriaBuilder.and(tagPred.toArray(new Predicate[tagPred.size()])));
            criteria = criteriaBuilder.and(criteria, criteriaBuilder.and(ingredientPred.toArray(new Predicate[ingredientPred.size()])));

            countQuery.select(criteriaBuilder.count(recipe));
            countQuery.where(criteria).distinct(true);

            totalCount = Math.toIntExact(entityManager.createQuery(countQuery).getSingleResult());
        }

        return totalCount;
    }
}
