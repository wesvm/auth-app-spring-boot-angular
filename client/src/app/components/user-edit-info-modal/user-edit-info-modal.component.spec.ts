import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEditInfoModalComponent } from './user-edit-info-modal.component';

describe('UserEditInfoModalComponent', () => {
  let component: UserEditInfoModalComponent;
  let fixture: ComponentFixture<UserEditInfoModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserEditInfoModalComponent]
    });
    fixture = TestBed.createComponent(UserEditInfoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
