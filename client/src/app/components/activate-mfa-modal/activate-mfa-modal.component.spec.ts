import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActivateMfaModalComponent } from './activate-mfa-modal.component';

describe('ActivateMfaModalComponent', () => {
  let component: ActivateMfaModalComponent;
  let fixture: ComponentFixture<ActivateMfaModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ActivateMfaModalComponent]
    });
    fixture = TestBed.createComponent(ActivateMfaModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
