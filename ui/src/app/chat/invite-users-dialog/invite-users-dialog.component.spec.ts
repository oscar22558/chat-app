import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InviteUsersDialogComponent } from './invite-users-dialog.component';

describe('InviteUserDialogComponent', () => {
  let component: InviteUsersDialogComponent;
  let fixture: ComponentFixture<InviteUsersDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InviteUsersDialogComponent]
    });
    fixture = TestBed.createComponent(InviteUsersDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
