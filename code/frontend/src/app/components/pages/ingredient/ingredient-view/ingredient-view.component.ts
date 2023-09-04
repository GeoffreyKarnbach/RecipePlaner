import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IngredientDto, LightRecipeDto } from 'src/app/dtos';
import { RecipeFilterDto } from 'src/app/dtos/recipe-filter-dto';
import { IngredientUnit } from 'src/app/enums';
import { IngredientService, RecipeService, ToastService } from 'src/app/services';

@Component({
  selector: 'app-ingredient-view',
  templateUrl: './ingredient-view.component.html',
  styleUrls: ['./ingredient-view.component.scss']
})
export class IngredientViewComponent implements OnInit{

  constructor(
    private ingredientService: IngredientService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
    private recipeService: RecipeService
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {

        this.id = Number(params['id']);
        this.ingredientService.get(this.id).subscribe(ingredient => {
          this.ingredient = ingredient;
          this.recipeFilterDto.ingredients.push(this.ingredient);
          this.getRecipeContainingIngredient();
        },
        error => {
          this.router.navigate(['/ingredients']);
        });
      });
  }

  id: number = 0;

  ingredient: IngredientDto = {
    id: 0,
    name: 'Default Name',
    imageSource: 'defaultImageSource',
    unit: IngredientUnit.GRAM,
    count: 0,
    ingredientCategory: 'Default Category'
  }

  // Recipe containing ingredient section


  currentPage: number = 0;
  pageSize: number = 5;

  totalResults: number = 0;
  totalPages: number = 0;
  resultCount: number = 0;
  recipes: LightRecipeDto[] = [];

  loadingComplete: boolean = false;

  recipeFilterDto: RecipeFilterDto = {
    name: '',
    category: null,
    mealType: null,
    maxPreparationTime: null,
    minDifficulty: 0,
    maxDifficulty: 5,
    tags: [],
    ingredients: [],
    filterCriteria: 'CREATION_DATE_ASCENDING'
  };

  //

  getIngredientUnitText(): string {
    switch (this.ingredient.unit.toString()) {
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

  deleteIngredient(): void {
    this.ingredientService.delete(this.ingredient.id).subscribe(
      (data) => {
        this.toastService.showSuccess('Zutat erfolgreich gelöscht');
        this.router.navigate(['/ingredient']);
      }
    );
  }

  goToIngredientEditPage(): void {
    console.log('goToIngredientEditPage()');
    this.router.navigate(['ingredient', 'edit', this.ingredient.id]);
  }

  nextPage(): void {
    this.currentPage++;
    this.getRecipeContainingIngredient();
  }

  previousPage(): void {
    this.currentPage--;
    this.getRecipeContainingIngredient();
  }

  getRecipeContainingIngredient() {
    this.loadingComplete = false;

    this.recipeService.getAllFiltered(this.currentPage, this.pageSize, this.recipeFilterDto).subscribe(
      (data) => {

        this.recipes = data.result;
        this.totalResults = data.totalResults;
        this.totalPages = data.totalPages;
        this.resultCount = data.resultCount;

        this.loadingComplete = true;
      }
    );
  }
}
