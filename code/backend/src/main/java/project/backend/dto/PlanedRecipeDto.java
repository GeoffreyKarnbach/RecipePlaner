package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanedRecipeDto {

    private Long id;

    private Long recipeId;

    private String recipeName;

    private LocalDate date;

    private String meal;

    private String comment;

    private Integer portionCount;
}
