import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SendFriendInvitationComponent } from './send-friend-invitation.component';

describe('SendFriendRequestComponent', () => {
  let component: SendFriendInvitationComponent;
  let fixture: ComponentFixture<SendFriendInvitationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SendFriendInvitationComponent]
    });
    fixture = TestBed.createComponent(SendFriendInvitationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
