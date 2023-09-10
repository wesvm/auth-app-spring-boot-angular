import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UserInfoResponse } from 'src/app/models/user-info-response';
import { AuthService } from 'src/app/services/auth.service';
import { TokenService } from 'src/app/services/token/token.service';
import { UserService } from 'src/app/services/user/user.service';
import { UserEditInfoModalComponent } from '../user-edit-info-modal/user-edit-info-modal.component';
import { ActivateMfaModalComponent } from '../activate-mfa-modal/activate-mfa-modal.component';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent {

    user: UserInfoResponse = { email: '', name: 'there', profileImage: '' };
    users: Array<UserInfoResponse> = [];

    constructor(
        private authService: AuthService,
        private userService: UserService,
        private router: Router,
        private tokenService: TokenService,
        public dialog: MatDialog
    ) {
    }

    openModal() {
        this.dialog.open(UserEditInfoModalComponent, {
            width: '300px',
            data: {
                image: this.user.profileImage
            }
        });
    }

    openMfaModal() {
        this.dialog.open(ActivateMfaModalComponent, {

        })
    }

    ngOnInit(): void {
        this.getUserInfo();
        this.findAllUsers();
    }

    private getUserInfo() {
        this.userService.getUserInfo().subscribe({
            next: (response: UserInfoResponse) => {
                this.user = response;
            },
            error: (error) => {
                console.error('Error loading user info:', error);
            }
        });
    }

    private findAllUsers() {
        this.userService.getAllUserInfo().subscribe({
            next: (response) => {
                this.users = response.content;
            },
            error: (error) => {
                console.error('Error loading users:', error);
            }
        });
    }

    logout() {
        this.authService.logout().subscribe({
            next: (response) => {
                this.tokenService.removeToken();
                this.router.navigate(['login']);
            },
            error: (error) => {
                console.error('Logout error:', error);
            }
        });
    }

}