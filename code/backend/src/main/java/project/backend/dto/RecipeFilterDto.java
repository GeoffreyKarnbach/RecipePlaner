package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
