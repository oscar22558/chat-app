import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InviteFriendsTabpageComponent } from './invite-friends-tabpage.component';

describe('InviteFriendsTabpageComponent', () => {
  let component: InviteFriendsTabpageComponent;
  let fixture: ComponentFixture<InviteFriendsTabpageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InviteFriendsTabpageComponent]
    });
    fixture = TestBed.createComponent(InviteFriendsTabpageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
