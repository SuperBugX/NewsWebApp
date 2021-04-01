import { Component, OnInit } from '@angular/core';
import { ArticlesService } from 'src/app/services/Articles/articles.service';
// JQuery Var (Needed for JQuery)
declare var $: any;

@Component({
  selector: 'app-news-category-filter',
  templateUrl: './news-category-filter.component.html',
  styleUrls: ['./news-category-filter.component.css']
})
export class NewsCategoryFilterComponent implements OnInit {

  // Variables
  generalCheck: boolean;
  sportsCheck: boolean;
  healthCheck: boolean;
  businessCheck: boolean;
  technologyCheck: boolean;
  scienceCheck: boolean;
  entertainmentCheck: boolean;

  constructor(private articleService: ArticlesService) { }

  ngOnInit(): void {

    // JQuery Code Needed for Country Selector in HTML
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

  // Methods
  onSubmit(){
    this.articleService.subscribeTopic('/topic/' + (<HTMLInputElement>document.getElementById("test")).value);
  }

  unSubmit(){
    this.articleService.unsubscribeTopic('/topic/' + (<HTMLInputElement>document.getElementById("test")).value);
  }

  // Button Toggle Methods used for CSS Class Applying in HTML
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

  //   updateTopicSubscriptions() {

  //   let topicsArray: string[];
  //   let selectedTopicBoxesElements = document.getElementById("topicCheckBoxes").querySelector("input:checked");
  //   this.articlesService.currentTopicSubscriptions.forEach((topic) => { this.articlesService.unsubscribeTopic(topic) });

  //   for (var i = 0; selectedTopicBoxesElements[i]; ++i) {
  //     this.articlesService.subscribeTopic(selectedTopicBoxesElements[i]);
  //     topicsArray.push(selectedTopicBoxesElements[i]);
  //   }
  //   this.articlesService.requestNewsArticles(topicsArray);
  // }
}
