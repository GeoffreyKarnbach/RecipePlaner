import { Component, Injectable, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbDateStruct, NgbDatepickerI18n } from '@ng-bootstrap/ng-bootstrap';
import { PlanedRecipeCreationDto } from 'src/app/dtos';

const I18N_VALUES = {
	de: {
		weekdays: ['Mon', 'Die', 'Mit', 'Don', 'Fre', 'Sam', 'Son'],
		months: ['Jän', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dec'],
		weekLabel: 'woche',
	},
};

@Injectable()
export class I18n {
	language = 'de';
}

// Define custom service providing the months and weekdays translations
@Injectable()
export class CustomDatepickerI18n extends NgbDatepickerI18n {
	constructor(private _i18n: I18n) {
		super();
	}

	getWeekdayLabel(weekday: number): string {
		return I18N_VALUES['de'].weekdays[weekday - 1];
	}
	override getWeekLabel(): string {
		return I18N_VALUES['de'].weekLabel;
	}
	getMonthShortName(month: number): string {
		return I18N_VALUES['de'].months[month - 1];
	}
	getMonthFullName(month: number): string {
		return this.getMonthShortName(month);
	}
	getDayAriaLabel(date: NgbDateStruct): string {
		return `${date.day}-${date.month}-${date.year}`;
	}
}

@Component({
  selector: 'app-planed-recipe-modal',
  templateUrl: './planed-recipe-modal.component.html',
  styleUrls: ['./planed-recipe-modal.component.scss'],
  providers: [I18n, { provide: NgbDatepickerI18n, useClass: CustomDatepickerI18n }],
})
export class PlanedRecipeModalComponent implements OnInit{

  constructor(private activeModal: NgbActiveModal) { }

  @Input() recipeId: number;

  model: NgbDateStruct;
	date: { year: number; month: number };

  planedRecipeCreationDto: PlanedRecipeCreationDto = {
    recipeId: 0,
    date: new Date(),
    meal: 'LUNCH',
    comment: '',
    portionCount: 1,
  };

  ngOnInit(): void {

    this.planedRecipeCreationDto.recipeId = this.recipeId;
  }

  public dismiss() {

    this.activeModal.dismiss();
  }

  public decline() {

    this.activeModal.close(null);
  }

  public accept() {
    if (this.model == null) {
      return;
    }

    this.planedRecipeCreationDto.date = new Date(this.model.year, this.model.month - 1, this.model.day);
    this.activeModal.close(this.planedRecipeCreationDto);
  }
}
