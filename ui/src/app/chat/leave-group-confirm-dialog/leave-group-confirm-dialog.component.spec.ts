import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaveGroupConfirmDialogComponent } from './leave-group-confirm-dialog.component';

describe('LeaveGroupConfirmDialogComponent', () => {
  let component: LeaveGroupConfirmDialogComponent;
  let fixture: ComponentFixture<LeaveGroupConfirmDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LeaveGroupConfirmDialogComponent]
    });
    fixture = TestBed.createComponent(LeaveGroupConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
