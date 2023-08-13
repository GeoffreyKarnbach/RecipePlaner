package project.backend.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;

import java.util.List;

@Mapper
public interface RecipeMapper {

    @Named("mapRecipeCategoryToRecipeCategoryDto")
    RecipeCategoryDto mapRecipeCategoryToRecipeCategoryDto(RecipeCategory recipeCategory);

    @Named("mapRecipeCategoryDtoToRecipeCategoryList")
    @IterableMapping(qualifiedByName = "mapRecipeCategoryToRecipeCategoryDto")
    List<RecipeCategoryDto> mapCategoriesToCategoriesDtos(List<RecipeCategory> recipeCategories);

    @Named("mapRecipeCreationDtoToRecipe")
    @Mapping(target = "recipeCategory", ignore = true)
    Recipe mapRecipeCreationDtoToRecipe(RecipeCreationDto recipeCreationDto);

    @Named("mapRecipeToRecipeDto")
    @Mapping(target = "recipeCategory", ignore = true)
    RecipeDto mapRecipeToRecipeDto(Recipe recipe);
}
