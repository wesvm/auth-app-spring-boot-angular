import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEditInfoComponent } from './user-edit-info.component';

describe('UserEditInfoComponent', () => {
  let component: UserEditInfoComponent;
  let fixture: ComponentFixture<UserEditInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserEditInfoComponent]
    });
    fixture = TestBed.createComponent(UserEditInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
