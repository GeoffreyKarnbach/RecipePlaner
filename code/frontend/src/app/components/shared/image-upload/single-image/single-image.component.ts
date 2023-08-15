import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-single-image',
  templateUrl: './single-image.component.html',
  styleUrls: ['./single-image.component.scss']
})
export class SingleImageComponent {
  @Input() imagePosition: number = 0;
  @Input() imageUrl = '';
  @Input() totalImageNumber: number = 0;

  @Output() moveImageLeftEvent = new EventEmitter<number>();
  @Output() moveImageRightEvent = new EventEmitter<number>();
  @Output() deleteImageEvent = new EventEmitter<number>();

  moveLeft() {
    console.log("Move forward!!");
    this.moveImageLeftEvent.emit(this.imagePosition);
  }

  moveRight() {
    console.log("Move backwards!!");
    this.moveImageRightEvent.emit(this.imagePosition);
  }

  removeImage() {
    console.log("Remove image!!");
    this.deleteImageEvent.emit(this.imagePosition);
  }
}
