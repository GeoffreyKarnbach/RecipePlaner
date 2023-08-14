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
  CreateEditRecipeComponent
 } from './components';

import { NgbToast } from "@ng-bootstrap/ng-bootstrap";
import { RecipeCardComponent } from './components/shared/recipe-card/recipe-card.component';
import { RecipeListViewComponent } from './components/pages/recipe/recipe-list-view/recipe-list-view.component';


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
