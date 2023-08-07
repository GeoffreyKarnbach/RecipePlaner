import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IngredientCreationDto } from 'src/app/dtos';
import { IngredientUnit } from 'src/app/enums';
import { Globals } from 'src/app/global';
import { ToastService, IngredientService } from 'src/app/services';

@Component({
  selector: 'app-create-edit-ingredient',
  templateUrl: './create-edit-ingredient.component.html',
  styleUrls: ['./create-edit-ingredient.component.scss']
})
export class CreateEditIngredientComponent implements OnInit{

  constructor(
    private globals: Globals,
    private router: Router,
    private route: ActivatedRoute,
    private ingredientService: IngredientService,
    private toastService: ToastService
  ) {}

  maxFileSize = 1000000;
  imageBase64: string = '';

  public ingredient: IngredientCreationDto = {
    name: '',
    imageSource: '',
    unit: IngredientUnit.GRAM,
    count: 0,
    ingredientCategory: ''
  };

  public categories: string[] = ["TEST123"];

  ngOnInit(): void {
    this.ingredientService.getAllIngredientCategories().subscribe(
      (categories) => {
        this.categories = categories.map((category) => category.name);
      }
    );
  }

  onSubmit(form: NgForm): void {
    if (this.ingredient.imageSource === '') {
      this.ingredient.imageSource = null!;
    }

    this.ingredientService.create(this.ingredient).subscribe(
      (ingredient) => {
        console.log(ingredient);
        this.toastService.showSuccess('Zutat erstellt', 'Erfolg');
        this.router.navigate(['/ingredients']);
      },
      (error) => {
        this.toastService.showErrorResponse(error);
      }
    );
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

}
