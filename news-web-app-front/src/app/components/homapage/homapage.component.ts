import { Component, OnInit } from '@angular/core';
declare var $, JQuery: any;

@Component({
  selector: 'app-homapage',
  templateUrl: './homapage.component.html',
  styleUrls: ['./homapage.component.css']
})
export class HomapageComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {

    $("#country_selector").countrySelect({
      defaultCountry: "gb",
      // onlyCountries: ['us', 'gb', 'ch', 'ca', 'do'],
      responsiveDropdown: true
    });
  }
}
