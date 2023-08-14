import { Component } from '@angular/core';
import { LightRecipeDto } from 'src/app/dtos';
import { RecipeService } from 'src/app/services';

@Component({
  selector: 'app-recipe-list-view',
  templateUrl: './recipe-list-view.component.html',
  styleUrls: ['./recipe-list-view.component.scss']
})
export class RecipeListViewComponent {
  constructor(
    private recipeService: RecipeService,
  ) { }

  currentPage: number = 0;
  pageSize: number = 10;

  totalResults: number = 0;
  totalPages: number = 0;
  resultCount: number = 0;
  recipes: LightRecipeDto[] = [];


  ngOnInit(): void {
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
}
