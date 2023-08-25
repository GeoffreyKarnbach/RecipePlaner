import { IngredientUnit } from "../enums";

export interface LightIngredientDto {
  id: number;
  name: string;
  imageSource: string;
  unit: IngredientUnit;
}
