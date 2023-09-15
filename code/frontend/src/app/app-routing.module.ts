import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateEditIngredientComponent,
  CreateEditRecipeComponent,
  IngredientCreateEditModes,
  IngredientListViewComponent,
  IngredientViewComponent,
  RecipeCreateEditModes,
  CalendarViewComponent
} from './components';
import { RecipeListViewComponent } from './components/pages/recipe/recipe-list-view/recipe-list-view.component';
import { RecipeViewComponent } from './components/pages/recipe/recipe-view/recipe-view.component';
import { CreateEditRecipeIngredientListComponent } from './components/pages/recipe/create-edit-recipe-ingredient-list/create-edit-recipe-ingredient-list.component';
import { CreateEditRecipeStepsComponent } from './components/pages/recipe/create-edit-recipe-steps/create-edit-recipe-steps.component';

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
    { path: ':id', component: RecipeViewComponent },
    { path: ':id/ingredient', component: CreateEditRecipeIngredientListComponent },
    { path: ':id/step', component: CreateEditRecipeStepsComponent },
    { path: '', component: RecipeListViewComponent }
  ]},
  { path: 'calendar', component: CalendarViewComponent },
  { path: '**', redirectTo: 'ingredient' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
