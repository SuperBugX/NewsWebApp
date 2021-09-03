import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// JQuery Var (Needed for JQuery)
declare var $: any;

@Component({
  selector: 'app-account-registration',
  templateUrl: './account-registration.component.html',
  styleUrls: ['./account-registration.component.css'],
})
export class AccountRegistrationComponent implements OnInit {
  // Variables
  accountForm: FormGroup;
  topicForm: FormGroup;
  generalCheck: boolean;
  sportsCheck: boolean;
  healthCheck: boolean;
  businessCheck: boolean;
  technologyCheck: boolean;
  scienceCheck: boolean;
  entertainmentCheck: boolean;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    // Account Form
    this.accountForm = this.formBuilder.group(
      {
        username: [
          '',
          [
            Validators.required,
            Validators.pattern('^[A-Za-z0-9]+$'),
            Validators.minLength(5),
            Validators.maxLength(25),
          ],
        ],
        email: ['', [Validators.required, Validators.email]],
        password: [
          '',
          [
            Validators.required,
            Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]+$'),
            Validators.minLength(10),
            Validators.maxLength(100),
          ],
        ],
        passwordConfirm: ['', [Validators.required]],
        preferedCountry: ['', []],
      },
      {
        validator: this.mustMatch('password', 'passwordConfirm'),
      }
    );

    // News Topic Form
    this.topicForm = this.formBuilder.group({});

    $(document).ready(function () {
      // JQuery Code Needed for Country Selector in HTML
      $('#country_selector').countrySelect({
        defaultCountry: 'gb',
        onlyCountries: [
          'ar',
          'au',
          'at',
          'be',
          'br',
          'bg',
          'ca',
          'cn',
          'co',
          'cz',
          'eg',
          'fr',
          'de',
          'gr',
          'hk',
          'hu',
          'in',
          'id',
          'ie',
          'il',
          'it',
          'jp',
          'lv',
          'lt',
          'my',
          'mx',
          'ma',
          'nl',
          'nz',
          'ng',
          'no',
          'ph',
          'pl',
          'pt',
          'ro',
          'sa',
          'rs',
          'sg',
          'sk',
          'si',
          'za',
          'kr',
          'se',
          'ch',
          'tw',
          'th',
          'tr',
          'ae',
          'ua',
          'gb',
          'us',
          've',
        ],
        responsiveDropdown: true,
      });
    });
  }

  // Methods
  // custom validator to check that two fields match
  mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.mustMatch) {
        // return if another validator has already found an error on the matchingControl
        return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
    };
  }
  // Getters And Setters for Registration Form
  get username() {
    return this.accountForm.get('username');
  }

  get password() {
    return this.accountForm.get('password');
  }

  get passwordConfirm() {
    return this.accountForm.get('passwordConfirm');
  }

  get email() {
    return this.accountForm.get('email');
  }

  // Button Toggle Methods used for CSS Class Applying in HTML
  generalChecked(event): void {
    this.generalCheck = event.target.checked;
  }

  sportsChecked(event): void {
    this.sportsCheck = event.target.checked;
  }

  healthChecked(event): void {
    this.healthCheck = event.target.checked;
  }

  businessChecked(event): void {
    this.businessCheck = event.target.checked;
  }

  technologyChecked(event): void {
    this.technologyCheck = event.target.checked;
  }

  scienceChecked(event): void {
    this.scienceCheck = event.target.checked;
  }

  entertainmentChecked(event): void {
    this.entertainmentCheck = event.target.checked;
  }
}
