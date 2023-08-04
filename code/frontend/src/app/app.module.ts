import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { ToastComponent } from './components';
import { NgbToast } from "@ng-bootstrap/ng-bootstrap";
import { CreateEditIngredientComponent } from './components/pages/ingredient/create-edit-ingredient/create-edit-ingredient.component';

@NgModule({
  declarations: [
    AppComponent,
    ToastComponent,
    CreateEditIngredientComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    NgbToast
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
