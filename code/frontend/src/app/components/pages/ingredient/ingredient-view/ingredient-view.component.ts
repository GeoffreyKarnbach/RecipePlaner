import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IngredientDto } from 'src/app/dtos';
import { IngredientUnit } from 'src/app/enums';
import { IngredientService } from 'src/app/services';

@Component({
  selector: 'app-ingredient-view',
  templateUrl: './ingredient-view.component.html',
  styleUrls: ['./ingredient-view.component.scss']
})
export class IngredientViewComponent implements OnInit{

  constructor(
    private ingredientService: IngredientService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {

        this.id = Number(params['id']);
        this.ingredientService.get(this.id).subscribe(ingredient => {
          this.ingredient = ingredient;
        },
        error => {
          this.router.navigate(['/ingredients']);
        });
      });
  }

  id: number = 0;

  ingredient: IngredientDto = {
    id: 0,
    name: 'Default Name',
    imageSource: 'defaultImageSource',
    unit: IngredientUnit.GRAM,
    count: 0,
    ingredientCategory: 'Default Category'
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
}
