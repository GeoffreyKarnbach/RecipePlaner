import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleRecipeRatingComponent } from './single-recipe-rating.component';

describe('SingleRecipeRatingComponent', () => {
  let component: SingleRecipeRatingComponent;
  let fixture: ComponentFixture<SingleRecipeRatingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SingleRecipeRatingComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SingleRecipeRatingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
