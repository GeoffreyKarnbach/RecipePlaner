import { Component } from '@angular/core';

@Component({
  selector: 'app-custom-calendar',
  templateUrl: './custom-calendar.component.html',
  styleUrls: ['./custom-calendar.component.scss']
})
export class CustomCalendarComponent {

  currentMonth: number = new Date().getMonth();
  currentYear: number = new Date().getFullYear();

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
  }

  /**
   * Switches to the current month
   */
  currentMonthAndYear(): void {
    this.currentMonth = new Date().getMonth();
    this.currentYear = new Date().getFullYear();
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

    return [
      {
        name: 'Recipe 1',
        recipeId: 1
      },
      {
        name: 'Recipe 2',
        recipeId: 2
      },
      {
        name: 'Recipe 3',
        recipeId: 3
      },
      {
        name: 'Recipe 4',
        recipeId: 4
      },
    ];


    return [];
  }

  openPlanedRecipeModal(recipeId: number): void {
    // console.log('Open modal for recipe with id: ' + recipeId);
    // TODO: Open modal
  }

}
