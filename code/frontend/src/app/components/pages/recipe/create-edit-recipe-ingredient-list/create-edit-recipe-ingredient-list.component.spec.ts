import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditRecipeIngredientListComponent } from './create-edit-recipe-ingredient-list.component';

describe('CreateEditRecipeIngredientListComponent', () => {
  let component: CreateEditRecipeIngredientListComponent;
  let fixture: ComponentFixture<CreateEditRecipeIngredientListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEditRecipeIngredientListComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditRecipeIngredientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
