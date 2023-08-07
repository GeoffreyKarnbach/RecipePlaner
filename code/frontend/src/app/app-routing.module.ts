import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEditIngredientComponent } from './components';
import { IngredientCreateEditModes } from './components';
import { IngredientListViewComponent } from './components/pages/ingredient/ingredient-list-view/ingredient-list-view.component';

const routes: Routes = [
  { path: 'ingredient', children: [
    { path: 'create', component: CreateEditIngredientComponent, data: { mode: IngredientCreateEditModes.CREATE } },
    { path: 'edit/:id', component: CreateEditIngredientComponent, data: { mode: IngredientCreateEditModes.EDIT } },
    { path: '', component: IngredientListViewComponent }
  ]},
  { path: '**', redirectTo: 'ingredient' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
