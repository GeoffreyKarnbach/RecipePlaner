import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecipeListViewComponent } from './recipe-list-view.component';

describe('RecipeListViewComponent', () => {
  let component: RecipeListViewComponent;
  let fixture: ComponentFixture<RecipeListViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecipeListViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecipeListViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
