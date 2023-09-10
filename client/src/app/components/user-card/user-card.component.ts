import { Component, Input } from '@angular/core';
import { UserInfoResponse } from 'src/app/models/user-info-response';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent {
  @Input()
  user: UserInfoResponse = { email: '', name: '', profileImage: '' };
}
