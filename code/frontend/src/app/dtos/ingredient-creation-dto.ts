import { IngredientUnit } from "../enums";
import { IngredientCategoryDto } from ".";

export interface IngredientCreationDto {
  name: string;
  imageSource: string;
  unit: IngredientUnit;
  count: number;
  ingredientCategory: IngredientCategoryDto;
}
