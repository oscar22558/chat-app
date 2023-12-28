import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupDeleteDialogComponent } from './group-delete-dialog.component';

describe('GroupDeleteDialogComponent', () => {
  let component: GroupDeleteDialogComponent;
  let fixture: ComponentFixture<GroupDeleteDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GroupDeleteDialogComponent]
    });
    fixture = TestBed.createComponent(GroupDeleteDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
