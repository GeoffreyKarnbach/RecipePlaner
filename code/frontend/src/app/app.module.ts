import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule }   from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ToastComponent } from './components';
import { NgbToast } from "@ng-bootstrap/ng-bootstrap";
import { CreateEditIngredientComponent } from './components/pages/ingredient/create-edit-ingredient/create-edit-ingredient.component';
import { IngredientListViewComponent } from './components/pages/ingredient/ingredient-list-view/ingredient-list-view.component';
import { IngredientCardComponent } from './components/shared/ingredient-card/ingredient-card.component';
import { IngredientViewComponent } from './components/pages/ingredient/ingredient-view/ingredient-view.component';

@NgModule({
  declarations: [
    AppComponent,
    ToastComponent,
    CreateEditIngredientComponent,
    IngredientListViewComponent,
    IngredientCardComponent,
    IngredientViewComponent,
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
