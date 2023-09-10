import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MfaResponse } from 'src/app/models/mfa-response';
import { TfaRequest } from 'src/app/models/tfa-request';
import { TokenService } from 'src/app/services/token/token.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-activate-mfa-modal',
  templateUrl: './activate-mfa-modal.component.html',
  styleUrls: ['./activate-mfa-modal.component.css']
})
export class ActivateMfaModalComponent {
  errorMessage = '';
  otpCode = '';
  ticket = '';
  qrImage = '';
  generate = false;

  mfaResponse: MfaResponse = { qrcode: '', ticket: '' };

  constructor(
    private dialogRef: MatDialogRef<ActivateMfaModalComponent>,
    private service: UserService,
    private storage: TokenService
  ) { }

  generateQr() {
    this.generate = true;
    this.service.getEnableMfa().subscribe({
      next: (response: MfaResponse) => {
        this.qrImage = response.qrcode;
        this.ticket = response.ticket;
      }
    })

  }

  closeErrorMessage() {
    this.errorMessage = '';
  }

  verifyCode() {
    const mfaRequest: TfaRequest = {
      email: this.storage.getUserName() as string,
      code: this.otpCode,
      ticket: this.ticket
    };
    this.service.validateMfa(mfaRequest).subscribe({
      next: (response: string) => {
        alert(response + ', reload the page!');
        this.closeModal();
      },
      error: (error) => {
        alert(error.message);
      }
    })
  }

  closeModal(): void {
    this.dialogRef.close();
  }

}
