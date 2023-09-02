import { Component } from '@angular/core';
import { IngredientDto, LightRecipeDto } from 'src/app/dtos';
import { IngredientService, RecipeService } from 'src/app/services';

@Component({
  selector: 'app-recipe-list-view',
  templateUrl: './recipe-list-view.component.html',
  styleUrls: ['./recipe-list-view.component.scss']
})
export class RecipeListViewComponent {
  constructor(
    private recipeService: RecipeService,
    private ingredientService: IngredientService
  ) { }

  currentPage: number = 0;
  pageSize: number = 10;

  totalResults: number = 0;
  totalPages: number = 0;
  resultCount: number = 0;
  recipes: LightRecipeDto[] = [];

  tags: string[] = [''];
  categories: string[] = [""];

  selectedTag: string = null;
  currentIngredientName: string = '';

  tagsDtos: string[] = [];
  ingredientDtos: IngredientDto[] = [];

  ingredients: IngredientDto[] = [];
  ingredientHashMap: Map<number, IngredientDto>;

  ngOnInit(): void {

    this.recipeService.getAllRecipeTags().subscribe(
      (tags) => {
        this.tags = tags;
        this.tags.sort();

      }
    );

    this.recipeService.getAllRecipeCategories().subscribe(
      (categories) => {
        this.categories = categories.map((category) => category.name);
      }
    );

    this.ingredientService.getAll().subscribe(
      (ingredients) => {
        this.ingredients = ingredients;
        this.ingredientHashMap = new Map<number, IngredientDto>();
        for (let ingredient of this.ingredients) {
          this.ingredientHashMap.set(ingredient.id, ingredient);
        }
      }
    );

    this.refreshData();
  }

  refreshData(): void {
    this.recipeService.getAll(this.currentPage, this.pageSize).subscribe(
      (data) => {
        this.recipes = data.result;
        this.totalResults = data.totalResults;
        this.totalPages = data.totalPages;
        this.resultCount = data.resultCount;
      }
    );
  }

  nextPage(): void {
    this.currentPage++;
    this.refreshData();
  }

  previousPage(): void {
    this.currentPage--;
    this.refreshData();
  }

  deletedElement($event: any): void {

    if (this.recipes.length === 1 && this.currentPage !== 0) {
      // If last element on page, go back one page
      this.currentPage--;
    }

    this.refreshData();
  }

  searchFilteredPreparation() {
    console.log("searchFilteredPreparation");
  }

  onTagAdded(): void {
    if (this.selectedTag) {
      this.tags = this.tags.filter(tag => tag !== this.selectedTag);
      this.tagsDtos.push(this.selectedTag);
      this.selectedTag = null;
      this.tagsDtos.sort();
    }
  }

  onIngredientAdded(): void {
    if (this.currentIngredientName === '') {
      return;
    }

    const ingredientId = this.getIngredientIdByName(this.currentIngredientName);
    this.ingredientDtos.push(this.ingredientHashMap.get(ingredientId));
    this.currentIngredientName = '';

    this.ingredients = this.ingredients.filter((ingredient) => ingredient.id !== ingredientId);
  }

  deleteTag($event: number) {
    const tagID = $event - 1;

    this.tags.push(this.tagsDtos.splice(tagID, 1)[0]);
    this.tagsDtos.sort();
    this.tags.sort();
  }

  deleteIngredient($event: number) {
    const ingredientID = $event;

    this.ingredients.push(this.ingredientDtos.splice(ingredientID, 1)[0]);
  }

  getIngredientImageById(id: number): string {
    return this.ingredientHashMap.get(id).imageSource;
  }

  getIngredientIdByName(name: string): number {
    return this.ingredients.find((ingredient) => ingredient.name === name).id;
  }
}
