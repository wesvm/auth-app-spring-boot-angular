import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MfaResponse } from 'src/app/models/mfa-response';
import { TfaRequest } from 'src/app/models/tfa-request';
import { UserInfoResponse } from 'src/app/models/user-info-response';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  //private baseUrl = "http://localhost:8080/api/auth";
  private baseUrl = "http://192.168.1.84:8080/api/users"

  constructor(
    private http: HttpClient
  ) { }

  uploadProfileImage(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(`${this.baseUrl}/info/upload-image`, formData, { responseType: 'text' });
  }

  getUserInfo(): Observable<UserInfoResponse> {
    return this.http.get<UserInfoResponse>(`${this.baseUrl}/info`);
  }

  getAllUserInfo(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}`);
  }

  getEnableMfa(): Observable<MfaResponse> {
    return this.http.get<MfaResponse>(`${this.baseUrl}/activate-tfa`);
  }

  validateMfa(authRequest: TfaRequest): Observable<string> {
    return this.http.post(`${this.baseUrl}/validate-tfa`, authRequest, { responseType: 'text' });
  }

}
