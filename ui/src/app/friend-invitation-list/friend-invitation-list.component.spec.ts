import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendInvitationListComponent } from './friend-invitation-list.component';

describe('FriendInvitationListComponent', () => {
  let component: FriendInvitationListComponent;
  let fixture: ComponentFixture<FriendInvitationListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FriendInvitationListComponent]
    });
    fixture = TestBed.createComponent(FriendInvitationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
