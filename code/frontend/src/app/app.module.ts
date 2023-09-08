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
import { RecipeViewComponent } from './components/pages/recipe/recipe-view/recipe-view.component';
import { ImageViewComponent } from './components/shared/image-view/image-view.component';
import { CreateEditRecipeIngredientListComponent } from './components/pages/recipe/create-edit-recipe-ingredient-list/create-edit-recipe-ingredient-list.component';
import { CreateEditRecipeStepsComponent } from './components/pages/recipe/create-edit-recipe-steps/create-edit-recipe-steps.component';
import { RecipeStepComponent } from './components/shared/recipe-step/recipe-step.component';
import { RecipeStepEditComponent } from './components/shared/recipe-step-edit/recipe-step-edit.component';
import { SingleRecipeRatingComponent } from './components/shared/single-recipe-rating/single-recipe-rating.component';
import { RecipeRatingGroupComponent } from './components/shared/recipe-rating-group/recipe-rating-group.component';
import { ConfirmationBoxComponent } from './components/shared/confirmation-box/confirmation-box.component';
import { NgbModalModule  } from '@ng-bootstrap/ng-bootstrap';
import { IngredientCountButtonsComponent } from './components/shared/ingredient-count-buttons/ingredient-count-buttons.component';

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
    TagComponent,
    RecipeViewComponent,
    ImageViewComponent,
    CreateEditRecipeIngredientListComponent,
    CreateEditRecipeStepsComponent,
    RecipeStepComponent,
    RecipeStepEditComponent,
    SingleRecipeRatingComponent,
    RecipeRatingGroupComponent,
    ConfirmationBoxComponent,
    IngredientCountButtonsComponent
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
