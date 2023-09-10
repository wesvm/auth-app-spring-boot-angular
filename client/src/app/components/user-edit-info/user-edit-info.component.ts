import { Component, EventEmitter, Input, Output } from '@angular/core';
import { UserInfoResponse } from 'src/app/models/user-info-response';

@Component({
  selector: 'app-user-edit-info',
  templateUrl: './user-edit-info.component.html',
  styleUrls: ['./user-edit-info.component.css']
})
export class UserEditInfoComponent {
  @Input()
  user: UserInfoResponse = { email: '', name: '', profileImage: '', mfaEnabled: false };

  @Output() activeTfa = new EventEmitter();
  @Output() update = new EventEmitter();

  onActiveTfa() {
    this.activeTfa.emit(this.user);
  }
  onUpdate() {
    this.update.emit(this.user);
  }
}
