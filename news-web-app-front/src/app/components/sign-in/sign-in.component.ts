import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormControl, Validators, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-sign-in',
  templateUrl: './sign-in.component.html',
  styleUrls: ['./sign-in.component.css']
})
export class SignInComponent implements OnInit {

  loginForm: FormGroup;

  constructor(private router: Router, private formBuilder: FormBuilder) {
    if (false) {
      router.navigate(['/']);
    }
  }

  ngOnInit(): void {

    this.loginForm = this.formBuilder.group({
      username: ['', [
        Validators.required,
        Validators.pattern('^[\/a-zA-Z0-9/d]+$'),
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
    })
  }

  get username() {
    return this.loginForm.get('username');
  }

  get password() {
    return this.loginForm.get('password');
  }

  get remember() {
    return this.loginForm.get('remember');
  }

  onSubmit() {
    console.warn(this.loginForm.value);
  }

}
