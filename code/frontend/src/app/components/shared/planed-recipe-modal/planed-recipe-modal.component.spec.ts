import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlanedRecipeModalComponent } from './planed-recipe-modal.component';

describe('PlanedRecipeModalComponent', () => {
  let component: PlanedRecipeModalComponent;
  let fixture: ComponentFixture<PlanedRecipeModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlanedRecipeModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlanedRecipeModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
