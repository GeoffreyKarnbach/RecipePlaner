import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditRecipeComponent } from './create-edit-recipe.component';

describe('CreateEditRecipeComponent', () => {
  let component: CreateEditRecipeComponent;
  let fixture: ComponentFixture<CreateEditRecipeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEditRecipeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditRecipeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
