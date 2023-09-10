import { Component, EventEmitter, Inject, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-user-edit-info-modal',
  templateUrl: './user-edit-info-modal.component.html',
  styleUrls: ['./user-edit-info-modal.component.css']
})
export class UserEditInfoModalComponent {
  selectedImage: File | null = null;
  previewImageSrc = '';

  @Output() saveImage = new EventEmitter();

  constructor(
    private userService: UserService,
    private dialogRef: MatDialogRef<UserEditInfoModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { this.previewImageSrc = data.image }

  handleImageUpload(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files && inputElement.files.length > 0) {
      this.selectedImage = inputElement.files[0];
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.previewImageSrc = e.target.result;
      };
      reader.readAsDataURL(this.selectedImage);
    }
  }

  uploadImage(): void {
    if (this.selectedImage) {
      this.userService.uploadProfileImage(this.selectedImage).subscribe({
        next: (res: string) => {
          alert('great, reload the page');
        },
        error: (error) => {
          alert(error);
        }
      });
      this.dialogRef.close();
    }
  }

  closeModal(): void {
    this.dialogRef.close();
  }
}
