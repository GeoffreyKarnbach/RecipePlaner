import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ToastService} from "../../../services";
import {Globals} from "../../../global";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-image-upload',
  templateUrl: './image-upload.component.html',
  styleUrls: ['./image-upload.component.scss']
})
export class ImageUploadComponent implements OnInit{

  @Input() imageURLs: string[] = [];

  imageCounter = 0;
  maxFileSize = 1000000;

  @Output() imageUpdate = new EventEmitter<string[]>();

  constructor(
    private globals: Globals,
    private router: Router,
    private route: ActivatedRoute,
    private toastService: ToastService
  ) {}

  /*
 * Opens the file selection dialog
 */
  selectImage() {
    // @ts-ignore
    document.getElementById('upload-file').click();
  }

  /*
   * Adds the selected image to the list of images
   * If the image is already in the list, it will not be added
   * If the image file size is above 5MB, it will not be added
   * There can be at most 5 images in the list
   *
   * @param event containing the selected image
   */
  onFileSelected(event: any) {
    if (event.target.files[0]) {
      const reader = new FileReader();
      reader.readAsDataURL(event.target.files[0]);
      reader.onload = done => {
        for (const image of this.imageURLs) {
          // @ts-ignore
          if (image === done.target.result) {
            this.toastService.showError('Image already selected', 'Error');
            return;
          }
        }

        if (this.imageCounter >= 5) {
          this.toastService.showError('Maximum 5 images allowed', 'Error');
          return;
        }

        if (event.target.files[0].size > this.maxFileSize) {
          this.toastService.showError('Image size at most 1MB', 'Error');
          return;
        }
      /*
        if (event.target.files[0].naturalWidth !== 350 || event.target.files[0].naturalWidth !== 350){
          this.toastService.showError('Image has to have dimension 350x350 px.');
          return;
        }
        */


        // @ts-ignore
        this.imageURLs.push(done.target.result as string);
        this.imageCounter += 1;

        // Reset file input
        const fileInput = document.getElementById('upload-file') as HTMLInputElement;
        fileInput.value = '';

        // Notify parent component
        this.updateParentComponent();
      };
    }
  }

  /*
   * Moves the image with the given ID one position to the left
   * @param $event ID of the image to be moved
   */
  moveImageLeft($event: number) {
    const imgID = $event - 1;

    const img = this.imageURLs[imgID];
    this.imageURLs[imgID] = this.imageURLs[imgID - 1];
    this.imageURLs[imgID - 1] = img;

    this.updateParentComponent();
  }

  /*
   * Moves the image with the given ID one position to the right
   * @param $event ID of the image to be moved
   */
  moveImageRight($event: number) {
    const imgID = $event - 1;

    const img = this.imageURLs[imgID];
    this.imageURLs[imgID] = this.imageURLs[imgID + 1];
    this.imageURLs[imgID + 1] = img;

    this.updateParentComponent();
  }

  /*
   * Removes the image with the given ID from the list
   * @param $event ID of the image to be removed
   */
  deleteImage($event: number) {
    const imgID = $event - 1;

    this.imageURLs.splice(imgID, 1);
    this.imageCounter -= 1;

    this.updateParentComponent();
  }

  updateParentComponent() {
    this.imageUpdate.emit(this.imageURLs);
  }

  ngOnInit(): void {
  }
}
