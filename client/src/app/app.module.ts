import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';

import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from "@angular/common/http";
import { FormsModule } from '@angular/forms';
import { HttpInterceptorService } from './interceptor/http-interceptor.service';

import { MatIconModule } from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog'
import { CommonModule } from '@angular/common';

import { AppComponent } from './app.component';

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { AdminComponent } from './components/admin/admin.component';
import { HomeComponent } from './components/home/home.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { UserCardComponent } from './components/user-card/user-card.component';
import { UserEditInfoComponent } from './components/user-edit-info/user-edit-info.component';
import { UserEditInfoModalComponent } from './components/user-edit-info-modal/user-edit-info-modal.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ActivateMfaModalComponent } from './components/activate-mfa-modal/activate-mfa-modal.component';

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent,
        AdminComponent,
        HomeComponent,
        NotFoundComponent,
        UserCardComponent,
        UserEditInfoComponent,
        UserEditInfoModalComponent,
        NavbarComponent,
        ActivateMfaModalComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        MatIconModule,
        CommonModule,
        MatDialogModule
    ],
    providers: [
        HttpClient,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: HttpInterceptorService,
            multi: true
        }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
