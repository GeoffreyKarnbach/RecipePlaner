import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global';
import { Observable } from 'rxjs';
import { RecipeImagesDto } from '../dtos';

@Injectable({
  providedIn: 'root',
})
export class ImageService {

  private imageBaseUri: string = this.globals.backendUri + '/image';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  uploadOrUpdateImages(images: RecipeImagesDto): Observable<any> {
    return this.httpClient.post<any>(`${this.imageBaseUri}`, images);
  }
}
