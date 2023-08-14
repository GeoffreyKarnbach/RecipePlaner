import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEditIngredientComponent,
  CreateEditRecipeComponent,
  IngredientCreateEditModes,
  IngredientListViewComponent,
  IngredientViewComponent,
  RecipeCreateEditModes
} from './components';
import { RecipeListViewComponent } from './components/pages/recipe/recipe-list-view/recipe-list-view.component';

const routes: Routes = [
  { path: 'ingredient', children: [
    { path: 'create', component: CreateEditIngredientComponent, data: { mode: IngredientCreateEditModes.CREATE } },
    { path: 'edit/:id', component: CreateEditIngredientComponent, data: { mode: IngredientCreateEditModes.EDIT } },
    { path: ':id', component: IngredientViewComponent },
    { path: '', component: IngredientListViewComponent }
  ]},
  { path: 'recipe', children: [
    { path: 'create', component: CreateEditRecipeComponent, data: { mode: RecipeCreateEditModes.CREATE } },
    { path: 'edit/:id', component: CreateEditRecipeComponent, data: { mode: RecipeCreateEditModes.EDIT } },
    { path: '', component: RecipeListViewComponent }
  ]},
  { path: '**', redirectTo: 'ingredient' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
