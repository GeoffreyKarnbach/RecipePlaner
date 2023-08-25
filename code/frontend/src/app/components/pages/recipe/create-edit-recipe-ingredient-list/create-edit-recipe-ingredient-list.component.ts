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

  ingredientPositionToEdit: number = -1;
  ingredientAmountUpdateValue: number = 0;

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.recipeId = params['id'];
      this.recipeService.get(this.recipeId).subscribe(
        (recipe) => {
          this.recipeId = recipe.id;
          this.recipeIngredientList.recipeId = recipe.id;
          this.recipe = recipe;
          this.ingredientService.getAll().subscribe(
            (ingredients) => {
              this.ingredients = ingredients;

              this.ingredientHashMap = new Map<number, IngredientDto>();
              for (let ingredient of this.ingredients) {
                this.ingredientHashMap.set(ingredient.id, ingredient);
              }

              this.recipeService.getRecipeIngredientList(this.recipeId).subscribe(
                (recipeIngredientList) => {
                  this.recipeIngredientList = recipeIngredientList;

                  for (let recipeIngredientItem of this.recipeIngredientList.recipeIngredientItems) {
                    this.ingredients = this.ingredients.filter((ingredient) => {
                      return ingredient.id != recipeIngredientItem.ingredientId;
                    });
                  }
                },
                (error) => {
                  this.toastService.showError(error.error, 'Fehler');
                });
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

    // IngredientName has to exist in this.ingredients
    let ingredientExists = false;
    for (let ingredient of this.ingredients) {
      if (ingredient.name == this.currentIngredientName) {
        ingredientExists = true;
        break;
      }
    }

    if (!ingredientExists) {
      this.toastService.showError('Die Zutat existiert nicht.', 'Fehler');
      return;
    }

    if (this.currentIngredientAmount <= 0) {
      this.toastService.showError('Die Menge muss größer als 0 sein.', 'Fehler');
      return;
    }

    this.recipeIngredientList.recipeIngredientItems.push({
      ingredientId: this.getIngredientIdByName(this.currentIngredientName),
      amount: this.currentIngredientAmount
    });

    // Remove ingredient from list
    this.ingredients = this.ingredients.filter((ingredient) => {
      return ingredient.name != this.currentIngredientName;
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
    return this.getUnitDisplayValue(this.ingredientHashMap.get(id).unit.toString());
  }

  getUnitDisplayValue(unit: string): string {
    switch (unit) {
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

  startUpdateIngredientAmount(position: number) {
    this.ingredientPositionToEdit = position;
    this.ingredientAmountUpdateValue = this.recipeIngredientList.recipeIngredientItems[position].amount;
  }

  updateIngredientAmount(position: number) {
    this.recipeIngredientList.recipeIngredientItems[position].amount = this.ingredientAmountUpdateValue;
    this.ingredientAmountUpdateValue = 0;
    this.ingredientPositionToEdit = -1;
  }

  removeIngredientFromList(position: number) {
    this.ingredients.push(this.ingredientHashMap.get(this.recipeIngredientList.recipeIngredientItems[position].ingredientId));
    this.recipeIngredientList.recipeIngredientItems.splice(position, 1);
  }

  saveRecipeIngredientList() {
    this.recipeService.editRecipeIngredientList(this.recipeIngredientList).subscribe(
      (response) => {
        this.toastService.showSuccess('Die Zutatenliste wurde erfolgreich gespeichert.', 'Erfolg');
        this.router.navigate(['/recipe', this.recipeId]);
      },
      (error) => {
        this.toastService.showErrorResponse(error);
    });
  }
}
