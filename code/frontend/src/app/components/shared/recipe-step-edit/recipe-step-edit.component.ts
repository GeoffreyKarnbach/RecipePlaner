import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { RecipeSingleStepDto } from 'src/app/dtos';

@Component({
  selector: 'app-recipe-step-edit',
  templateUrl: './recipe-step-edit.component.html',
  styleUrls: ['./recipe-step-edit.component.scss']
})
export class RecipeStepEditComponent implements OnInit{

  ngOnInit(): void {

  }

  @Input() stepToEdit: RecipeSingleStepDto = {
    id: -1,
    position: -1,
    name: '',
    description: '',
    imageSource: 'assets/nopic.jpg'
  }

  @Output() confirmEdit: EventEmitter<RecipeSingleStepDto> = new EventEmitter<RecipeSingleStepDto>();

  confirmEditClick() {
    this.confirmEdit.emit(this.stepToEdit);
  }

  cancelCreation() {
    this.confirmEdit.emit(null);
  }

  selectImage() {
    document.getElementById('upload-file').click();
  }

  onFileSelected(event: any) {
    if (event.target.files[0]) {
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = done => {

        this.stepToEdit.imageSource = done.target.result as string;
      };
    }
  }

  removeImage() {
    this.stepToEdit.imageSource = 'assets/nopic.jpg';
  }
}
