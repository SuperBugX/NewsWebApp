import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
declare var $: any;

@Component({
  selector: 'app-account-registration',
  templateUrl: './account-registration.component.html',
  styleUrls: ['./account-registration.component.css']
})
export class AccountRegistrationComponent implements OnInit {

  accountForm: FormGroup;
  topicForm: FormGroup;

  constructor(private formBuilder: FormBuilder) { }

  ngOnInit(): void {

    this.accountForm = this.formBuilder.group({
      username: ['', [

      ]],
      email: ['', [

      ]],
      password: ['', [

      ]],
      passwordConfirm: ['', [

      ]],
      preferedCountry: ['', [

      ]]
    });

    this.topicForm = this.formBuilder.group({
      secondCtrl: ['', Validators.required]
    });

    $("#country_selector").countrySelect({
      defaultCountry: "gb",
      onlyCountries: [
        'ar', 'au', 'at', 'be', 'br', 'bg', 'ca', 'cn', 'co', 'cz', 'eg', 'fr', 'de',
        'gr', 'hk', 'hu', 'in', 'id', 'ie', 'il', 'it', 'jp', 'lv', 'lt', 'my', 'mx',
        'ma', 'nl', 'nz', 'ng', 'no', 'ph', 'pl', 'pt', 'ro', 'sa', 'rs', 'sg', 'sk',
        'si', 'za', 'kr', 'se', 'ch', 'tw', 'th', 'tr', 'ae', 'ua', 'gb', 'us', 've'
      ],
      responsiveDropdown: true,
    });
  }
}
