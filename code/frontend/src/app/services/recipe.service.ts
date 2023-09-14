import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global';
import { Observable } from 'rxjs';
import { LightRecipeDto, Pageable, PlanedRecipeCreationDto, PlanedRecipeDto, RecipeCategoryDto, RecipeCreationDto, RecipeDto, RecipeIngredientListDto, RecipeRatingDto, RecipeStepsDto } from '../dtos';
import { RecipeFilterDto } from '../dtos/recipe-filter-dto';

@Injectable({
  providedIn: 'root',
})
export class RecipeService {
  private recipeBaseUri: string = this.globals.backendUri + '/recipe';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  getAllRecipeCategories(): Observable<RecipeCategoryDto[]> {
    return this.httpClient.get<RecipeCategoryDto[]>(`${this.recipeBaseUri}/categories`);
  }

  getAllRecipeTags(): Observable<string[]> {
    return this.httpClient.get<string[]>(`${this.recipeBaseUri}/tags`);
  }

  create(recipe: RecipeCreationDto): Observable<RecipeDto> {
    return this.httpClient.post<RecipeDto>(`${this.recipeBaseUri}`, recipe);
  }

  get(id: number): Observable<RecipeDto> {
    return this.httpClient.get<RecipeDto>(`${this.recipeBaseUri}/${id}`);
  }

  edit(recipe: RecipeDto, id: number): Observable<RecipeDto> {
    return this.httpClient.put<RecipeDto>(`${this.recipeBaseUri}/${id}`, recipe);
  }

  getAll(
    page_: number,
    pageSize_: number,
  ): Observable<Pageable<LightRecipeDto>> {
    return this.httpClient.get<Pageable<LightRecipeDto>>(`${this.recipeBaseUri}/all`,
      { params: { page: page_, pageSize: pageSize_ } });
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete<any>(`${this.recipeBaseUri}/${id}`);
  }

  editRecipeIngredientList(recipeIngredientList: RecipeIngredientListDto): Observable<any> {
    return this.httpClient.post<any>(`${this.recipeBaseUri}/ingredient-list`, recipeIngredientList);
  }

  getRecipeIngredientList(id: number): Observable<RecipeIngredientListDto> {
    return this.httpClient.get<RecipeIngredientListDto>(`${this.recipeBaseUri}/${id}/ingredient-list`);
  }

  updateSteps(stepsDto: RecipeStepsDto): Observable<any> {
    return this.httpClient.post<any>(`${this.recipeBaseUri}/steps`, stepsDto);
  }

  getSteps(recipeId: number): Observable<RecipeStepsDto> {
    return this.httpClient.get<RecipeStepsDto>(`${this.recipeBaseUri}/${recipeId}/steps`);
  }

  saveRating(rating: RecipeRatingDto): Observable<any> {
    return this.httpClient.post<any>(`${this.recipeBaseUri}/rating`, rating);
  }

  getRatings(
    page: number,
    pageSize: number,
    recipeId: number
  ): Observable<Pageable<RecipeRatingDto>> {
    return this.httpClient.get<Pageable<RecipeRatingDto>>(`${this.recipeBaseUri}/${recipeId}/ratings`,
      { params: { page: page, pageSize: pageSize } });
  }

  getAllFiltered(
    page_: number,
    pageSize_: number,
    filterDto: RecipeFilterDto
  ) : Observable<Pageable<LightRecipeDto>> {
    return this.httpClient.post<Pageable<LightRecipeDto>>(`${this.recipeBaseUri}/filter`, filterDto, { params: { page: page_, pageSize: pageSize_ } });
  }

  cookRecipe(
    recipeId: number,
    ingredientList: RecipeIngredientListDto
  ) : Observable<any> {
    return this.httpClient.post<any>(`${this.recipeBaseUri}/cook/${recipeId}`, ingredientList);
  }

  planRecipe(
    recipeId: number,
    planedRecipeCreationDto: PlanedRecipeCreationDto
  ): Observable<PlanedRecipeDto> {
    return this.httpClient.post<PlanedRecipeDto>(`${this.recipeBaseUri}/plan/${recipeId}`, planedRecipeCreationDto);
  }

  getPlanedRecipes(
    year: number,
    month: number,
  ): Observable<Map<number, PlanedRecipeDto[]>> {
    return this.httpClient.get<Map<number, PlanedRecipeDto[]>>(`${this.recipeBaseUri}/planned?year=${year}&month=${month}`);
  }
}
