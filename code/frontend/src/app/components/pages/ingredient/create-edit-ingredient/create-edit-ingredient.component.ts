import { Component, OnInit } from '@angular/core';
import { IngredientCreationDto } from 'src/app/dtos';

@Component({
  selector: 'app-create-edit-ingredient',
  templateUrl: './create-edit-ingredient.component.html',
  styleUrls: ['./create-edit-ingredient.component.scss']
})
export class CreateEditIngredientComponent implements OnInit{

  ngOnInit(): void {
    console.log('CreateEditIngredientComponent initialized');
  }

  public ingredient: IngredientCreationDto = {
    name: '',
    imageSource: '',
    unit: 0,
    count: 0,
    ingredientCategory: {
      id: 0,
      name: '',
      iconSource: ''
    }
  };

}
