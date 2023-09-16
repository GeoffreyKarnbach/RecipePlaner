import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global';
import { Observable } from 'rxjs';
import { PlannedShoppingCreationDto, PlannedShoppingDto } from '../dtos';

@Injectable({
  providedIn: 'root',
})
export class ShoppingService {
  private shoppingBaseUri: string = this.globals.backendUri + '/shopping';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  createdPlannedShopping(
    plannedShopping: PlannedShoppingCreationDto
  ): Observable<PlannedShoppingDto> {
    return this.httpClient.post<PlannedShoppingDto>(
      this.shoppingBaseUri,
      plannedShopping
    );
  }

  getPlannedShopping(
    year: number,
    month: number,
  ): Observable<Map<number, PlannedShoppingDto[]>> {
    return this.httpClient.get<Map<number, PlannedShoppingDto[]>>(`${this.shoppingBaseUri}/planned?year=${year}&month=${month}`);
  }

  getPlannedShoppingById(id: number): Observable<PlannedShoppingDto> {
    return this.httpClient.get<PlannedShoppingDto>(`${this.shoppingBaseUri}/${id}`);
  }

}
