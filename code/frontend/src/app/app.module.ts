import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import {
  ToastComponent,
  CreateEditIngredientComponent,
  IngredientListViewComponent,
  IngredientCardComponent,
  IngredientViewComponent,
  CreateEditRecipeComponent,
  TagComponent
 } from './components';

import { NgbToast } from "@ng-bootstrap/ng-bootstrap";
import { RecipeCardComponent } from './components/shared/recipe-card/recipe-card.component';
import { RecipeListViewComponent } from './components/pages/recipe/recipe-list-view/recipe-list-view.component';
import { ImageUploadComponent } from './components/shared/image-upload/image-upload.component';
import { SingleImageComponent } from './components/shared/image-upload/single-image/single-image.component';
import { NavbarComponent } from './components/shared/navbar/navbar.component';


@NgModule({
  declarations: [
    AppComponent,
    ToastComponent,
    CreateEditIngredientComponent,
    IngredientListViewComponent,
    IngredientCardComponent,
    IngredientViewComponent,
    CreateEditRecipeComponent,
    RecipeCardComponent,
    RecipeListViewComponent,
    ImageUploadComponent,
    SingleImageComponent,
    NavbarComponent,
    TagComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    NgbModule,
    NgbToast,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
