import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-tag',
  templateUrl: './tag.component.html',
  styleUrls: ['./tag.component.scss'],
})
export class TagComponent {
  @Input() tagname: string;
  @Input() isEditable: boolean;
  @Input() position: number;

  @Output() deleteTagEvent = new EventEmitter<number>();

  deleteTag() {
    this.deleteTagEvent.emit(this.position);
  }
}
