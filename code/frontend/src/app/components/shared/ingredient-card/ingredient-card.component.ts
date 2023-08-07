import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { IngredientDto } from 'src/app/dtos';

@Component({
  selector: 'app-ingredient-card',
  templateUrl: './ingredient-card.component.html',
  styleUrls: ['./ingredient-card.component.scss']
})
export class IngredientCardComponent {

  constructor(
    private router: Router,
  ) { }

  @Input() ingredient: IngredientDto;

  goToIngredientEditPage(): void {
    console.log('goToIngredientEditPage()');
    this.router.navigate(['ingredient', 'edit', this.ingredient.id]);
  }

  goToTest(): void {
    console.log('goToTest()');
    this.router.navigate(['ingredient', 'create']);
  }
}
