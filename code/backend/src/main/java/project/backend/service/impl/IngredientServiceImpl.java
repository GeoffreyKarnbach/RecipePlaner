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
import project.backend.dto.LightIngredientDto;
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

import java.util.ArrayList;
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
            // New image uploaded, remove old one
            imageSource = imageService.uploadImage(ingredientDto.getImageSource());

            IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();

            if (!ingredientCategory.getIconSource().equals(currentIngredient.getImageSource())) {
                if (!currentIngredient.getImageSource().endsWith("_ing.png"))
                {
                    imageService.removeImage(currentIngredient.getImageSource());
                }
            }
        } else if (ingredientDto.getImageSource() == null && currentIngredient.getImageSource() != null) {
            // Image removed, remove old one, use default icon from category
            IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();

            if (!ingredientCategory.getIconSource().equals(currentIngredient.getImageSource())) {
                if (!currentIngredient.getImageSource().endsWith("_ing.png"))
                {
                    imageService.removeImage(currentIngredient.getImageSource());
                }
                imageSource = ingredientCategory.getIconSource();
            }
        }

        IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();

        if (!currentIngredient.getIngredientCategory().getId().equals(ingredientCategory.getId())
            && ingredientDto.getImageSource() != null
            && ingredientDto.getImageSource().equals(currentIngredient.getImageSource())
            && currentIngredient.getImageSource().endsWith("_ing.png")
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

    @Override
    public List<IngredientDto> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientDto> ingredientDtos = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            IngredientDto ingredientDto = ingredientMapper.mapIngredientToIngredientDto(ingredient);
            ingredientDto.setIngredientCategory(ingredient.getIngredientCategory().getName());
            ingredientDtos.add(ingredientDto);
        }

        return ingredientDtos;
    }

    @Override
    public LightIngredientDto getLightIngredient(Long id) {
        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        if (ingredient.isEmpty()) {
            throw new NotFoundException("Ingredient with id " + id + " not found");
        }

        LightIngredientDto lightIngredientDto = new LightIngredientDto();
        lightIngredientDto.setId(ingredient.get().getId());
        lightIngredientDto.setName(ingredient.get().getName());
        lightIngredientDto.setImageSource(ingredient.get().getImageSource());
        lightIngredientDto.setUnit(ingredient.get().getUnit());

        return lightIngredientDto;
    }

    @Override
    public void changeIngredientInventoryCount(Long id, int amount) {

        Optional<Ingredient> optIngredient = ingredientRepository.findById(id);
        if (optIngredient.isEmpty()) {
            throw new NotFoundException("Ingredient with id " + id + " not found");
        }

        Ingredient ingredient = optIngredient.get();
        float newAmount = ingredient.getCount() + amount;

        if (newAmount < 0) {
            throw new ConflictException(new ValidationErrorRestDto("Ingredient with id " + id + " has not enough items in inventory", null));
        }

        ingredient.setCount(newAmount);
        ingredientRepository.save(ingredient);
    }
}
