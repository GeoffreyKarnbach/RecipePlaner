import { IngredientUnit } from "../enums";

export interface IngredientFilterDto {
  filterName: string;
  filterCategory: string;
  filterUnit: IngredientUnit;
  filterCriteria: string;
}
