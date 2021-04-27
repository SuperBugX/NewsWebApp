import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/Authentication/authentication.service';
import {MatDialog} from '@angular/material/dialog';
import {ErrorDialogComponent} from '../dialogs/error-dialog/error-dialog.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {

  // Variables
  loginForm: FormGroup;

  constructor(private router: Router, private formBuilder: FormBuilder, private authenticationService:AuthenticationService, public dialog: MatDialog) {
    if (this.authenticationService.isLoggedIn()) {
      // Code used for redirecting
      router.navigate(['/']);
    }
    else{

      // Angular Reactive Login Form
      this.loginForm = this.formBuilder.group({
        username: ['', [
          Validators.required,
          Validators.pattern('^[A-Za-z0-9]+$'),
          Validators.minLength(5),
          Validators.maxLength(25),

        ]],
        password: ['', [
          Validators.required,
          Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]+$'),
          Validators.minLength(10),
          Validators.maxLength(100),

        ]],
        remember: [false, [

        ]],
      });
    }
  }

  ngOnInit(): void {

  }

  // Getters And Setters for Login Form
  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get remember() {
    return this.loginForm.get('remember');
  }

  async onSubmit() {
    this.authenticationService.login(this.username.value, this.password.value).then(status =>{
      this.router.navigate(["/homepage"], {skipLocationChange: true});
    })
    .catch(errorCode =>{

      this.loginForm.reset();
      let data;

      switch(errorCode){

        case 0:
          data = {data:{
            title: "Could Not Connect",
            body: "The Website was unable to connect, please try again later"
          }};
          break;

        case 503:
          data = {data:{
            title: "Could Not Connect",
            body: "The Website was unable to connect, please try again later"
          }};
          break;

        case 403:
          data = {data:{
            title: "Invalid Credentials",
            body: "Try Using Another Username or Password"
          }};
          break;

        case 422:
          data = {data:{
            title: "Invalid Credentials",
            body: "Try Using Another Username or Password"
          }};
          break;

        default:
          data = {data:{
            title: "Unknown Error",
            body: "If the issue persists, please contact support"
          }};
          break;
      }

      this.dialog.open(ErrorDialogComponent, data);
    })
  }
}
