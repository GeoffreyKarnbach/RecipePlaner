import { RecipeIngredientListDto } from "./recipe-ingredient-list-dto";
import { RecipeStepsDto } from "./recipe-steps-dto";

export interface RecipeDto {
  id: number;
  name: string;
  description: string;
  difficulty: number;
  preparationTime: number;
  mealType: string;
  recipeCategory: string;
  images: string[];
  tags: string[];
  ingredients: RecipeIngredientListDto;
  steps: RecipeStepsDto;
  averageRating: number;
  numberOfRatings: number;
}
