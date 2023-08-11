import { Component, OnInit } from '@angular/core';
import { IngredientDto } from 'src/app/dtos';
import { IngredientService } from 'src/app/services';

@Component({
  selector: 'app-ingredient-list-view',
  templateUrl: './ingredient-list-view.component.html',
  styleUrls: ['./ingredient-list-view.component.scss']
})
export class IngredientListViewComponent implements OnInit {

  constructor(
    private ingredientService: IngredientService,
  ) { }

  currentPage: number = 0;
  pageSize: number = 10;

  totalResults: number = 0;
  totalPages: number = 0;
  resultCount: number = 0;
  ingredients: IngredientDto[] = [];


  ngOnInit(): void {
    this.refreshData();
  }

  refreshData(): void {
    this.ingredientService.getAll(this.currentPage, this.pageSize).subscribe(
      (data) => {
        this.ingredients = data.result;
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
    this.refreshData();
  }

}
