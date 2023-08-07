import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IngredientListViewComponent } from './ingredient-list-view.component';

describe('IngredientListViewComponent', () => {
  let component: IngredientListViewComponent;
  let fixture: ComponentFixture<IngredientListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ IngredientListViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IngredientListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
