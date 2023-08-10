package project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.backend.enums.IngredientFilterCriterias;
import project.backend.enums.IngredientUnit;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngredientFilterDto {

    private String filterName;

    private String filterCategory;

    private IngredientUnit filterUnit;

    private IngredientFilterCriterias filterCriteria;
}
