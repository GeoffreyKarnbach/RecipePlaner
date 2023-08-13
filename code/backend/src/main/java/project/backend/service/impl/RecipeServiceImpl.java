package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.exception.NotFoundException;
import project.backend.mapper.RecipeMapper;
import project.backend.repository.RecipeCategoryRepository;
import project.backend.repository.RecipeRepository;
import project.backend.service.RecipeService;
import project.backend.service.validator.RecipeValidator;

import java.util.List;
import java.util.Optional;

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

    @Override
    public RecipeDto editRecipe(RecipeDto recipeDto, Long id) {

        recipeValidator.validateRecipeDtoForEdit(recipeDto, id);

        RecipeCategory recipeCategory = recipeCategoryRepository.findRecipeCategoryByName(recipeDto.getRecipeCategory()).get();

        Recipe recipe = recipeMapper.mapRecipeDtoToRecipe(recipeDto);
        recipe.setRecipeCategory(recipeCategory);
        recipe = recipeRepository.save(recipe);

        RecipeDto recipeDtoToReturn = recipeMapper.mapRecipeToRecipeDto(recipe);
        recipeDtoToReturn.setRecipeCategory(recipeCategory.getName());

        return recipeDtoToReturn;
    }

    @Override
    public RecipeDto getRecipe(Long id) {

        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()){
            throw new NotFoundException("Recipe with id " + id + " not found");
        }

        RecipeDto recipeDtoToReturn = recipeMapper.mapRecipeToRecipeDto(recipe.get());
        recipeDtoToReturn.setRecipeCategory(recipe.get().getRecipeCategory().getName());

        return recipeDtoToReturn;
    }
}
