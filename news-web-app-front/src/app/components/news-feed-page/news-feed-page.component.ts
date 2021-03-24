import { Component, OnInit } from '@angular/core';
declare var $: any;

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

@Component({
  selector: 'app-news-feed-page',
  templateUrl: './news-feed-page.component.html',
  styleUrls: ['./news-feed-page.component.css']
})
export class NewsFeedPageComponent implements OnInit {

  tiles: Tile[] = [
    { text: 'One', cols: 3, rows: 1, color: 'lightblue' },
    { text: 'Two', cols: 1, rows: 2, color: 'lightgreen' },
    { text: 'Three', cols: 1, rows: 1, color: 'lightpink' },
    { text: 'Four', cols: 2, rows: 1, color: '#DDBDF1' },
  ];

  generalCheck: boolean;
  sportsCheck: boolean;
  healthCheck: boolean;
  businessCheck: boolean;
  technologyCheck: boolean;
  scienceCheck: boolean;
  entertainmentCheck: boolean;

  constructor() { }

  ngOnInit(): void {

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

  generalChecked(event) {
    this.generalCheck = event.target.checked;
  }

  sportsChecked(event) {
    this.sportsCheck = event.target.checked;
  }

  healthChecked(event) {
    this.healthCheck = event.target.checked;
  }

  businessChecked(event) {
    this.businessCheck = event.target.checked;
  }

  technologyChecked(event) {
    this.technologyCheck = event.target.checked;
  }

  scienceChecked(event) {
    this.scienceCheck = event.target.checked;
  }

  entertainmentChecked(event) {
    this.entertainmentCheck = event.target.checked;
  }
}
