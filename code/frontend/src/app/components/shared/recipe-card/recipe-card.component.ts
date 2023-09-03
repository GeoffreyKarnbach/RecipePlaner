import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { LightRecipeDto } from 'src/app/dtos';
import { RecipeService } from 'src/app/services';

@Component({
  selector: 'app-recipe-card',
  templateUrl: './recipe-card.component.html',
  styleUrls: ['./recipe-card.component.scss']
})
export class RecipeCardComponent implements OnInit{

  constructor(
    private router: Router,
    private recipeService: RecipeService
  ) { }

  ngOnInit(): void {
    if (this.recipe.mainImage === null) {
      this.recipe.mainImage = '/assets/nopic.jpg'
    }
  }

  @Input() recipe: LightRecipeDto;
  @Input() showButtons: boolean = true;
  @Output() deletedElement = new EventEmitter();

  goToRecipeEditPage(): void {
    this.router.navigate(['recipe', 'edit', this.recipe.id]);
  }

  getPreparationTime(): string {
    if (this.recipe.preparationTime > 60) {
      return Math.floor(this.recipe.preparationTime / 60) + ' h ' + this.recipe.preparationTime % 60 + ' min';
    }

    return this.recipe.preparationTime + ' min';
  }

  deleteRecipe(): void {

    this.recipeService.delete(this.recipe.id).subscribe(
      (data) => {
        console.log(data);
        this.deletedElement.emit();
      }
    );
  }

  goToDetailedView(): void {
    this.router.navigate(['recipe', this.recipe.id]);
  }

  getDifficultyFiledArray(): number[] {
    return Array(this.recipe.difficulty).fill(0);
  }

  getDifficultyEmptyArray(): number[] {
    return Array(5 - this.recipe.difficulty).fill(0);
  }

  getRatingFiledArray(): number[] {
    return Array(this.recipe.averageRating).fill(0);
  }

  getRatingEmptyArray(): number[] {
    return Array(5 - this.recipe.averageRating).fill(0);
  }
}
