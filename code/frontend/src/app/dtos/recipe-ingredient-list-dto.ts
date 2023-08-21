import { RecipeIngredientItemDto } from ".";

export interface RecipeIngredientListDto {
  recipeId: number;
  recipeIngredientItems: RecipeIngredientItemDto[];
}
