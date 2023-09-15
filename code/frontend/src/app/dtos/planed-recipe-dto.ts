export interface PlanedRecipeDto {
  id: number;
  recipeId: number;
  recipeName: string;
  date: Date;
  meal: string;
  comment: string;
  portionCount: number;
}
