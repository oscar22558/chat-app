import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteMemberDialogComponent } from './delete-member-dialog.component';

describe('DeleteMemberDialogComponent', () => {
  let component: DeleteMemberDialogComponent;
  let fixture: ComponentFixture<DeleteMemberDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeleteMemberDialogComponent]
    });
    fixture = TestBed.createComponent(DeleteMemberDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
