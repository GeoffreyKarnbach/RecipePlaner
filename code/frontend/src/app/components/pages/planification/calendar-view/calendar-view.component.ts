import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PlannedShoppingCreationModalComponent } from 'src/app/components/shared/planned-shopping-creation-modal/planned-shopping-creation-modal.component';
import { PlanedRecipeDto, PlannedShoppingDto } from 'src/app/dtos';
import { RecipeService } from 'src/app/services';
import { ShoppingService } from 'src/app/services/shopping.service';

@Component({
  selector: 'app-calendar-view',
  templateUrl: './calendar-view.component.html',
  styleUrls: ['./calendar-view.component.scss']
})
export class CalendarViewComponent implements OnInit{

  constructor(
    private recipeService: RecipeService,
    private modalService: NgbModal,
    private shoppingService: ShoppingService
  ) { }

  plannedRecipeList: Map<number, PlanedRecipeDto[]>;
  plannedShoppingSessionList: Map<number, PlannedShoppingDto[]>;

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

    this.shoppingService.getPlannedShopping($event.year, $event.month + 1).subscribe((data) => {
      const entries = Object.entries(data);
      const plannedShoppingSessionListTemp = new Map<number, PlannedShoppingDto[]>();

      for (const [key, value] of entries) {
        plannedShoppingSessionListTemp.set(Number(key), value);
      }

      this.plannedShoppingSessionList = plannedShoppingSessionListTemp;
    });
  }

  updateRequired(): void {
    this.onMonthChanged({ year: new Date().getFullYear(), month: new Date().getMonth() });
  }

  createNewPlannedShoppingSession(): void {
    const modalRef = this.modalService.open(PlannedShoppingCreationModalComponent, { size: 'lg' });
    modalRef.componentInstance.plannedShoppingCreated.subscribe(() => {
      this.updateRequired();
    });
  }
}
