import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Globals } from 'src/app/global';
import { ImageService, ToastService } from 'src/app/services';

@Component({
  selector: 'app-image-view',
  templateUrl: './image-view.component.html',
  styleUrls: ['./image-view.component.scss']
})
export class ImageViewComponent {
  @ViewChild('imageModal') imageModal: any;
  @Input() imageURLs: string[] = [];

  modalImageUrl: string;
  modalImageAlt: string;

  constructor(
    private route: ActivatedRoute,
    private globals: Globals,
    private toastService: ToastService,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
  }

  openImageModal(imageUrl: string): void {
    this.modalImageUrl = imageUrl;
    this.modalImageAlt = 'Image';
    const modalRef = this.modalService.open(this.imageModal, { windowClass: 'image-view-modal-content' });
    modalRef.result.then(
      result => {
        console.log(`Closed with: ${result}`);
      },
      reason => {
        console.log(`Dismissed with: ${reason}`);
      }
    );
  }
}
