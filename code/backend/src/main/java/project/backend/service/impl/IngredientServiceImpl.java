package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.backend.dto.IngredientCategoryDto;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.dto.IngredientFilterDto;
import project.backend.dto.PageableDto;
import project.backend.dto.ValidationErrorRestDto;
import project.backend.entity.Ingredient;
import project.backend.entity.IngredientCategory;
import project.backend.exception.ConflictException;
import project.backend.exception.NotFoundException;
import project.backend.mapper.IngredientMapper;
import project.backend.repository.IngredientCategoryRepository;
import project.backend.repository.IngredientRepository;
import project.backend.service.ImageService;
import project.backend.service.IngredientService;
import project.backend.service.validator.IngredientValidator;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientValidator ingredientValidator;
    private final IngredientRepository ingredientRepository;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    private final IngredientMapper ingredientMapper;
    private final ImageService imageService;

    @Override
    public List<IngredientCategoryDto> getIngredientCategories() {
        List<IngredientCategory> ingredientCategories = ingredientCategoryRepository.findAll();
        return ingredientMapper.mapCategoriesToCategoriesDtos(ingredientCategories);
    }

    @Override
    public IngredientDto getIngredient(Long id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);

        if (ingredient.isEmpty()) {
            throw new NotFoundException("Ingredient with id " + id + " not found");
        }

        IngredientDto ingredientDtoToReturn = ingredientMapper.mapIngredientToIngredientDto(ingredient.get());
        ingredientDtoToReturn.setIngredientCategory(ingredient.get().getIngredientCategory().getName());

        return ingredientDtoToReturn;
    }

    @Override
    public PageableDto<IngredientDto> getIngredients(int page, int pageSize) {

        Page<Ingredient> ingredients = ingredientRepository.findAll(PageRequest.of(page, pageSize));
        return getIngredientDtoPageableDto(ingredients);
    }

    @Override
    public PageableDto<IngredientDto> getFilteredIngredients(int page, int pageSize, IngredientFilterDto ingredientFilterDto) {
        Page<Ingredient> ingredients = ingredientRepository.findAllFiltered(PageRequest.of(page, pageSize), ingredientFilterDto);
        return getIngredientDtoPageableDto(ingredients);
    }

    private PageableDto<IngredientDto> getIngredientDtoPageableDto(Page<Ingredient> ingredients) {
        var ingredientsDtos = ingredients.stream().map(ingredientMapper::mapIngredientToIngredientDto).toList();

        for (int i = 0; i < ingredientsDtos.size(); i++) {
            ingredientsDtos.get(i).setIngredientCategory(ingredients.getContent().get(i).getIngredientCategory().getName());
        }

        return new PageableDto<>(
            ingredients.getTotalElements(),
            ingredients.getTotalPages(),
            ingredients.getNumberOfElements(),
            ingredientsDtos
        );
    }

    @Override
    public IngredientDto createIngredient(IngredientCreationDto ingredientDto) {

        ingredientValidator.validateIngredientDtoForCreation(ingredientDto);

        Ingredient ingredient = ingredientMapper.mapIngredientCreationDtoToIngredient(ingredientDto);
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();
        ingredient.setIngredientCategory(ingredientCategory);

        if (ingredientDto.getImageSource() != null){
            ingredient.setImageSource(imageService.uploadImage(ingredientDto.getImageSource()));
        } else {
            // Use default icon from category
            ingredient.setImageSource(ingredientCategory.getIconSource());
        }


        ingredient = ingredientRepository.save(ingredient);

        IngredientDto ingredientDtoToReturn = ingredientMapper.mapIngredientToIngredientDto(ingredient);
        ingredientDtoToReturn.setIngredientCategory(ingredientDto.getIngredientCategory());
        return ingredientDtoToReturn;
    }

    @Override
    public IngredientDto editIngredient(IngredientDto ingredientDto, Long id) {

        ingredientValidator.validateIngredientDtoForEdit(ingredientDto, id);

        Ingredient currentIngredient = ingredientRepository.findById(ingredientDto.getId()).get();

        String imageSource = currentIngredient.getImageSource();

        // Check if we need to handle image upload again
        if (ingredientDto.getImageSource() != null && !ingredientDto.getImageSource().equals(currentIngredient.getImageSource())) {
            imageSource = imageService.uploadImage(ingredientDto.getImageSource());

            IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();

            if (!ingredientCategory.getIconSource().equals(currentIngredient.getImageSource())) {
                imageService.removeImage(currentIngredient.getImageSource());
            }
        } else if (ingredientDto.getImageSource() == null && currentIngredient.getImageSource() != null) {
            IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();

            if (!ingredientCategory.getIconSource().equals(currentIngredient.getImageSource())) {
                imageService.removeImage(currentIngredient.getImageSource());
                imageSource = ingredientCategory.getIconSource();
            }
        }

        IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();
        if (!currentIngredient.getIngredientCategory().equals(ingredientCategory)
            && ((ingredientDto.getImageSource() != null
            && !ingredientDto.getImageSource().startsWith("data:image/"))
            || currentIngredient.getImageSource().length() < 10)
        ){
            imageSource = ingredientCategory.getIconSource();
        }

        Ingredient ingredient = ingredientMapper.mapIngredientDtoToIngredient(ingredientDto);

        ingredient.setIngredientCategory(ingredientCategory);
        ingredient.setImageSource(imageSource);

        ingredient = ingredientRepository.save(ingredient);

        IngredientDto ingredientDtoToReturn = ingredientMapper.mapIngredientToIngredientDto(ingredient);
        ingredientDtoToReturn.setIngredientCategory(ingredientDto.getIngredientCategory());

        return ingredientDtoToReturn;
    }

    @Override
    public void deleteIngredient(Long id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);

        if (ingredient.isEmpty()) {
            throw new NotFoundException("Ingredient with id " + id + " not found");
        }

        if (!ingredient.get().getRecipes().isEmpty()){
            throw new ConflictException(new ValidationErrorRestDto("Ingredient with id " + id + " is used in recipes", null));
        }

        IngredientCategory ingredientCategory = ingredient.get().getIngredientCategory();

        if (!ingredientCategory.getIconSource().equals(ingredient.get().getImageSource())) {
            imageService.removeImage(ingredient.get().getImageSource());
        }

        ingredientRepository.deleteById(id);
    }
}
