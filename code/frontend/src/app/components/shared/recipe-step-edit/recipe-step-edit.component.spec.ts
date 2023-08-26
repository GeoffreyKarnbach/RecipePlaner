import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeStepEditComponent } from './recipe-step-edit.component';

describe('RecipeStepEditComponent', () => {
  let component: RecipeStepEditComponent;
  let fixture: ComponentFixture<RecipeStepEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecipeStepEditComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeStepEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
