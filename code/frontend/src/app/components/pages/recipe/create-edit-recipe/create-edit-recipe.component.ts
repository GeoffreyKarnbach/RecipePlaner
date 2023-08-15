import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { RecipeCreationDto, RecipeDto, RecipeImagesDto } from 'src/app/dtos';
import { ImageService, RecipeService, ToastService } from 'src/app/services';

export enum RecipeCreateEditModes {
  CREATE,
  EDIT,
}

@Component({
  selector: 'app-create-edit-recipe',
  templateUrl: './create-edit-recipe.component.html',
  styleUrls: ['./create-edit-recipe.component.scss']
})
export class CreateEditRecipeComponent implements OnInit{

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private recipeService: RecipeService,
    private toastService: ToastService,
    private imageService: ImageService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.mode = data['mode'];
    });

    this.recipeService.getAllRecipeCategories().subscribe(
      (categories) => {
        this.categories = categories.map((category) => category.name);
      }
    );

    if (this.mode === RecipeCreateEditModes.EDIT) {
      this.route.params.subscribe((params) => {
        this.id = params['id'];
        this.recipeService.get(this.id).subscribe(
          (recipe) => {
            this.recipe = recipe;
            this.images = recipe.images;
            console.log(recipe);
          },
          (error) => {
            this.router.navigate(['/recipe']);
            this.toastService.showError(error.error, 'Fehler');
          }
        );
      });
    }
  }

  public recipe: RecipeCreationDto = {
    name: '',
    description: '',
    difficulty: 0,
    preparationTime: 0,
    mealType: '',
    recipeCategory: ''
  }

  public categories: string[] = [""];

  images: string[] = [];
  mode: RecipeCreateEditModes = RecipeCreateEditModes.CREATE;
  id: number = -1;

  submitText(): string {
    return this.mode === RecipeCreateEditModes.CREATE ? 'Erstellen' : 'Bearbeiten';
  }

  titleText(): string {
    return this.mode === RecipeCreateEditModes.CREATE ? 'Rezept erstellen' : 'Rezept bearbeiten';
  }

  onSubmit(form: NgForm): void {
    if (this.recipe.difficulty === 0){
      this.toastService.showError('Bitte Schwierigkeit auswählen', 'Fehler');
      return
    }

    if (this.mode === RecipeCreateEditModes.CREATE) {
      this.recipeService.create(this.recipe).subscribe(
        (recipe) => {

          const imageDto: RecipeImagesDto = {
            recipeId: recipe.id,
            images: this.images
          }

          this.imageService.uploadOrUpdateImages(imageDto).subscribe(
            () => {
              console.log(recipe);
              this.toastService.showSuccess('Rezept erstellt', 'Erfolg');
              this.router.navigate(['/recipe']);
            }, (error) => {
              this.toastService.showErrorResponse(error);
            }
          );
        },
        (error) => {
          this.toastService.showErrorResponse(error);
        }
      );
    } else if (this.mode === RecipeCreateEditModes.EDIT) {

      const recipeUpdateDto: RecipeDto = {
        id: this.id,
        name: this.recipe.name,
        description: this.recipe.description,
        difficulty: this.recipe.difficulty,
        preparationTime: this.recipe.preparationTime,
        mealType: this.recipe.mealType,
        recipeCategory: this.recipe.recipeCategory,
        images: []
      };

      this.recipeService.edit(recipeUpdateDto, this.id).subscribe(
        (recipe) => {

            const imageDto: RecipeImagesDto = {
              recipeId: recipe.id,
              images: this.images
            }

            this.imageService.uploadOrUpdateImages(imageDto).subscribe(
              () => {
                console.log(recipe);
                this.toastService.showSuccess('Rezept aktualisiert', 'Erfolg');
                this.router.navigate(['/recipe']);
              }, (error) => {
                this.toastService.showErrorResponse(error);
              }
            );
        },
        (error) => {
          this.toastService.showErrorResponse(error);
        }
      );
    }
  }

  onImageUpdate($event: any) {
    this.images = $event;
  }

}
