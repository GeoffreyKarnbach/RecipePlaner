import { IngredientUnit } from "../enums";

export interface IngredientCreationDto {
  name: string;
  imageSource: string;
  unit: IngredientUnit;
  count: number;
  ingredientCategory: string;
}
