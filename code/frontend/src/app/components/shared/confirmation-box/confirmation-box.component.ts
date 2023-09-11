import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirmation-box',
  templateUrl: './confirmation-box.component.html',
  styleUrls: ['./confirmation-box.component.scss']
})
export class ConfirmationBoxComponent {

  constructor(private activeModal: NgbActiveModal) { }

  @Input() title: string;

  @Input() message: string;

  @Input() btnOkText: string;

  @Input() btnCancelText: string;

  public decline() {

    this.activeModal.close(false);

    }

    public accept() {

    this.activeModal.close(true);

    }

    public dismiss() {

    this.activeModal.dismiss();

    }
}
