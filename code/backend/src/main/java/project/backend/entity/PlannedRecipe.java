package project.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import project.backend.enums.MealType;

import java.time.LocalDate;

@Entity
@Table(name = "planed_recipe")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannedRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_planed_recipe")
    @SequenceGenerator(name = "seq_planed_recipe", allocationSize = 5)
    @EqualsAndHashCode.Exclude
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "meal", nullable = false)
    private MealType meal;

    @Column(name = "comment", nullable = true, length = 10000)
    private String comment;

    @Column(name = "portion_count", nullable = false)
    private Integer portionCount;

    @ManyToOne
    @JoinColumn(name = "recipe_planned", nullable = false)
    private Recipe recipe;
}
