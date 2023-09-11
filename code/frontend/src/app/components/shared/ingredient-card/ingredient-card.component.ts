import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { IngredientDto } from 'src/app/dtos';
import { IngredientUnit } from 'src/app/enums';
import { IngredientService, ToastService } from 'src/app/services';
import { ConfirmationBoxService } from 'src/app/services/confirmation-box.service';

@Component({
  selector: 'app-ingredient-card',
  templateUrl: './ingredient-card.component.html',
  styleUrls: ['./ingredient-card.component.scss']
})
export class IngredientCardComponent {

  constructor(
    private router: Router,
    private ingredientService: IngredientService,
    private confirmationBoxService: ConfirmationBoxService,
    private toastService: ToastService
  ) { }

  @Input() ingredient: IngredientDto;
  @Output() deletedElement = new EventEmitter();

  goToIngredientEditPage(): void {
    console.log('goToIngredientEditPage()');
    this.router.navigate(['ingredient', 'edit', this.ingredient.id]);
  }

  goToTest(): void {
    this.router.navigate(['ingredient', 'create']);
  }

  getIngredientUnitText(): string {
    switch (this.ingredient.unit.toString()) {
      case "GRAM":
        return 'Gramm';
      case "MILLILITER":
        return 'Milliliter';
      case "PIECE":
        return 'Stück';
      default:
        return 'Unbekannt';
    }
  }

  deleteIngredient(): void {
    this.confirmationBoxService.confirm('Zutat löschen', 'Bist du dir sicher, dass du diese Zutat löschen willst? (Zutaten, die in Rezepten verwendet werden können nicht gelöscht werden)').then(
      (confirmed) => {
        if (confirmed) {
          this.ingredientService.delete(this.ingredient.id).subscribe(
            (data) => {
              console.log(data);
              this.deletedElement.emit();
            }, (error) => {
              this.toastService.showErrorResponse(error);
            }
          );
        }
    });
  }

  goToDetailedView(): void {
    this.router.navigate(['ingredient', this.ingredient.id]);
  }

}
