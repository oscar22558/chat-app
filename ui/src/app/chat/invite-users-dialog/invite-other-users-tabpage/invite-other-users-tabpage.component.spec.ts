import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InviteOtherUsersTabpageComponent } from './invite-other-users-tabpage.component';

describe('InviteOtherUsersTabpageComponent', () => {
  let component: InviteOtherUsersTabpageComponent;
  let fixture: ComponentFixture<InviteOtherUsersTabpageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InviteOtherUsersTabpageComponent]
    });
    fixture = TestBed.createComponent(InviteOtherUsersTabpageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
