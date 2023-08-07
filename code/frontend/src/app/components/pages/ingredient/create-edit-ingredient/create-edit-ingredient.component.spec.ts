import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditIngredientComponent } from './create-edit-ingredient.component';

describe('CreateEditIngredientComponent', () => {
  let component: CreateEditIngredientComponent;
  let fixture: ComponentFixture<CreateEditIngredientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEditIngredientComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditIngredientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
