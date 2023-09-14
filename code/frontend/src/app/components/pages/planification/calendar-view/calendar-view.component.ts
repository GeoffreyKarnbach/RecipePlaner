import { Component, OnInit } from '@angular/core';
import { PlanedRecipeDto } from 'src/app/dtos';
import { RecipeService } from 'src/app/services';

@Component({
  selector: 'app-calendar-view',
  templateUrl: './calendar-view.component.html',
  styleUrls: ['./calendar-view.component.scss']
})
export class CalendarViewComponent implements OnInit{

  constructor(
    private recipeService: RecipeService,
  ) { }

  plannedRecipeList: Map<number, PlanedRecipeDto[]>;

  ngOnInit(): void {
    this.onMonthChanged({ year: new Date().getFullYear(), month: new Date().getMonth() });
  }

  onMonthChanged($event: { year: number; month: number }): void {
    this.recipeService.getPlanedRecipes($event.year, $event.month + 1).subscribe((data) => {
      const entries = Object.entries(data);
      const plannedRecipeListTemp = new Map<number, PlanedRecipeDto[]>();

      for (const [key, value] of entries) {
        plannedRecipeListTemp.set(Number(key), value);
      }

      this.plannedRecipeList = plannedRecipeListTemp;
    });
  }
}
