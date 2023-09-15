package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.entity.PlannedRecipe;
import project.backend.enums.MealType;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlannedRecipeRepository extends JpaRepository<PlannedRecipe, Long> {

    @Query("SELECT pr FROM PlannedRecipe pr WHERE pr.date = ?1 AND pr.meal = ?2")
    List<PlannedRecipe> findByDateAndMeal(LocalDate date, MealType meal);

    @Query("SELECT pr FROM PlannedRecipe pr WHERE pr.date BETWEEN ?1 AND ?2 ORDER BY pr.date ASC, pr.meal ASC")
    List<PlannedRecipe> findPlannedRecipeBetweenDates(LocalDate startDate, LocalDate endDate);
}
