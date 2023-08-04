package project.backend.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.backend.dto.IngredientCategoryDto;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.entity.Ingredient;
import project.backend.entity.IngredientCategory;

import java.util.List;

@Mapper
public interface IngredientMapper {

    @Named("mapIngredientCategoryToIngredientCategoryDto")
    IngredientCategoryDto mapIngredientCategoryToIngredientCategoryDto(IngredientCategory ingredientCategory);

    @Named("mapIngredientCategoryDtoToIngredientCategoryList")
    @IterableMapping(qualifiedByName = "mapIngredientCategoryToIngredientCategoryDto")
    List<IngredientCategoryDto> mapCategoriesToCategoriesDtos(List<IngredientCategory> ingredientCategories);

    @Named("mapIngredientCreationDtoToIngredient")
    @Mapping(target = "ingredientCategory", ignore = true)
    @Mapping(target = "imageSource", ignore = true)
    Ingredient mapIngredientCreationDtoToIngredient(IngredientCreationDto ingredientCreationDto);

    @Named("mapIngredientToIngredientDto")
    @Mapping(target = "ingredientCategory", ignore = true)
    IngredientDto mapIngredientToIngredientDto(Ingredient ingredient);
}

