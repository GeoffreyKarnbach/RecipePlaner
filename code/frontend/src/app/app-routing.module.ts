import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEditIngredientComponent } from './components';

const routes: Routes = [
  { path: 'ingredient', children: [
    { path: 'create', component: CreateEditIngredientComponent },
  ]},
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
