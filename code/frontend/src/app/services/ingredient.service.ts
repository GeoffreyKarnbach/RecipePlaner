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

  getAllIngredientCategories(): Observable<IngredientCategoryDto[]> {
    return this.httpClient.get<IngredientCategoryDto[]>(`${this.ingredientBaseUri}/categories`);
  }

}
