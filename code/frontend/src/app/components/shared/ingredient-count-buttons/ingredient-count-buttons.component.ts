import { Component, Input, OnInit } from '@angular/core';
import { IngredientDto } from 'src/app/dtos';

@Component({
  selector: 'app-ingredient-count-buttons',
  templateUrl: './ingredient-count-buttons.component.html',
  styleUrls: ['./ingredient-count-buttons.component.scss']
})
export class IngredientCountButtonsComponent implements OnInit{

  ngOnInit(): void { }

  @Input() ingredient: IngredientDto;

  customAmount: number = 0;

  changeIngredientCount(amount: number): void {
    console.log(amount);
    // TODO: Backend call
  }

}
