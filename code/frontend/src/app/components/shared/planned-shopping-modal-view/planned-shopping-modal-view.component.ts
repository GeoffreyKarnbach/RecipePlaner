import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PlannedShoppingDto } from 'src/app/dtos';
import { ToastService } from 'src/app/services';
import { ShoppingService } from 'src/app/services/shopping.service';

@Component({
  selector: 'app-planned-shopping-modal-view',
  templateUrl: './planned-shopping-modal-view.component.html',
  styleUrls: ['./planned-shopping-modal-view.component.scss']
})
export class PlannedShoppingModalViewComponent implements OnInit {

  constructor(
    private activeModal: NgbActiveModal,
    private shoppingService: ShoppingService,
    private toastService: ToastService
  ) { }

  ngOnInit(): void {
    this.shoppingService.getPlannedShoppingById(this.id).subscribe(
      (data) => {
        this.shoppingSession = data;
      },
      (error) => {
        this.toastService.showErrorResponse(error);
      });
  }

  @Input() id: number;

  shoppingSession : PlannedShoppingDto = {
    id: 0,
    comment: '',
    date: new Date(),
    isMorning: false,
  }

  public dismiss() {

    this.activeModal.dismiss();
  }
}
