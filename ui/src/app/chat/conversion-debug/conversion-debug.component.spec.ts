import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConversionDebugComponent } from './conversion-debug.component';

describe('ConversionDebugComponent', () => {
  let component: ConversionDebugComponent;
  let fixture: ComponentFixture<ConversionDebugComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConversionDebugComponent]
    });
    fixture = TestBed.createComponent(ConversionDebugComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
