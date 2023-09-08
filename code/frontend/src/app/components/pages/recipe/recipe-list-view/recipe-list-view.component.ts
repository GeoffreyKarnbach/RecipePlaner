import { Component } from '@angular/core';
import { IngredientDto, LightRecipeDto } from 'src/app/dtos';
import { RecipeFilterDto } from 'src/app/dtos/recipe-filter-dto';
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

  // Pagination
  currentPage: number = 0;
  pageSize: number = 10;

  totalResults: number = 0;
  totalPages: number = 0;
  resultCount: number = 0;
  recipes: LightRecipeDto[] = [];

  loadingComplete: boolean = false;

  // Selected tag and ingredient
  selectedTag: string = null;
  currentIngredientName: string = '';

  // All available tags, categories and ingredients
  tags: string[] = [''];
  categories: string[] = [""];

  ingredients: IngredientDto[] = [];
  ingredientHashMap: Map<number, IngredientDto>;

  // DTO to send to backend
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

    this.searchFilteredPreparation();
  }

  nextPage(): void {
    this.currentPage++;
    this.searchFilteredPreparation();
  }

  previousPage(): void {
    this.currentPage--;
    this.searchFilteredPreparation();
  }

  deletedElement($event: any): void {

    if (this.recipes.length === 1 && this.currentPage !== 0) {
      // If last element on page, go back one page
      this.currentPage--;
    }

    this.searchFilteredPreparation();
  }

  searchFilteredPreparation() {

    if (this.recipeFilterDto.mealType === 'null'){
      this.recipeFilterDto.mealType = null;
    }

    if (this.recipeFilterDto.category === 'null'){
      this.recipeFilterDto.category = null;
    }

    if (this.recipeFilterDto.maxPreparationTime < 0){
      this.recipeFilterDto.maxPreparationTime = 0;
    }

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

  onIngredientAdded(): void {
    if (this.currentIngredientName === '') {
      return;
    }

    const ingredientId = this.getIngredientIdByName(this.currentIngredientName);
    this.recipeFilterDto.ingredients.push(this.ingredientHashMap.get(ingredientId));
    this.currentIngredientName = '';

    this.ingredients = this.ingredients.filter((ingredient) => ingredient.id !== ingredientId);

    this.searchFilteredPreparation();
  }

  deleteIngredient($event: number) {
    const ingredientID = $event;

    this.ingredients.push(this.recipeFilterDto.ingredients.splice(ingredientID, 1)[0]);
    this.ingredients.sort((a, b) => a.name.localeCompare(b.name));

    this.searchFilteredPreparation();
  }

  onTagAdded(): void {
    if (this.selectedTag) {
      this.tags = this.tags.filter(tag => tag !== this.selectedTag);
      this.recipeFilterDto.tags.push(this.selectedTag);
      this.selectedTag = null;
      this.recipeFilterDto.tags.sort();

      this.searchFilteredPreparation();
    }
  }

  deleteTag($event: number) {
    const tagID = $event - 1;

    this.tags.push(this.recipeFilterDto.tags.splice(tagID, 1)[0]);
    this.recipeFilterDto.tags.sort();
    this.tags.sort();

    this.searchFilteredPreparation();
  }

  getIngredientImageById(id: number): string {
    return this.ingredientHashMap.get(id).imageSource;
  }

  getIngredientIdByName(name: string): number {
    return this.ingredients.find((ingredient) => ingredient.name === name).id;
  }
}
