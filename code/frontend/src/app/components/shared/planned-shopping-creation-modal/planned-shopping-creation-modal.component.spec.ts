import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlannedShoppingCreationModalComponent } from './planned-shopping-creation-modal.component';

describe('PlannedShoppingCreationModalComponent', () => {
  let component: PlannedShoppingCreationModalComponent;
  let fixture: ComponentFixture<PlannedShoppingCreationModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PlannedShoppingCreationModalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlannedShoppingCreationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
