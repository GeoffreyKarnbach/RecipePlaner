import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeCreationDto } from 'src/app/dtos';
import { RecipeService, ToastService } from 'src/app/services';

export enum RecipeCreateEditModes {
  CREATE,
  EDIT,
}

@Component({
  selector: 'app-create-edit-recipe',
  templateUrl: './create-edit-recipe.component.html',
  styleUrls: ['./create-edit-recipe.component.scss']
})
export class CreateEditRecipeComponent implements OnInit{

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data['mode'];
    });

    this.recipeService.getAllIngredientCategories().subscribe(
      (categories) => {
        this.categories = categories.map((category) => category.name);
      }
    );
  }

  public recipe: RecipeCreationDto = {
    name: '',
    description: '',
    difficulty: 0,
    preparationTime: 0,
    mealType: '',
    recipeCategory: ''
  }

  public categories: string[] = [""];

  mode: RecipeCreateEditModes = RecipeCreateEditModes.CREATE;

  submitText(): string {
    return this.mode === RecipeCreateEditModes.CREATE ? 'Erstellen' : 'Bearbeiten';
  }

  titleText(): string {
    return this.mode === RecipeCreateEditModes.CREATE ? 'Rezept erstellen' : 'Rezept bearbeiten';
  }

  onSubmit(form: NgForm): void {
    if (this.recipe.difficulty === 0){
      this.toastService.showError('Bitte Schwierigkeit auswählen', 'Fehler');
      return
    }

    if (this.mode === RecipeCreateEditModes.CREATE) {
      this.recipeService.create(this.recipe).subscribe(
        (recipe) => {
          console.log(recipe);
          this.toastService.showSuccess('Rezept erstellt', 'Erfolg');
          this.router.navigate(['/recipe']);
        },
        (error) => {
          this.toastService.showErrorResponse(error);
        }
      );
    }
  }

}
