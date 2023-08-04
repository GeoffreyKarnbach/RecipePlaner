import { IngredientUnit } from "../enums";

export interface IngredientDto {
  id: number;
  name: string;
  imageSource: string;
  unit: IngredientUnit;
  count: number;
  ingredientCategory: string;
}
