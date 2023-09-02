import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeRatingGroupComponent } from './recipe-rating-group.component';

describe('RecipeRatingGroupComponent', () => {
  let component: RecipeRatingGroupComponent;
  let fixture: ComponentFixture<RecipeRatingGroupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecipeRatingGroupComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeRatingGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
