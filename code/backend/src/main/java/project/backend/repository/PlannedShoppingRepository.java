package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.backend.entity.PlannedShopping;

import java.time.LocalDate;
import java.util.List;

public interface PlannedShoppingRepository extends JpaRepository<PlannedShopping, Long> {

    @Query("SELECT pr FROM PlannedShopping pr WHERE pr.date BETWEEN ?1 AND ?2 ORDER BY pr.date ASC, pr.isMorning ASC")
    List<PlannedShopping> findPlannedShoppingBetweenDates(LocalDate startDate, LocalDate endDate);
}
