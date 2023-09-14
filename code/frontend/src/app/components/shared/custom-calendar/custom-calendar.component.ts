import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import { PlanedRecipeDto } from 'src/app/dtos';
import { PlanedRecipeModalViewComponent } from '..';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-custom-calendar',
  templateUrl: './custom-calendar.component.html',
  styleUrls: ['./custom-calendar.component.scss']
})
export class CustomCalendarComponent implements OnChanges{

  currentMonth: number = new Date().getMonth();
  currentYear: number = new Date().getFullYear();

  @Input() plannedRecipeList: Map<number, PlanedRecipeDto[]>;
  planedRecipeListById: Map<number, PlanedRecipeDto> = new Map<number, PlanedRecipeDto>();

  @Output() monthChanged = new EventEmitter<{ year: number; month: number }>();

  constructor(
    private modalService: NgbModal
  ) { }

  ngOnChanges(): void {
    if (this.plannedRecipeList === undefined) {
      return;
    }

    for (const [key, value] of this.plannedRecipeList) {
      for (const planedRecipe of value) {
        this.planedRecipeListById.set(planedRecipe.id, planedRecipe);
      }
    }
  }

  getMonthName(): string {
    return new Date(this.currentYear, this.currentMonth).toLocaleString('default', { month: 'long' });
  }

  /**
   * Switches to the next month
   */
  nextMonth(): void {
    if (this.currentMonth === 11) {
      this.currentMonth = 0;
      this.currentYear++;
    } else {
      this.currentMonth++;
    }

    this.monthChanged.emit({ year: this.currentYear, month: this.currentMonth });
  }

  /**
   * Switches to the previous month
   */
  previousMonth(): void {
    if (this.currentMonth === 0) {
      this.currentMonth = 11;
      this.currentYear--;
    } else {
      this.currentMonth--;
    }

    this.monthChanged.emit({ year: this.currentYear, month: this.currentMonth });
  }

  /**
   * Switches to the current month
   */
  currentMonthAndYear(): void {
    this.currentMonth = new Date().getMonth();
    this.currentYear = new Date().getFullYear();

    this.monthChanged.emit({ year: this.currentYear, month: this.currentMonth });
  }

  /**
   *
   * @returns the number of days in the current month
   */
  getDaysInMonth(): number {
    return new Date(this.currentYear, this.currentMonth + 1, 0).getDate();
  }

  /**
   *
   * @param week the number of the week in the ngFor
   * @param day the number of the day in the ngFor
   * @returns the day number string for the given week and day
   */
  getDayNumberForPosition(week: number, day: number): number | string {

    const firstDayOfMonth = (new Date(this.currentYear, this.currentMonth).getDay() + 6) % 7;
    const dayNumber = (week * 7) + day - firstDayOfMonth + 1;

    if (dayNumber <= 0 || dayNumber > this.getDaysInMonth()) {
      return '';
    }
    return dayNumber.toString();
  }

  getTodayIndicator(week: number, day: number): boolean {
    const dayNumber = this.getDayNumberForPosition(week, day);

    if (dayNumber === '') {
      return false;
    }

    return dayNumber.toString() === new Date().getDate().toString() && this.currentMonth === new Date().getMonth() && this.currentYear === new Date().getFullYear();
  }

  /**
   * @returns the number of weeks in the current month, used for the ngFor
   */
  getWeeksForDisplayInMonth(): number[] {
    const firstDayOfMonth = (new Date(this.currentYear, this.currentMonth).getDay() + 6) % 7;
    const daysInMonth = this.getDaysInMonth();

    return Array(Math.ceil((daysInMonth + firstDayOfMonth) / 7)).fill(0).map((x, i) => i);
  }

  getRecipesForDay(week: number, day: number): any {
    const dayNumber = this.getDayNumberForPosition(week, day);
    if (dayNumber === '') {
      return [];
    }

    const dayNumberString = dayNumber.toString();

    return this.plannedRecipeList?.get(Number(dayNumberString));
  }

  openPlanedRecipeModal(plannedRecipeId: number): void {

    const modalRef = this.modalService.open(PlanedRecipeModalViewComponent, { size: 'lg' });
    modalRef.componentInstance.planedRecipeDto = this.planedRecipeListById.get(plannedRecipeId);
  }

}
