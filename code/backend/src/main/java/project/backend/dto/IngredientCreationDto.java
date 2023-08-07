package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.enums.IngredientUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientCreationDto {

    private String name;

    private String imageSource;

    private IngredientUnit unit;

    private float count;

    private String ingredientCategory;
}
