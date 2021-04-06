import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ArticlesService } from 'src/app/services/Articles/articles.service';
// JQuery Var (Needed for JQuery)
declare var $: any;

@Component({
  selector: 'app-news-category-filter',
  templateUrl: './news-category-filter.component.html',
  styleUrls: ['./news-category-filter.component.css'],
})
export class NewsCategoryFilterComponent implements OnInit {

  @ViewChild('countryInput') countryInput: ElementRef;
  @ViewChild('languageInput') languageInput: ElementRef;

  // Variables
  activeTopics: string[];
  chosenLanguage: string;
  chosenCountry: string;
  generalCheck: boolean;
  sportsCheck: boolean;
  healthCheck: boolean;
  businessCheck: boolean;
  technologyCheck: boolean;
  scienceCheck: boolean;
  entertainmentCheck: boolean;

  constructor(private articleService: ArticlesService) {
    this.activeTopics = [];
    this.chosenLanguage = '';
    this.chosenCountry = '';
    this.generalCheck = false;
    this.sportsCheck = false;
    this.healthCheck = false;
    this.businessCheck = false;
    this.technologyCheck = false;
    this.scienceCheck = false;
    this.entertainmentCheck = false;
  }

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

  // Methods
  getArticles() {
    this.articleService.unsubscribeAllTopics();
    this.articleService.madeNewRequest$.next(true);
    this.chosenCountry = this.countryInput.nativeElement.value;
    this.chosenLanguage = this.languageInput.nativeElement.value;
    let subscriptionRequest;

    for (var i = 0; this.activeTopics[i]; ++i) {
      subscriptionRequest = this.activeTopics[i] + "?" + "country=" + this.chosenCountry + "&" + "lang=" + this.chosenLanguage;
      this.articleService.subscribeTopic(subscriptionRequest);
    }
  }

  // Button Toggle Methods used for CSS Class Applying in HTML
  generalChecked(event): void {
    this.generalCheck = event.target.checked;
    this.updateChosenTopicsList(event.srcElement.value);
  }

  sportsChecked(event): void {
    this.sportsCheck = event.target.checked;
    this.updateChosenTopicsList(event.srcElement.value);
  }

  healthChecked(event): void {
    this.healthCheck = event.target.checked;
    this.updateChosenTopicsList(event.srcElement.value);
  }

  businessChecked(event): void {
    this.businessCheck = event.target.checked;
    this.updateChosenTopicsList(event.srcElement.value);
  }

  technologyChecked(event): void {
    this.technologyCheck = event.target.checked;
    this.updateChosenTopicsList(event.srcElement.value);
  }

  scienceChecked(event): void {
    this.scienceCheck = event.target.checked;
    this.updateChosenTopicsList(event.srcElement.value);
  }

  entertainmentChecked(event): void {
    this.entertainmentCheck = event.target.checked;
    this.updateChosenTopicsList(event.srcElement.value);
  }

  updateChosenTopicsList(topic: string): void {
    let index = this.activeTopics.indexOf(topic);

    if (index == -1) {
      this.activeTopics.push(topic);
    } else {
      this.activeTopics.splice(index, 1);
    }
  }
}
