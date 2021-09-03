import { Component, OnInit } from '@angular/core';
// JQuery Var (Needed for JQuery)
declare var $: any;

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css'],
})
export class MyAccountComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {
    // JQuery Code Needed for Country Selector in HTML

    $('#country_selector').countrySelect({
      defaultCountry: '--',
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
        '--',
      ],
      responsiveDropdown: true,
    });
  }
}
