import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subject } from 'rxjs';
import { RecipeDto, LightIngredientDto } from 'src/app/dtos';
import { IngredientService, RecipeService, ToastService } from 'src/app/services';
import { ConfirmationBoxService } from 'src/app/services/confirmation-box.service';
import { PlanedRecipeModalComponent } from 'src/app/components/shared';

@Component({
  selector: 'app-recipe-view',
  templateUrl: './recipe-view.component.html',
  styleUrls: ['./recipe-view.component.scss']
})
export class RecipeViewComponent implements OnInit{

  constructor(
    private recipeService: RecipeService,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService,
    private ingredientService: IngredientService,
    private confirmationBoxService: ConfirmationBoxService,
    private modalService: NgbModal
  ) { }

  id: number = 0;
  recipe: RecipeDto = {
    id: 0,
    name: '',
    description: '',
    difficulty: 0,
    preparationTime: 0,
    mealType: '',
    recipeCategory: '',
    images: [],
    tags: [],
    ingredients: {
      recipeId: 0,
      recipeIngredientItems: []
    },
    steps: {
      recipeId: 0,
      steps: []
    },
    averageRating: 0,
    numberOfRatings: 1
  }

  ratingListNotification: Subject<Boolean> = new Subject<Boolean>();

  recipeRating: number = 3.5;
  activeNewRating: boolean = false;

  ingredients: Map<number, LightIngredientDto> = new Map<number, LightIngredientDto>();

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.id = Number(params['id']);
      this.recipeService.get(this.id).subscribe(recipe => {
        this.recipe = recipe;
        for (let recipeIngredientItem of this.recipe.ingredients.recipeIngredientItems) {

          this.ingredientService.getLight(recipeIngredientItem.ingredientId).subscribe(ingredient => {
            this.ingredients.set(recipeIngredientItem.ingredientId, ingredient);
          },
          error => {
            this.toastService.showError('Fehler', 'Zutat konnte nicht gefunden werden');
            this.router.navigate(['/ingredients']);
          });
        }
      },
      error => {
        this.toastService.showError('Fehler', 'Rezept konnte nicht gefunden werden');
        this.router.navigate(['/ingredients']);
      });
    });
  }

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

    this.confirmationBoxService.confirm('Rezept löschen', 'Bist du dir sicher, dass du das Rezept löschen willst?').then(
      (confirmed) => {
        if (confirmed) {
          this.recipeService.delete(this.recipe.id).subscribe(
            (data) => {
              console.log(data);
              this.toastService.showSuccess('Erfolg', 'Rezept wurde gelöscht');
              this.router.navigate(['/recipe']);
          });
        }
    });
  }

  cookRecipe(): void {
    this.confirmationBoxService.confirm('Rezept kochen', 'Bist du dir sicher, dass du das Rezept kochen möchtest? Die verwendeten Zutaten werden aus deinem Inventar entfernt.').then(
      (confirmed) => {
        if (confirmed) {
          console.log(this.recipe.ingredients)
          this.recipeService.cookRecipe(this.recipe.id, this.recipe.ingredients).subscribe(
            (data) => {
              console.log(data);
              this.toastService.showSuccess('Erfolg', 'Zutaten für das Rezept wurden aus deinem Inventar entfernt');
              this.router.navigate(['/recipe']);
          }, (error) => {
            this.toastService.showErrorResponse(error);
          });
        }
      }
    );
  }

  openPlanningModal(): void {
    const modalRef = this.modalService.open(PlanedRecipeModalComponent, { size: 'lg' });
    modalRef.componentInstance.recipeId = this.recipe.id;

    modalRef.result.then((result) => {
      if (result) {
        this.recipeService.planRecipe(this.recipe.id, result).subscribe(
          (data) => {
            console.log(data);
            this.toastService.showSuccess('Rezept wurde geplant', 'Erfolg');
            this.router.navigate(['/calendar']);
        }, (error) => {
          this.toastService.showErrorResponse(error);
        });
      }
    });
  }

  getDifficultyFiledArray(): number[] {
    return Array(this.recipe.difficulty).fill(0);
  }

  getDifficultyEmptyArray(): number[] {
    return Array(5 - this.recipe.difficulty).fill(0);
  }

  getMealType(): string {
    switch (this.recipe.mealType) {
      case 'BREAKFAST':
        return 'Frühstück';
      case 'LUNCH':
        return 'Mittagessen';
      case 'DINNER':
        return 'Abendessen';
      case 'SNACK':
        return 'Snack';
      case 'OTHER':
        return 'Andere';
      default:
        return '';
    }
  }

  startNewRating(): void {
    this.activeNewRating = true;
  }

  completedOrCanceledRating(): void {
    this.activeNewRating = false;

    this.ratingListNotification.next(true);
  }
}
