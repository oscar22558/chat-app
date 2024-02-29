import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupInvitationListComponent } from './group-invitation-list.component';

describe('GroupInvitationListComponent', () => {
  let component: GroupInvitationListComponent;
  let fixture: ComponentFixture<GroupInvitationListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GroupInvitationListComponent]
    });
    fixture = TestBed.createComponent(GroupInvitationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
