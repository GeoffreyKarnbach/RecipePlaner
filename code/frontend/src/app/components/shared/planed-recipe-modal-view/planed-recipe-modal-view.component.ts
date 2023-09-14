import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PlanedRecipeDto } from 'src/app/dtos';

@Component({
  selector: 'app-planed-recipe-modal-view',
  templateUrl: './planed-recipe-modal-view.component.html',
  styleUrls: ['./planed-recipe-modal-view.component.scss']
})
export class PlanedRecipeModalViewComponent{

  constructor(
    private activeModal: NgbActiveModal,
    private router: Router
  ) { }

  @Input() planedRecipeDto: PlanedRecipeDto;

  public dismiss() {

    this.activeModal.dismiss();
  }

  public goToRecipe() {

    this.activeModal.close();
    this.router.navigate(['/recipe', this.planedRecipeDto.recipeId]);

  }

  public getMealText(): string {
    switch (this.planedRecipeDto.meal) {
      case 'BREAKFAST':
        return 'Frühstück';
      case 'LUNCH':
        return 'Mittagessen';
      case 'DINNER':
        return 'Abendessen';
      default:
        return '';
    }
  }
}
