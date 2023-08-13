import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global';
import { Observable } from 'rxjs';
import { RecipeCategoryDto, RecipeCreationDto, RecipeDto } from '../dtos';

@Injectable({
  providedIn: 'root',
})
export class RecipeService {
  private recipeBaseUri: string = this.globals.backendUri + '/recipe';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  getAllIngredientCategories(): Observable<RecipeCategoryDto[]> {
    return this.httpClient.get<RecipeCategoryDto[]>(`${this.recipeBaseUri}/categories`);
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
}
