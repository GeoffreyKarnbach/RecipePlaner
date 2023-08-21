import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IngredientDto, RecipeDto, RecipeIngredientListDto } from 'src/app/dtos';
import { IngredientService, RecipeService, ToastService } from 'src/app/services';

@Component({
  selector: 'app-create-edit-recipe-ingredient-list',
  templateUrl: './create-edit-recipe-ingredient-list.component.html',
  styleUrls: ['./create-edit-recipe-ingredient-list.component.scss']
})
export class CreateEditRecipeIngredientListComponent implements OnInit {

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private toastService: ToastService,
    private ingredientService: IngredientService
  ) { }

  recipeId: number = 0;
  recipe: RecipeDto;

  recipeIngredientList: RecipeIngredientListDto = {
    recipeId: 0,
    recipeIngredientItems: []
  }

  currentIngredientName: string = '';
  currentIngredientAmount: number = 0;

  ingredients: IngredientDto[] = [];
  ingredientHashMap: Map<number, IngredientDto>;

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.recipeId = params['id'];
      this.recipeService.get(this.recipeId).subscribe(
        (recipe) => {
          this.recipeId = recipe.id;
          this.recipe = recipe;
          this.ingredientService.getAll().subscribe(
            (ingredients) => {
              this.ingredients = ingredients;

              this.ingredientHashMap = new Map<number, IngredientDto>();
              for (let ingredient of this.ingredients) {
                this.ingredientHashMap.set(ingredient.id, ingredient);
              }
            },
            (error) => {
              this.toastService.showError(error.error, 'Fehler');
            }
          );
        },
        (error) => {
          this.router.navigate(['/recipe']);
          this.toastService.showError(error.error, 'Fehler');
        }
      );
    });
  }

  addIngredient() {
    if (this.currentIngredientName == '') {
      return;
    }

    this.recipeIngredientList.recipeIngredientItems.push({
      ingredientId: this.getIngredientIdByName(this.currentIngredientName),
      amount: this.currentIngredientAmount
    });

    this.currentIngredientName = '';
    this.currentIngredientAmount = 0;
    console.log(this.recipeIngredientList);
  }

  getIngredientIdByName(name: string): number {
    for (let ingredient of this.ingredients) {
      if (ingredient.name == name) {
        return ingredient.id;
      }
    }
    return -1;
  }

  getIngredientNameById(id: number): string {
    return this.ingredientHashMap.get(id).name;
  }

  getIngredientImageById(id: number): string {
    return this.ingredientHashMap.get(id).imageSource;
  }

  getIngredientUnitById(id: number): string {
    switch (this.ingredientHashMap.get(id).unit.toString()) {
      case "GRAM":
        return 'Gramm';
      case "MILLILITER":
        return 'Milliliter';
      case "PIECE":
        return 'Stück';
      default:
        return 'Unbekannt';
    }
  }
}
