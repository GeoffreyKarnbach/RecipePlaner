import { Component, EventEmitter, Output } from '@angular/core';
import { NgbActiveModal, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { PlannedShoppingCreationDto } from 'src/app/dtos';
import { ToastService } from 'src/app/services';
import { ShoppingService } from 'src/app/services/shopping.service';

@Component({
  selector: 'app-planned-shopping-creation-modal',
  templateUrl: './planned-shopping-creation-modal.component.html',
  styleUrls: ['./planned-shopping-creation-modal.component.scss']
})
export class PlannedShoppingCreationModalComponent {

  constructor(
    private activeModal: NgbActiveModal,
    private shoppingService: ShoppingService,
    private toastService: ToastService
    ) { }

  model: NgbDateStruct;
	date: { year: number; month: number };

  plannedShoppingCreationDto: PlannedShoppingCreationDto = {
    comment: '',
    date: new Date(),
    isMorning: false,
  };

  @Output() plannedShoppingCreated = new EventEmitter<any>();

  public dismiss() {

    this.activeModal.dismiss();
  }

  public decline() {

    this.activeModal.close(null);
  }

  public accept() {
    if (this.model == null) {
      return;
    }

    this.plannedShoppingCreationDto.date = new Date(this.model.year, this.model.month - 1, this.model.day, 12);

    if (this.plannedShoppingCreationDto.comment === '') {
      this.plannedShoppingCreationDto.comment = null;
    }

    this.shoppingService.createdPlannedShopping(this.plannedShoppingCreationDto).subscribe(
      (data) => {
        console.log(data);
        this.toastService.showSuccess('Einkauf geplant', 'Erfolg');
        this.activeModal.close(data);
        this.plannedShoppingCreated.emit();
      },
      (error) => {
        this.toastService.showErrorResponse(error);
    });
  }
}
