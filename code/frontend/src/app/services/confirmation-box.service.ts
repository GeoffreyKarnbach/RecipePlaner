import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationBoxComponent } from '../components/shared/confirmation-box/confirmation-box.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmationBoxService {

  constructor(
    private modalService: NgbModal
  ) { }

  public confirm(
    title: string,
    message: string,
    btnOkText: string = "OK",
    btnCancelText: string = "Cancel",
    dialogSize: "sm"|"lg" = "sm"): Promise<boolean> {
      const modalRef = this.modalService.open(ConfirmationBoxComponent, { size: dialogSize });
      modalRef.componentInstance.title = title;
      modalRef.componentInstance.message = message;
      modalRef.componentInstance.btnOkText = btnOkText;
      modalRef.componentInstance.btnCancelText = btnCancelText;
      return modalRef.result;
    }
}
