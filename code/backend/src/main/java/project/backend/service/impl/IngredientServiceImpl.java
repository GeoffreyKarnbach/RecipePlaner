package project.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.backend.dto.IngredientCategoryDto;
import project.backend.dto.IngredientCreationDto;
import project.backend.dto.IngredientDto;
import project.backend.entity.Ingredient;
import project.backend.entity.IngredientCategory;
import project.backend.mapper.IngredientMapper;
import project.backend.repository.IngredientCategoryRepository;
import project.backend.repository.IngredientRepository;
import project.backend.service.ImageService;
import project.backend.service.IngredientService;
import project.backend.service.validator.IngredientValidator;

import java.util.List;

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
    public IngredientDto createIngredient(IngredientCreationDto ingredientDto) {

        ingredientValidator.validateIngredientDtoForCreation(ingredientDto);

        Ingredient ingredient = ingredientMapper.mapIngredientCreationDtoToIngredient(ingredientDto);
        IngredientCategory ingredientCategory = ingredientCategoryRepository.findIngredientCategoryByName(ingredientDto.getIngredientCategory()).get();
        ingredient.setIngredientCategory(ingredientCategory);

        ingredient.setImageSource(imageService.uploadImage(ingredientDto.getImageSource()));

        ingredient = ingredientRepository.save(ingredient);

        IngredientDto ingredientDtoToReturn = ingredientMapper.mapIngredientToIngredientDto(ingredient);
        ingredientDtoToReturn.setIngredientCategory(ingredientDto.getIngredientCategory());
        return ingredientDtoToReturn;
    }
}
