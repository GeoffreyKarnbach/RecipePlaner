import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeDto } from 'src/app/dtos';
import { RecipeService, ToastService } from 'src/app/services';

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
    private toastService: ToastService
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
    tags: []
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {

        this.id = Number(params['id']);
        this.recipeService.get(this.id).subscribe(recipe => {
          this.recipe = recipe;
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

    /*
    this.recipeService.delete(this.recipe.id).subscribe(
      (data) => {
        console.log(data);
        this.toastService.showSuccess('Success', 'Recipe deleted');
        this.router.navigate(['/recipe']);
      }
    );
    */
    console.log('delete');
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
}
