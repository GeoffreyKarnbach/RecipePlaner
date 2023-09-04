package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.enums.RecipeFilterCriterias;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeFilterDto {

    private String name;

    private String category;

    private String mealType;

    private int maxPreparationTime;

    private int minDifficulty;

    private int maxDifficulty;

    private String[] tags;

    private IngredientDto[] ingredients;

    private RecipeFilterCriterias filterCriteria;
}
