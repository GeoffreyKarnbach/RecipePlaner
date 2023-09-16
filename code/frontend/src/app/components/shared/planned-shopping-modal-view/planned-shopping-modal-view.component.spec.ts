import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlannedShoppingModalViewComponent } from './planned-shopping-modal-view.component';

describe('PlannedShoppingModalViewComponent', () => {
  let component: PlannedShoppingModalViewComponent;
  let fixture: ComponentFixture<PlannedShoppingModalViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlannedShoppingModalViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlannedShoppingModalViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
