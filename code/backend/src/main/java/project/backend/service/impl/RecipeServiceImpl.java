package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.mapper.RecipeMapper;
import project.backend.repository.RecipeCategoryRepository;
import project.backend.repository.RecipeRepository;
import project.backend.service.RecipeService;
import project.backend.service.validator.RecipeValidator;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeValidator recipeValidator;
    private final RecipeRepository recipeRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeCategoryRepository recipeCategoryRepository;

    @Override
    public List<RecipeCategoryDto> getRecipeCategories() {
        List<RecipeCategory> recipeCategories = recipeCategoryRepository.findAll();
        return recipeMapper.mapCategoriesToCategoriesDtos(recipeCategories);
    }

    @Override
    public RecipeDto createRecipe(RecipeCreationDto recipeDto) {

        recipeValidator.validateRecipeForCreation(recipeDto);

        Recipe recipe = recipeMapper.mapRecipeCreationDtoToRecipe(recipeDto);
        RecipeCategory recipeCategory = recipeCategoryRepository.findRecipeCategoryByName(recipeDto.getRecipeCategory()).get();
        recipe.setRecipeCategory(recipeCategory);

        recipe = recipeRepository.save(recipe);

        RecipeDto recipeDtoToReturn = recipeMapper.mapRecipeToRecipeDto(recipe);
        recipeDtoToReturn.setRecipeCategory(recipeCategory.getName());

        return recipeDtoToReturn;
    }
}
