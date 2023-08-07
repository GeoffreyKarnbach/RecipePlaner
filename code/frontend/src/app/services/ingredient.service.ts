import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global';
import { IngredientCategoryDto, IngredientCreationDto, IngredientDto } from '../dtos';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class IngredientService {
  private ingredientBaseUri: string = this.globals.backendUri + '/ingredient';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  create(ingredient: IngredientCreationDto): Observable<IngredientDto> {
    return this.httpClient.post<IngredientDto>(`${this.ingredientBaseUri}`, ingredient);
  }

  get(id: number): Observable<IngredientDto> {
    return this.httpClient.get<IngredientDto>(`${this.ingredientBaseUri}/${id}`);
  }

  edit(ingredient: IngredientDto): Observable<IngredientDto> {
    return this.httpClient.put<IngredientDto>(`${this.ingredientBaseUri}/${ingredient.id}`, ingredient);
  }

  getAllIngredientCategories(): Observable<IngredientCategoryDto[]> {
    return this.httpClient.get<IngredientCategoryDto[]>(`${this.ingredientBaseUri}/categories`);
  }

}
