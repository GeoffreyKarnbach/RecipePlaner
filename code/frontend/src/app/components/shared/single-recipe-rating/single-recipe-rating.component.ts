import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RecipeRatingDto } from 'src/app/dtos';
import { RecipeService, ToastService } from 'src/app/services';

@Component({
  selector: 'app-single-recipe-rating',
  templateUrl: './single-recipe-rating.component.html',
  styleUrls: ['./single-recipe-rating.component.scss']
})
export class SingleRecipeRatingComponent implements OnInit{

  constructor(
    private recipeService: RecipeService,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.rating.recipeId = this.recipeId;
  }

  @Input() rating: RecipeRatingDto = {
    recipeId: 0,
    id: -1,
    rating: 0,
    comment: '',
    title: '',
    date: new Date()
  }

  @Input() recipeId: number = -1;
  @Input() isEditMode: boolean = false;
  @Output() completedOrCanceled: EventEmitter<any> = new EventEmitter();

  cancel(): void {
    this.completedOrCanceled.emit();
  }

  complete(): void {
    console.log(this.rating);
    this.recipeService.saveRating(this.rating).subscribe(() => {
      this.completedOrCanceled.emit();
    }, (error) => {
      this.toastService.showErrorResponse(error);
    });
  }
}
