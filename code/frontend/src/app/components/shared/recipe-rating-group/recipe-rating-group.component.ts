import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { Subject } from 'rxjs';
import { RecipeRatingDto } from 'src/app/dtos';
import { RecipeService } from 'src/app/services';

@Component({
  selector: 'app-recipe-rating-group',
  templateUrl: './recipe-rating-group.component.html',
  styleUrls: ['./recipe-rating-group.component.scss']
})
export class RecipeRatingGroupComponent implements OnInit{

  constructor(
    private recipeService: RecipeService,
  ) {}

  ngOnInit(): void {
    this.refreshData();

    this.needToRefresh.subscribe(
      (data) => {
        this.refreshData();
      });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['recipeId'] && !changes['recipeId'].firstChange) {
      this.refreshData();
    }
  }

  @Input() recipeId: number = 0;
  @Input() needToRefresh: Subject<Boolean>;

  currentPage: number = 0;
  pageSize: number = 5;

  totalResults: number = 0;
  totalPages: number = 0;
  resultCount: number = 0;
  ratings: RecipeRatingDto[] = [];

  refreshData(): void {
    this.recipeService.getRatings(this.currentPage, this.pageSize, this.recipeId).subscribe(
      (data) => {
        this.ratings = data.result;
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
}
