package project.backend.service.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import project.backend.dto.RecipeCreationDto;
import project.backend.dto.RecipeDto;
import project.backend.dto.RecipeIngredientItemDto;
import project.backend.dto.RecipeIngredientListDto;
import project.backend.dto.RecipeRatingDto;
import project.backend.dto.RecipeSingleStepDto;
import project.backend.dto.RecipeStepsDto;
import project.backend.dto.ValidationErrorDto;
import project.backend.dto.ValidationErrorRestDto;
import project.backend.entity.Ingredient;
import project.backend.entity.Recipe;
import project.backend.entity.RecipeCategory;
import project.backend.entity.RecipeTag;
import project.backend.exception.NotFoundException;
import project.backend.exception.ValidationException;
import project.backend.mapper.RecipeMapper;
import project.backend.repository.IngredientRepository;
import project.backend.repository.RecipeCategoryRepository;
import project.backend.repository.RecipeRepository;
import project.backend.repository.RecipeTagRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecipeValidator {

    private final RecipeCategoryRepository recipeCategoryRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeMapper recipeMapper;
    private final RecipeTagRepository recipeTagRepository;

    public void validateRecipeForCreation(RecipeCreationDto recipeDto) {

        List<String> validationErrors = new ArrayList<>();

        if (recipeDto.getName() == null || recipeDto.getName().isEmpty() || recipeDto.getName().isBlank()) {
            validationErrors.add("Recipe name cannot be empty");
        }

        if (recipeDto.getDescription() == null || recipeDto.getDescription().isEmpty() || recipeDto.getDescription().isBlank()) {
            validationErrors.add("Recipe description cannot be empty");
        }

        if (recipeDto.getDifficulty() < 1 || recipeDto.getDifficulty() > 5) {
            validationErrors.add("Recipe difficulty must be between 1 and 5");
        }

        if (recipeDto.getPreparationTime() < 1) {
            validationErrors.add("Recipe preparation time must be greater than 0");
        }

        Optional<RecipeCategory> recipeCategory = recipeCategoryRepository.findRecipeCategoryByName(recipeDto.getRecipeCategory());
        if (recipeCategory.isEmpty()) {
            validationErrors.add("Recipe category does not exist");
        }

        List<ValidationErrorDto> validationErrorDtos = new ArrayList<>();

        for (int i = 0; i < validationErrors.size(); i++) {
            validationErrorDtos.add(new ValidationErrorDto((long) i, validationErrors.get(i), null));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Rezepten", validationErrorDtos));
        }
    }

    public void validateRecipeDtoForEdit(RecipeDto recipeDto, Long id){

        if (recipeDto.getId() == null) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Rezept", List.of(new ValidationErrorDto(0L, "Recipe id cannot be null", null))));
        }

        if (!recipeDto.getId().equals(id)) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Rezept", List.of(new ValidationErrorDto(0L, "Recipe id in path and body do not match", null))));
        }

        Optional<Recipe> recipe = recipeRepository.findById(recipeDto.getId());

        if (recipe.isEmpty()) {
            throw new NotFoundException("Recipe with id '" + recipeDto.getId() + "' does not exist");
        }

        RecipeCreationDto dtoToValidate = recipeMapper.mapRecipeDtoToRecipeCreationDto(recipeDto);
        this.validateRecipeForCreation(dtoToValidate);
    }

    public void validateRecipeTags(String[] tags) {
        List<String> validationErrors = new ArrayList<>();
        List<String> existingErrors = new ArrayList<>();

        for (String tag : tags) {

            Optional<RecipeTag> recipeTag = recipeTagRepository.findRecipeTagByName(tag);
            if (recipeTag.isEmpty()) {
                validationErrors.add("Recipe tag '" + tag + "' does not exist");
            }

            int count = 0;
            for (String tag2 : tags) {
                if (tag.equals(tag2)) {
                    count++;
                }
            }

            if (count > 1 && !existingErrors.contains(tag)) {
                validationErrors.add("Recipe tag '" + tag + "' is duplicated");
                existingErrors.add(tag);
            }
        }

        List<ValidationErrorDto> validationErrorDtos = new ArrayList<>();

        for (int i = 0; i < validationErrors.size(); i++) {
            validationErrorDtos.add(new ValidationErrorDto((long) i, validationErrors.get(i), null));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Tags", validationErrorDtos));
        }
    }

    public void validateRecipeIngredientList(RecipeIngredientListDto recipeIngredientListDto){
        List<String> validationErrors = new ArrayList<>();

        Optional<Recipe> recipe = recipeRepository.findById(recipeIngredientListDto.getRecipeId());
        if (recipe.isEmpty()) {
            validationErrors.add("Recipe with id '" + recipeIngredientListDto.getRecipeId() + "' does not exist");
        }

        for (RecipeIngredientItemDto item: recipeIngredientListDto.getRecipeIngredientItems()){
            Optional<Ingredient> ingredient = ingredientRepository.findById(item.getIngredientId());
            if (ingredient.isEmpty()) {
                validationErrors.add("Ingredient with id '" + item.getIngredientId() + "' does not exist");
            }

            if (item.getAmount() < 1) {
                validationErrors.add("Ingredient amount must be greater than 0");
            }
        }

        List<ValidationErrorDto> validationErrorDtos = new ArrayList<>();

        for (int i = 0; i < validationErrors.size(); i++) {
            validationErrorDtos.add(new ValidationErrorDto((long) i, validationErrors.get(i), null));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Rezept Zutatenliste", validationErrorDtos));
        }
    }

    public void validateRecipeStepsList(RecipeStepsDto recipeStepsDto){
        List<String> validationErrors = new ArrayList<>();
        HashSet<Integer> positions = new HashSet<>();

        Optional<Recipe> recipe = recipeRepository.findById(recipeStepsDto.getRecipeId());
        if (recipe.isEmpty()) {
            validationErrors.add("Recipe with id '" + recipeStepsDto.getRecipeId() + "' does not exist");
        }

        for (RecipeSingleStepDto step: recipeStepsDto.getSteps()){
            if (step.getName() == null || step.getName().isEmpty() || step.getName().isBlank()) {
                validationErrors.add("Recipe step name cannot be empty");
            }

            if (step.getDescription() == null || step.getDescription().isEmpty() || step.getDescription().isBlank()) {
                validationErrors.add("Recipe step description cannot be empty");
            }

            if (step.getPosition() < 0) {
                validationErrors.add("Recipe step position must be greater or equal to 0");
            }

            if (positions.contains(step.getPosition())) {
                validationErrors.add("Recipe step position is duplicated");
            }

            positions.add(step.getPosition());
        }

        List<ValidationErrorDto> validationErrorDtos = new ArrayList<>();

        for (int i = 0; i < validationErrors.size(); i++) {
            validationErrorDtos.add(new ValidationErrorDto((long) i, validationErrors.get(i), null));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Rezept Zubereitungsschritten", validationErrorDtos));
        }
    }

    public void validateRecipeNewRating(RecipeRatingDto recipeRatingDto){
        List<String> validationErrors = new ArrayList<>();

        if (recipeRatingDto.getRecipeId() == null) {
            validationErrors.add("Recipe ID darf nicht leer sein");
        }

        if (recipeRatingDto.getRating() < 0 || recipeRatingDto.getRating() > 5) {
            validationErrors.add("Rezept Bewertung muss zwischen 0 und 5 liegen");
        }

        if (recipeRatingDto.getComment() == null || recipeRatingDto.getComment().isEmpty() || recipeRatingDto.getComment().isBlank()) {
            validationErrors.add("Rezept Kommentar darf nicht leer sein");
        }

        if (recipeRatingDto.getTitle() == null || recipeRatingDto.getTitle().isEmpty() || recipeRatingDto.getTitle().isBlank()) {
            validationErrors.add("Rezept Titel darf nicht leer sein");
        }

        List<ValidationErrorDto> validationErrorDtos = new ArrayList<>();

        for (int i = 0; i < validationErrors.size(); i++) {
            validationErrorDtos.add(new ValidationErrorDto((long) i, validationErrors.get(i), null));
        }

        if (validationErrors.size() > 0) {
            throw new ValidationException(new ValidationErrorRestDto("Validierungsfehler bei Rezept Bewertung", validationErrorDtos));
        }
    }
}
