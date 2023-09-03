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
import project.backend.dto.RecipeFilterDto;
import project.backend.dto.RecipeIngredientItemDto;
import project.backend.dto.RecipeIngredientListDto;
import project.backend.dto.RecipeRatingDto;
import project.backend.dto.RecipeSingleStepDto;
import project.backend.dto.RecipeStepsDto;
import project.backend.dto.ValidationErrorRestDto;
import project.backend.entity.Ingredient;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.entity.RecipeImage;
import project.backend.entity.RecipeRating;
import project.backend.entity.RecipeStep;
import project.backend.entity.RecipeTag;
import project.backend.exception.NotFoundException;
import project.backend.exception.ValidationException;
import project.backend.mapper.RecipeMapper;
import project.backend.repository.IngredientRepository;
import project.backend.repository.RecipeCategoryRepository;
import project.backend.repository.RecipeImageRepository;
import project.backend.repository.RecipeRatingRepository;
import project.backend.repository.RecipeRepository;
import project.backend.repository.RecipeStepRepository;
import project.backend.repository.RecipeTagRepository;
import project.backend.service.ImageService;
import project.backend.service.RecipeService;
import project.backend.service.validator.RecipeValidator;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final ImageService imageService;
    private final RecipeTagRepository recipeTagRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeStepRepository recipeStepRepository;
    private final RecipeRatingRepository recipeRatingRepository;

    @Override
    public List<RecipeCategoryDto> getRecipeCategories() {
        List<RecipeCategory> recipeCategories = recipeCategoryRepository.findAll();
        return recipeMapper.mapCategoriesToCategoriesDtos(recipeCategories);
    }

    @Override
    public List<String> getRecipeTags() {
        List<RecipeTag> recipeTags = recipeTagRepository.findAll();

        List<String> tagList = new ArrayList<>();
        for (RecipeTag recipeTag : recipeTags) {
            tagList.add(recipeTag.getName());
        }

        return tagList;
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

        this.handleTags(recipeDto.getTags(), recipe.getId());

        return recipeDtoToReturn;
    }

    private void handleTags(String[] tags, Long recipeId) {
        recipeValidator.validateRecipeTags(tags);

        Recipe recipe = recipeRepository.findById(recipeId).get();
        List<RecipeTag> newTags = new ArrayList<>();

        for (String tag : tags) {
            Optional<RecipeTag> recipeTag = recipeTagRepository.findRecipeTagByName(tag);

            newTags.add(recipeTag.get());
        }

        recipe.setRecipeTags(newTags);
        recipeRepository.save(recipe);
    }

    @Override
    public RecipeDto editRecipe(RecipeDto recipeDto, Long id) {

        recipeValidator.validateRecipeDtoForEdit(recipeDto, id);

        RecipeCategory recipeCategory = recipeCategoryRepository.findRecipeCategoryByName(recipeDto.getRecipeCategory()).get();

        Optional<Recipe> currentRecipe = recipeRepository.findById(id);
        if (currentRecipe.isEmpty()){
            throw new NotFoundException("Recipe with id " + id + " not found");
        }

        Recipe recipe = recipeMapper.mapRecipeDtoToRecipe(recipeDto);
        recipe.setRecipeCategory(recipeCategory);
        recipe.setIngredients(currentRecipe.get().getIngredients());
        recipe.setRecipeSteps(currentRecipe.get().getRecipeSteps());
        recipe.setIngredientCount(currentRecipe.get().getIngredientCount());
        recipe = recipeRepository.save(recipe);

        RecipeDto recipeDtoToReturn = recipeMapper.mapRecipeToRecipeDto(recipe);
        recipeDtoToReturn.setRecipeCategory(recipeCategory.getName());

        this.handleTags(recipeDto.getTags(), recipe.getId());

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

        List<String> tags = new ArrayList<>();
        for (RecipeTag recipeTag : recipe.get().getRecipeTags()) {
            tags.add(recipeTag.getName());
        }

        recipeDtoToReturn.setTags(tags.toArray(new String[0]));
        recipeDtoToReturn.setIngredients(this.getIngredientList(id));
        recipeDtoToReturn.setSteps(this.getSteps(id));

        int averageRating = recipeRepository.getAverageRating(id);

        recipeDtoToReturn.setAverageRating(averageRating);
        recipeDtoToReturn.setNumberOfRatings(recipeRatingRepository.countByRecipeId(id));

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

        List<RecipeImage> recipeImages = recipeImageRepository.findRecipeImageByRecipeId(id);

        for (RecipeImage recipeImage : recipeImages) {
            imageService.removeImage(recipeImage.getImageSource());
            recipeImageRepository.delete(recipeImage);
        }

        this.handleTags(new String[]{}, id);

        List<RecipeRating> ratings = recipeRatingRepository.findAllByRecipeId(id);
        recipeRatingRepository.deleteAll(ratings);

        List<RecipeStep> steps = recipeStepRepository.getRecipeStepByRecipeId(id);
        recipeStepRepository.deleteAll(steps);

        recipeRepository.deleteById(id);
    }

    @Override
    public void updateIngredientList(RecipeIngredientListDto recipeIngredientListDto) {
        recipeValidator.validateRecipeIngredientList(recipeIngredientListDto);

        Recipe recipe = recipeRepository.findById(recipeIngredientListDto.getRecipeId()).get();

        // Reset content of ingredient list
        recipe.setIngredientCount(new HashMap<>());
        recipe.setIngredients(new ArrayList<>());

        for (RecipeIngredientItemDto item : recipeIngredientListDto.getRecipeIngredientItems()) {
            Ingredient ingredient = this.ingredientRepository.findById(item.getIngredientId()).get();
            recipe.getIngredientCount().put(ingredient, (float) item.getAmount());
            recipe.getIngredients().add(ingredient);
        }

        recipeRepository.save(recipe);
    }

    @Override
    public RecipeIngredientListDto getIngredientList(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()){
            throw new NotFoundException("Recipe with id " + recipeId + " not found");
        }

        RecipeIngredientListDto recipeIngredientListDto = new RecipeIngredientListDto();
        recipeIngredientListDto.setRecipeId(recipeId);

        List<RecipeIngredientItemDto> recipeIngredientItemDtos = new ArrayList<>();
        for (Ingredient ingredient : recipe.get().getIngredients()) {
            RecipeIngredientItemDto recipeIngredientItemDto = new RecipeIngredientItemDto();
            recipeIngredientItemDto.setIngredientId(ingredient.getId());
            recipeIngredientItemDto.setAmount(Math.round(recipe.get().getIngredientCount().get(ingredient)));
            recipeIngredientItemDtos.add(recipeIngredientItemDto);
        }

        recipeIngredientListDto.setRecipeIngredientItems(recipeIngredientItemDtos);

        return recipeIngredientListDto;
    }

    @Override
    public void updateSteps(RecipeStepsDto recipeStepsDto) {

        this.recipeValidator.validateRecipeStepsList(recipeStepsDto);

        List<String> stillNeededImages = new ArrayList<>();
        for (RecipeSingleStepDto recipeSingleStepDto : recipeStepsDto.getSteps()) {
            if (recipeSingleStepDto.getImageSource().startsWith("http://localhost:8080/api/v1/image/")){
                stillNeededImages.add(recipeSingleStepDto.getImageSource());
            }
        }

        List<RecipeStep> oldSteps = recipeStepRepository.getRecipeStepByRecipeId(recipeStepsDto.getRecipeId());

        for (RecipeStep recipeStep : oldSteps) {
            if (!recipeStep.getImageSource().equals("assets/nopic.jpg") &&
                !stillNeededImages.contains(recipeStep.getImageSource())){
                imageService.removeImage(recipeStep.getImageSource());
            }
            recipeStepRepository.delete(recipeStep);
        }

        Recipe recipe = recipeRepository.findById(recipeStepsDto.getRecipeId()).get();

        for (int i = 0; i < recipeStepsDto.getSteps().size(); i++) {
            RecipeSingleStepDto recipeSingleStepDto = recipeStepsDto.getSteps().get(i);
            RecipeStep recipeStep = new RecipeStep();
            recipeStep.setRecipe(recipe);
            recipeStep.setPosition(i);
            recipeStep.setName(recipeSingleStepDto.getName());
            recipeStep.setDescription(recipeSingleStepDto.getDescription());

            String imageSource = recipeSingleStepDto.getImageSource();

            if (!recipeSingleStepDto.getImageSource().equals("assets/nopic.jpg") &&
                !recipeSingleStepDto.getImageSource().startsWith("http://localhost:8080/api/v1/image/")){
                imageSource = imageService.uploadImage(recipeSingleStepDto.getImageSource());
            }

            recipeStep.setImageSource(imageSource);

            recipeStepRepository.save(recipeStep);
        }
    }

    @Override
    public RecipeStepsDto getSteps(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()){
            throw new NotFoundException("Recipe with id " + recipeId + " not found");
        }

        List<RecipeStep> recipeSteps = recipeStepRepository.getRecipeStepByRecipeId(recipeId);
        RecipeStepsDto recipeStepsDto = new RecipeStepsDto();
        recipeStepsDto.setRecipeId(recipeId);

        List<RecipeSingleStepDto> recipeSingleStepDtos = new ArrayList<>();
        for (RecipeStep step: recipeSteps){
            RecipeSingleStepDto recipeSingleStepDto = new RecipeSingleStepDto();

            recipeSingleStepDto.setName(step.getName());
            recipeSingleStepDto.setDescription(step.getDescription());
            recipeSingleStepDto.setImageSource(step.getImageSource());
            recipeSingleStepDto.setPosition(step.getPosition());
            recipeSingleStepDto.setId(step.getId());

            recipeSingleStepDtos.add(recipeSingleStepDto);
        }

        recipeStepsDto.setSteps(recipeSingleStepDtos);

        return recipeStepsDto;
    }

    @Override
    public void addRating(RecipeRatingDto recipeRatingDto) {

        recipeValidator.validateRecipeNewRating(recipeRatingDto);

        Recipe recipe = recipeRepository.findById(recipeRatingDto.getRecipeId()).get();

        RecipeRating newRating = new RecipeRating();
        newRating.setRating(recipeRatingDto.getRating());
        newRating.setComment(recipeRatingDto.getComment());
        newRating.setTitle(recipeRatingDto.getTitle());
        newRating.setRecipe(recipe);
        newRating.setCreatedAt(recipeRatingDto.getDate());

        recipeRatingRepository.save(newRating);
    }

    @Override
    public PageableDto<RecipeRatingDto> getRatings(int page, int pageSize, Long recipeId) {
        if (page < 0 || pageSize <= 0) {
            throw new ValidationException(
                new ValidationErrorRestDto(
                    "Page must be greater than or equal to 0 and pageSize must be greater than 0", null));
        }

        Page<RecipeRating> ratings = recipeRatingRepository.findAllByRecipeId(PageRequest.of(page, pageSize), recipeId);
        return getRecipeRatingDtoPageableDto(ratings, recipeId);
    }

    @Override
    public PageableDto<LightRecipeDto> getFilteredRecipes(int page, int pageSize, RecipeFilterDto recipeFilterDto) {
        Page<Recipe> recipes = recipeRepository.findAllFiltered(PageRequest.of(page, pageSize), recipeFilterDto);

        log.info("{}", recipes);
        return getRecipeDtoPageableDto(recipes);
    }

    private PageableDto<RecipeRatingDto> getRecipeRatingDtoPageableDto(Page<RecipeRating> ratings, Long recipeId){
        var ratingsDtos = ratings.stream().map(recipeMapper::mapRecipeRatingToRecipeRatingDto).toList();

        for (int i = 0; i < ratingsDtos.size(); i++) {
            ratingsDtos.get(i).setRecipeId(recipeId);
            ratingsDtos.get(i).setDate(ratings.getContent().get(i).getCreatedAt());
        }

        return new PageableDto<>(
            ratings.getTotalElements(),
            ratings.getTotalPages(),
            ratings.getNumberOfElements(),
            ratingsDtos
        );
    }

    private PageableDto<LightRecipeDto> getRecipeDtoPageableDto(Page<Recipe> recipes) {
        var recipesDtos = recipes.stream().map(recipeMapper::mapRecipeToLightRecipeDto).toList();

        for (int i = 0; i < recipesDtos.size(); i++) {
            Optional<RecipeImage> recipeImage = recipeImageRepository.getFirstImageByRecipe(recipes.getContent().get(i).getId());

            if (recipeImage.isPresent()) {
                recipesDtos.get(i).setMainImage(recipeImage.get().getImageSource());
            }

            recipesDtos.get(i).setRecipeCategory(recipes.getContent().get(i).getRecipeCategory().getName());
            recipesDtos.get(i).setAverageRating(recipeRepository.getAverageRating(recipes.getContent().get(i).getId()));
            recipesDtos.get(i).setNumberOfRatings(recipeRatingRepository.countByRecipeId(recipes.getContent().get(i).getId()));
        }

        return new PageableDto<>(
            recipes.getTotalElements(),
            recipes.getTotalPages(),
            recipes.getNumberOfElements(),
            recipesDtos
        );
    }

}
