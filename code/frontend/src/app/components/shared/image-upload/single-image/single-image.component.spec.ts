import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SingleImageComponent } from './single-image.component';

describe('SingleImageComponent', () => {
  let component: SingleImageComponent;
  let fixture: ComponentFixture<SingleImageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SingleImageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SingleImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
