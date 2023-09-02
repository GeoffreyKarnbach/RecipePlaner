import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { RecipeDto, LightIngredientDto } from 'src/app/dtos';
import { IngredientService, RecipeService, ToastService } from 'src/app/services';

@Component({
  selector: 'app-recipe-view',
  templateUrl: './recipe-view.component.html',
  styleUrls: ['./recipe-view.component.scss']
})
export class RecipeViewComponent implements OnInit{

  constructor(
    private recipeService: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
    private ingredientService: IngredientService
  ) { }

  id: number = 0;
  recipe: RecipeDto = {
    id: 0,
    name: '',
    description: '',
    difficulty: 0,
    preparationTime: 0,
    mealType: '',
    recipeCategory: '',
    images: [],
    tags: [],
    ingredients: {
      recipeId: 0,
      recipeIngredientItems: []
    },
    steps: {
      recipeId: 0,
      steps: []
    },
    averageRating: 0,
    numberOfRatings: 1
  }

  ratingListNotification: Subject<Boolean> = new Subject<Boolean>();

  recipeRating: number = 3.5;
  activeNewRating: boolean = false;

  ingredients: Map<number, LightIngredientDto> = new Map<number, LightIngredientDto>();

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = Number(params['id']);
      this.recipeService.get(this.id).subscribe(recipe => {
        this.recipe = recipe;
        for (let recipeIngredientItem of this.recipe.ingredients.recipeIngredientItems) {

          this.ingredientService.getLight(recipeIngredientItem.ingredientId).subscribe(ingredient => {
            this.ingredients.set(recipeIngredientItem.ingredientId, ingredient);
          },
          error => {
            this.toastService.showError('Error', 'Ingredient not found');
            this.router.navigate(['/ingredients']);
          });
        }
      },
      error => {
        this.toastService.showError('Error', 'Recipe not found');
        this.router.navigate(['/ingredients']);
      });
    });
  }

  goToRecipeEditPage(): void {
    this.router.navigate(['recipe', 'edit', this.recipe.id]);
  }

  getPreparationTime(): string {
    if (this.recipe.preparationTime > 60) {
      return Math.floor(this.recipe.preparationTime / 60) + ' h ' + this.recipe.preparationTime % 60 + ' min';
    }

    return this.recipe.preparationTime + ' min';
  }

  deleteRecipe(): void {

    this.recipeService.delete(this.recipe.id).subscribe(
      (data) => {
        console.log(data);
        this.toastService.showSuccess('Success', 'Recipe deleted');
        this.router.navigate(['/recipe']);
      }
    );

  }

  getDifficultyFiledArray(): number[] {
    return Array(this.recipe.difficulty).fill(0);
  }

  getDifficultyEmptyArray(): number[] {
    return Array(5 - this.recipe.difficulty).fill(0);
  }

  getMealType(): string {
    switch (this.recipe.mealType) {
      case 'BREAKFAST':
        return 'Frühstück';
      case 'LUNCH':
        return 'Mittagessen';
      case 'DINNER':
        return 'Abendessen';
      case 'SNACK':
        return 'Snack';
      case 'OTHER':
        return 'Andere';
      default:
        return '';
    }
  }

  startNewRating(): void {
    this.activeNewRating = true;
  }

  completedOrCanceledRating(): void {
    this.activeNewRating = false;

    this.ratingListNotification.next(true);
  }
}
