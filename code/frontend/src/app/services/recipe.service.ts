import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global';
import { Observable } from 'rxjs';
import { LightRecipeDto, Pageable, RecipeCategoryDto, RecipeCreationDto, RecipeDto } from '../dtos';

@Injectable({
  providedIn: 'root',
})
export class RecipeService {
  private recipeBaseUri: string = this.globals.backendUri + '/recipe';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  getAllRecipeCategories(): Observable<RecipeCategoryDto[]> {
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

  getAll(
    page_: number,
    pageSize_: number,
  ): Observable<Pageable<LightRecipeDto>> {
    return this.httpClient.get<Pageable<LightRecipeDto>>(`${this.recipeBaseUri}/all`,
      { params: { page: page_, pageSize: pageSize_ } });
  }
}
