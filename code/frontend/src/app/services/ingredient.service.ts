import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global';
import { IngredientCategoryDto, IngredientCreationDto, IngredientDto, IngredientFilterDto, LightIngredientDto, Pageable } from '../dtos';
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

  getLight(id: number): Observable<LightIngredientDto> {
    return this.httpClient.get<LightIngredientDto>(`${this.ingredientBaseUri}/${id}/light`);
  }

  edit(ingredient: IngredientDto): Observable<IngredientDto> {
    return this.httpClient.put<IngredientDto>(`${this.ingredientBaseUri}/${ingredient.id}`, ingredient);
  }

  delete(id: number): Observable<any> {
    return this.httpClient.delete<any>(`${this.ingredientBaseUri}/${id}`);
  }

  getAllIngredientCategories(): Observable<IngredientCategoryDto[]> {
    return this.httpClient.get<IngredientCategoryDto[]>(`${this.ingredientBaseUri}/categories`);
  }

  getAll(): Observable<IngredientDto[]> {
    return this.httpClient.get<IngredientDto[]>(`${this.ingredientBaseUri}/all`);
  }

  getAllFiltered(
    page_: number,
    pageSize_: number,
    filterDto: IngredientFilterDto
  ) : Observable<Pageable<IngredientDto>> {
    return this.httpClient.post<Pageable<IngredientDto>>(`${this.ingredientBaseUri}/filter`, filterDto, { params: { page: page_, pageSize: pageSize_ } });
  }

  addIngredientToInventory(ingredientId: number, amount: number): Observable<IngredientDto> {
    return this.httpClient.post<IngredientDto>(`${this.ingredientBaseUri}/add/${ingredientId}?amount=${amount}`, null);
  }


}
