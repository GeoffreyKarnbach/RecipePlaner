import { IngredientDto } from "./ingredient-dto";

export interface RecipeFilterDto {
  name: string;
  category: string;
  mealType: string;
  maxPreparationTime: number;
  minDifficulty: number;
  maxDifficulty: number;
  tags: string[];
  ingredients: IngredientDto[];
  filterCriteria: string;
}
