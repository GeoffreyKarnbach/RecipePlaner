package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {

    private Long id;

    private String name;

    private String description;

    private int difficulty;

    private int preparationTime;

    private String mealType;

    private String recipeCategory;
}
