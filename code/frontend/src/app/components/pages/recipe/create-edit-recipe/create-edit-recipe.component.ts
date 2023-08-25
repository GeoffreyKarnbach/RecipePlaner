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

    this.recipeService.getAllRecipeTags().subscribe(
      (tags) => {
        this.tags = tags;
        this.tags.sort();

      }
    );

    if (this.mode === RecipeCreateEditModes.EDIT) {
      this.route.params.subscribe((params) => {
        this.id = params['id'];
        this.recipeService.get(this.id).subscribe(
          (recipe) => {
            this.recipe = recipe;
            this.images = recipe.images;

            for (const tag of this.recipe.tags) {
              this.tags = this.tags?.filter(t => t !== tag);
            }
            this.tags.sort();
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
    recipeCategory: '',
    tags: [],
  }

  public categories: string[] = [""];
  public tags: string[] = [''];

  images: string[] = [];
  mode: RecipeCreateEditModes = RecipeCreateEditModes.CREATE;
  id: number = -1;
  selectedTag: string = null;

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
              this.router.navigate(['/recipe', recipe.id]);
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
        images: [],
        tags: this.recipe.tags,
        ingredients: null
      };

      this.recipeService.edit(recipeUpdateDto, this.id).subscribe(
        (recipe) => {

            const imageDto: RecipeImagesDto = {
              recipeId: recipe.id,
              images: this.images
            }

            this.imageService.uploadOrUpdateImages(imageDto).subscribe(
              () => {
                this.toastService.showSuccess('Rezept aktualisiert', 'Erfolg');
                this.router.navigate(['/recipe', recipe.id]);
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

  public onTagAdded(): void {
    if (this.selectedTag) {
      this.tags = this.tags.filter(tag => tag !== this.selectedTag);
      this.recipe.tags.push(this.selectedTag);
      this.selectedTag = null;
      this.recipe.tags.sort();
    }
  }

  moveTagLeft($event: number) {
    const tagID = $event - 1;

    const tag = this.recipe.tags[tagID];
    this.recipe.tags[tagID] = this.recipe.tags[tagID - 1];
    this.recipe.tags[tagID - 1] = tag;
  }

  moveTagRight($event: number) {
    const tagID = $event - 1;

    const tag = this.recipe.tags[tagID];
    this.recipe.tags[tagID] = this.recipe.tags[tagID + 1];
    this.recipe.tags[tagID + 1] = tag;
  }

  deleteTag($event: number) {
    const tagID = $event - 1;

    this.tags.push(this.recipe.tags.splice(tagID, 1)[0]);
    this.recipe.tags.sort();
    this.tags.sort();
  }

}
