import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignInOrRegisterComponent } from './sign-in-or-register.component';

describe('SignInOrRegisterComponent', () => {
  let component: SignInOrRegisterComponent;
  let fixture: ComponentFixture<SignInOrRegisterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SignInOrRegisterComponent]
    });
    fixture = TestBed.createComponent(SignInOrRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
