package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.backend.dto.LightRecipeDto;
import project.backend.dto.PageableDto;
import project.backend.dto.RecipeCategoryDto;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.dto.ValidationErrorRestDto;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.entity.RecipeImage;
import project.backend.exception.NotFoundException;
import project.backend.exception.ValidationException;
import project.backend.mapper.RecipeMapper;
import project.backend.repository.RecipeCategoryRepository;
import project.backend.repository.RecipeImageRepository;
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
    private final RecipeImageRepository recipeImageRepository;

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

        List<RecipeImage> recipeImages = recipeImageRepository.findRecipeImageByRecipeId(id);

        String[] images = new String[recipeImages.size()];
        for (int i = 0; i < recipeImages.size(); i++) {
            images[i] = recipeImages.get(i).getImageSource();
        }

        recipeDtoToReturn.setImages(images);

        return recipeDtoToReturn;
    }

    @Override
    public PageableDto<LightRecipeDto> getRecipes(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new ValidationException(
                new ValidationErrorRestDto(
                    "Page must be greater than or equal to 0 and pageSize must be greater than 0", null));
        }

        Page<Recipe> recipes = recipeRepository.findAll(PageRequest.of(page, pageSize));
        return getRecipeDtoPageableDto(recipes);
    }

    @Override
    public void deleteRecipe(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()){
            throw new NotFoundException("Recipe with id " + id + " not found");
        }

        recipeRepository.deleteById(id);
    }

    private PageableDto<LightRecipeDto> getRecipeDtoPageableDto(Page<Recipe> recipes) {
        var recipesDtos = recipes.stream().map(recipeMapper::mapRecipeToLightRecipeDto).toList();

        for (int i = 0; i < recipesDtos.size(); i++) {
            Optional<RecipeImage> recipeImage = recipeImageRepository.getFirstImageByRecipe(recipes.getContent().get(i).getId());

            if (recipeImage.isPresent()) {
                recipesDtos.get(i).setMainImage(recipeImage.get().getImageSource());
            }

            recipesDtos.get(i).setRecipeCategory(recipes.getContent().get(i).getRecipeCategory().getName());
        }

        return new PageableDto<>(
            recipes.getTotalElements(),
            recipes.getTotalPages(),
            recipes.getNumberOfElements(),
            recipesDtos
        );
    }

}
