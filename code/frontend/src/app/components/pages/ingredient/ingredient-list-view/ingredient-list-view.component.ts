import { Component, OnInit } from '@angular/core';
import { IngredientDto, IngredientFilterDto } from 'src/app/dtos';
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
  categories: string[];

  loadingComplete: boolean = false;

  filterDto: IngredientFilterDto = {
    filterName: '',
    filterCategory: null,
    filterUnit: null,
    filterCriteria: "CREATION_DATE_ASCENDING"
  }

  oldCurrentPage: number = 0;
  oldPageSize: number = 0;

  bypassSearchLock: boolean = false;

  filterDtoLastSearch: IngredientFilterDto = null;

  ngOnInit(): void {
    this.refreshData(this.filterDto);

    this.ingredientService.getAllIngredientCategories().subscribe(
      (categories) => {
        this.categories = categories.map((category) => category.name);
        this.categories.unshift('Keine Kategorie');
      }
    );
  }

  refreshData(filterDtoForRequest: IngredientFilterDto): void {

    this.loadingComplete = false;

    this.ingredientService.getAllFiltered(this.currentPage, this.pageSize, filterDtoForRequest).subscribe(
      (data) => {
        this.ingredients = data.result;
        this.totalResults = data.totalResults;
        this.totalPages = data.totalPages;
        this.resultCount = data.resultCount;

        this.loadingComplete = true;
      }
    );
  }

  nextPage(): void {
    this.currentPage++;
    this.searchFilteredPreparation();
  }

  previousPage(): void {
    this.currentPage--;
    this.searchFilteredPreparation();
  }

  deletedElement($event: any): void {
    // Allows search to be triggered even if no change in filter settings
    this.bypassSearchLock = true;

    if (this.ingredients.length === 1 && this.currentPage !== 0) {
      // If last element on page, go back one page
      this.currentPage--;
    }
    this.searchFilteredPreparation();
  }

  searchFilteredPreparation() {

    if ( this.filterDtoLastSearch !== null &&
      this.filterDto.filterName === this.filterDtoLastSearch.filterName &&
      this.filterDto.filterCategory === this.filterDtoLastSearch.filterCategory &&
      this.filterDto.filterUnit === this.filterDtoLastSearch.filterUnit &&
      this.filterDto.filterCriteria === this.filterDtoLastSearch.filterCriteria &&
      this.oldCurrentPage === this.currentPage &&
      this.oldPageSize === this.pageSize &&
      this.bypassSearchLock !== true
     ) {
      return;
    }

    this.bypassSearchLock = false;

    if ( this.filterDtoLastSearch !== null && (
      this.filterDto.filterName !== this.filterDtoLastSearch.filterName ||
      this.filterDto.filterCategory !== this.filterDtoLastSearch.filterCategory ||
      this.filterDto.filterUnit !== this.filterDtoLastSearch.filterUnit ||
      this.filterDto.filterCriteria !== this.filterDtoLastSearch.filterCriteria
    )) {
      this.currentPage = 0;
    }


    this.oldCurrentPage = this.currentPage;
    this.oldPageSize = this.pageSize;

    this.filterDtoLastSearch = {
      filterName: this.filterDto.filterName,
      filterCategory: this.filterDto.filterCategory,
      filterUnit: this.filterDto.filterUnit,
      filterCriteria: this.filterDto.filterCriteria
    }

    let filterDtoForRequest: IngredientFilterDto = {
      filterName: this.filterDto.filterName,
      filterCategory: this.filterDto.filterCategory,
      filterUnit: this.filterDto.filterUnit,
      filterCriteria: this.filterDto.filterCriteria
    }

    if (filterDtoForRequest.filterCategory == 'Keine Kategorie') {
      filterDtoForRequest.filterCategory = null;
    }
    if (filterDtoForRequest.filterUnit?.toString() == 'null') {
      filterDtoForRequest.filterUnit = null;
    }

    this.refreshData(filterDtoForRequest);
  }

  resetFilterSettings() {
    this.filterDto = {
      filterName: '',
      filterCategory: null,
      filterUnit: null,
      filterCriteria: "CREATION_DATE_ASCENDING"
    }
    this.refreshData(this.filterDto);
  }

}
