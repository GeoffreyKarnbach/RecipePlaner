export interface RecipeDto {
  id: number;
  name: string;
  description: string;
  difficulty: number;
  preparationTime: number;
  mealType: string;
  recipeCategory: string;
  images: string[];
}
