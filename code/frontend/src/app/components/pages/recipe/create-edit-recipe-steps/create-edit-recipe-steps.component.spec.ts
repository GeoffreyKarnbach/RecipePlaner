import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditRecipeStepsComponent } from './create-edit-recipe-steps.component';

describe('CreateEditRecipeStepsComponent', () => {
  let component: CreateEditRecipeStepsComponent;
  let fixture: ComponentFixture<CreateEditRecipeStepsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEditRecipeStepsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditRecipeStepsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
