import { Component, EventEmitter, Input, Output } from '@angular/core';
import { RecipeSingleStepDto } from 'src/app/dtos';

@Component({
  selector: 'app-recipe-step',
  templateUrl: './recipe-step.component.html',
  styleUrls: ['./recipe-step.component.scss']
})
export class RecipeStepComponent {

  @Input() step: RecipeSingleStepDto = {
    id: -1,
    position: -1,
    name: '',
    description: '',
    imageSource: ''
  }

  @Input() isEditing: boolean = false;
  @Input() stepCount: number = 0;
  @Input() position: number = 0;

  @Output() moveStepUp: EventEmitter<number> = new EventEmitter<number>();
  @Output() editStep: EventEmitter<number> = new EventEmitter<number>();
  @Output() deleteStep: EventEmitter<number> = new EventEmitter<number>();
  @Output() moveStepDown: EventEmitter<number> = new EventEmitter<number>();

  moveStepUpClicked(): void {
    this.moveStepUp.emit(this.position);
  }

  editStepClicked(): void {
    this.editStep.emit(this.position);
  }

  deleteStepClicked(): void {
    this.deleteStep.emit(this.position);
  }

  moveStepDownClicked(): void {
    this.moveStepDown.emit(this.position);
  }
}
