import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanedRecipeModalViewComponent } from './planed-recipe-modal-view.component';

describe('PlanedRecipeModalViewComponent', () => {
  let component: PlanedRecipeModalViewComponent;
  let fixture: ComponentFixture<PlanedRecipeModalViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlanedRecipeModalViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanedRecipeModalViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
