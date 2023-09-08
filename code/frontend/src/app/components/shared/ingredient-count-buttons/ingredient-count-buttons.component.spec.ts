import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientCountButtonsComponent } from './ingredient-count-buttons.component';

describe('IngredientCountButtonsComponent', () => {
  let component: IngredientCountButtonsComponent;
  let fixture: ComponentFixture<IngredientCountButtonsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IngredientCountButtonsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IngredientCountButtonsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
