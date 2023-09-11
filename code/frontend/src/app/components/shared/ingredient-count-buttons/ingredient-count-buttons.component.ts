import { Component, Input, OnInit } from '@angular/core';
import { IngredientDto } from 'src/app/dtos';
import { IngredientService, ToastService } from 'src/app/services';

@Component({
  selector: 'app-ingredient-count-buttons',
  templateUrl: './ingredient-count-buttons.component.html',
  styleUrls: ['./ingredient-count-buttons.component.scss']
})
export class IngredientCountButtonsComponent implements OnInit{

  ngOnInit(): void { }

  constructor(
    private ingredientService: IngredientService,
    private toastService: ToastService
  ) {}

  @Input() ingredient: IngredientDto;

  customAmount: number = 0;

  changeIngredientCount(amount: number): void {
    console.log(amount);
    this.ingredientService.addIngredientToInventory(this.ingredient.id, amount).subscribe(
      () => {
        this.ingredient.count += amount;
        this.customAmount = 0;
      }, (error) => {
        this.toastService.showErrorResponse(error);
      }
    );
  }

}
