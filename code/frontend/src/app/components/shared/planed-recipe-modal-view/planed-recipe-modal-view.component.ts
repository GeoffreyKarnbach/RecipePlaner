import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PlanedRecipeDto } from 'src/app/dtos';
import { ToastService, ConfirmationBoxService, RecipeService } from 'src/app/services';

@Component({
  selector: 'app-planed-recipe-modal-view',
  templateUrl: './planed-recipe-modal-view.component.html',
  styleUrls: ['./planed-recipe-modal-view.component.scss']
})
export class PlanedRecipeModalViewComponent{

  constructor(
    private activeModal: NgbActiveModal,
    private router: Router,
    private confirmationBoxService: ConfirmationBoxService,
    private toastService: ToastService,
    private recipeService: RecipeService
  ) { }

  @Input() planedRecipeDto: PlanedRecipeDto;
  @Output() deleted: EventEmitter<any> = new EventEmitter();

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

  public deletePlannedRecipe() {
    this.confirmationBoxService.confirm('Geplantes Rezept löschen', 'Bist du dir sicher, dass du das geplante Rezept für die Mahlzeit entfernen möchtest?').then(
      (confirmed) => {
        if (confirmed) {
          this.recipeService.deletePlannedRecipe(this.planedRecipeDto.id).subscribe(
            (data) => {
              console.log(data);
              this.activeModal.close();
              this.deleted.emit();
          }, (error) => {
            this.toastService.showErrorResponse(error);
          });
        }
    });
  }
}
