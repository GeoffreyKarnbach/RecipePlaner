import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeDto, RecipeSingleStepDto, RecipeStepsDto } from 'src/app/dtos';
import { RecipeService, ToastService } from 'src/app/services';

@Component({
  selector: 'app-create-edit-recipe-steps',
  templateUrl: './create-edit-recipe-steps.component.html',
  styleUrls: ['./create-edit-recipe-steps.component.scss']
})
export class CreateEditRecipeStepsComponent implements OnInit{

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private toastService: ToastService
  ) { }

  recipeId: number = 0;
  recipe: RecipeDto = null;

  currentStepEdit: RecipeSingleStepDto = {
    id: -1,
    position: 0,
    name: ' ',
    description: ' ',
    imageSource: ''
  }

  stepEditPosition: number = -1;

  recipeSteps: RecipeStepsDto = {
    recipeId: 0,
    steps: []
  }

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.recipeId = params['id'];
      this.recipeService.get(this.recipeId).subscribe(
        (recipe) => {
          this.recipeId = recipe.id;
          this.recipe = recipe;

          this.recipeService.getSteps(this.recipeId).subscribe(
            (steps) => {
              this.recipeSteps = steps;
            },
            (error) => {
              this.toastService.showError(error.error, 'Error');
              console.log(error);
            });
        });
    });
  }

  editStep($event: any): void {
    this.stepEditPosition = $event;
    this.currentStepEdit = this.recipeSteps.steps[$event];
  }

  deleteStep($event: any): void {
    this.recipeSteps.steps.splice($event, 1);
  }

  moveStepUp($event: any): void {
    const step = this.recipeSteps.steps[$event];
    this.recipeSteps.steps[$event] = this.recipeSteps.steps[$event - 1];
    this.recipeSteps.steps[$event - 1] = step;

    if (this.stepEditPosition !== -1 && this.stepEditPosition === $event-1) {
      this.stepEditPosition++;
    }
  }

  moveStepDown($event: any): void {
    const step = this.recipeSteps.steps[$event];
    this.recipeSteps.steps[$event] = this.recipeSteps.steps[$event + 1];
    this.recipeSteps.steps[$event + 1] = step;

    if (this.stepEditPosition !== -1 && this.stepEditPosition === $event+1) {
      this.stepEditPosition--;
    }
  }

  confirmEditStep($event: any): void {

    if ($event === null) {
      this.recipeSteps.steps.splice(this.stepEditPosition, 1);

      this.stepEditPosition = -1;
      this.currentStepEdit = {
        id: -1,
        position: this.recipeSteps.steps.length,
        name: '',
        description: '',
        imageSource: 'assets/nopic.jpg'
      }
      return;
    }

    if ($event.name === '' || $event.description === '') {
      this.toastService.showError('Please fill in the step name and description', 'Error');
      return;
    }

    this.recipeSteps.steps[this.stepEditPosition] = $event;
    this.stepEditPosition = -1;

    this.currentStepEdit = {
      id: -1,
      position: this.recipeSteps.steps.length,
      name: ' ',
      description: ' ',
      imageSource: 'assets/nopic.jpg'
    }
  }

  addStep(): void {

    // If current step is not finished, do not add new step
    if (this.currentStepEdit.name === '' || this.currentStepEdit.description === '') {
      this.toastService.showError('Please fill in the step name and description', 'Error');
      return;
    }

    this.recipeSteps.steps.push({
      id: -1,
      position: this.recipeSteps.steps.length,
      name: '',
      description: '',
      imageSource: 'assets/nopic.jpg'
    });

    this.stepEditPosition = this.recipeSteps.steps.length - 1;
    this.currentStepEdit = this.recipeSteps.steps[this.stepEditPosition];
  }

  saveSteps(): void {
    this.recipeSteps.recipeId = this.recipeId;

    this.recipeService.updateSteps(this.recipeSteps).subscribe(
      () => {
        this.toastService.showSuccess('Recipe steps saved successfully', 'Success');
        this.router.navigate([`/recipe/${this.recipeId}`]);
      },
      (error) => {
        this.toastService.showError(error.error, 'Error');
        console.log(error);
      }
    );
  }
}
