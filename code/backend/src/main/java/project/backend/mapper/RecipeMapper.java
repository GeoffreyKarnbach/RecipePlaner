package project.backend.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import project.backend.dto.LightRecipeDto;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.dto.RecipeRatingDto;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.entity.RecipeRating;

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
    @Mapping(target = "ingredients", ignore = true)
    RecipeDto mapRecipeToRecipeDto(Recipe recipe);

    @Named("mapRecipeDtoToRecipeCreationDto")
    RecipeCreationDto mapRecipeDtoToRecipeCreationDto(RecipeDto recipeDto);

    @Named("mapRecipeDtoToRecipe")
    @Mapping(target = "recipeCategory", ignore = true)
    @Mapping(target = "ingredients", ignore = true)
    Recipe mapRecipeDtoToRecipe(RecipeDto recipeDto);

    @Named("mapRecipeToLightRecipeDto")
    @Mapping(target = "recipeCategory", ignore = true)
    LightRecipeDto mapRecipeToLightRecipeDto(Recipe recipe);

    @Named("mapRecipeRatingToRecipeRatingDto")
    RecipeRatingDto mapRecipeRatingToRecipeRatingDto(RecipeRating recipeRating);
}
