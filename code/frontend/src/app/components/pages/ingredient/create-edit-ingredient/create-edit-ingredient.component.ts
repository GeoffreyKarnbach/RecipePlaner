import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IngredientCreationDto, IngredientDto } from 'src/app/dtos';
import { IngredientUnit } from 'src/app/enums';
import { ToastService, IngredientService } from 'src/app/services';

export enum IngredientCreateEditModes {
  CREATE,
  EDIT,
}

@Component({
  selector: 'app-create-edit-ingredient',
  templateUrl: './create-edit-ingredient.component.html',
  styleUrls: ['./create-edit-ingredient.component.scss']
})
export class CreateEditIngredientComponent implements OnInit{

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private ingredientService: IngredientService,
    private toastService: ToastService
  ) {}

  maxFileSize = 1000000;
  imageBase64: string = '';
  mode: IngredientCreateEditModes = IngredientCreateEditModes.CREATE;
  id = -1;

  public ingredient: IngredientCreationDto = {
    name: '',
    imageSource: '',
    unit: IngredientUnit.GRAM,
    count: 0,
    ingredientCategory: ''
  };

  public categories: string[] = [""];

  ngOnInit(): void {

    this.route.data.subscribe(data => {
      this.mode = data['mode'];
    });

    this.ingredientService.getAllIngredientCategories().subscribe(
      (categories) => {
        this.categories = categories.map((category) => category.name);
      }
    );

    if (this.mode === IngredientCreateEditModes.EDIT) {
      this.route.params.subscribe((params) => {
        this.id = params['id'];
        this.ingredientService.get(this.id).subscribe(
          (ingredient) => {
            this.ingredient = ingredient;
          },
          (error) => {
            this.router.navigate(['/ingredient']);
            this.toastService.showError(error.error, 'Fehler');
          }
        );
      });
    }
  }

  onSubmit(form: NgForm): void {
    if (this.ingredient.imageSource === '') {
      this.ingredient.imageSource = null!;
    }

    if (this.mode === IngredientCreateEditModes.CREATE) {
      this.ingredientService.create(this.ingredient).subscribe(
        (ingredient) => {
          this.toastService.showSuccess('Zutat erstellt', 'Erfolg');
          this.router.navigate(['/ingredients']);
        },
        (error) => {
          this.toastService.showErrorResponse(error);
        }
      );
    } else if (this.mode === IngredientCreateEditModes.EDIT) {

      let ingredientEdit: IngredientDto = {
        id: this.id,
        name: this.ingredient.name,
        imageSource: this.ingredient.imageSource,
        unit: this.ingredient.unit,
        count: this.ingredient.count,
        ingredientCategory: this.ingredient.ingredientCategory
      };

      this.ingredientService.edit(ingredientEdit).subscribe(
        (ingredient) => {
          this.toastService.showSuccess('Zutat bearbeitet', 'Erfolg');
          this.router.navigate(['/ingredients']);
        },
        (error) => {
          this.toastService.showErrorResponse(error);
        }
      );
    }
  }

  selectImage() {
    document.getElementById('upload-file')?.click();
  }

  onFileSelected(event: any) {
    if (event.target.files[0]) {
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = done => {

        if (event.target.files[0].size > this.maxFileSize) {
          this.toastService.showError('Bild zu groß');
          return;
        }

        this.ingredient.imageSource = done.target?.result as string;

        // Reset file input
        const fileInput = document.getElementById('upload-file') as HTMLInputElement;
        fileInput.value = '';
      };
    }
  }

  removeImage() {
    this.ingredient.imageSource = '';
  }

  submitText(): string {
    return this.mode === IngredientCreateEditModes.CREATE ? 'Erstellen' : 'Bearbeiten';
  }

  titleText(): string {
    return this.mode === IngredientCreateEditModes.CREATE ? 'Zutat erstellen' : 'Zutat bearbeiten';
  }

}
