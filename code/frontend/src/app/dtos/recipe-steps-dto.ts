import { RecipeSingleStepDto } from ".";

export interface RecipeStepsDto {
  recipeId: number;
  steps: RecipeSingleStepDto[];
}
