import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
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
    let subscriptions = this.articleService.getSubscriptions();
    let tempCountryValue : string;
    let dataReseted: boolean;

    if(this.countryInput.nativeElement.value == "--"){
      tempCountryValue = "";
    }
    else{
      tempCountryValue = this.countryInput.nativeElement.value;
    }

    if(this.chosenCountry.localeCompare(tempCountryValue) != 0 || this.chosenLanguage.localeCompare(this.languageInput.nativeElement.value) != 0 || this.activeTopics.length == 0){
      alert("I DLETE");
      this.articleService.needDataReset$.next(true);
      this.articleService.unsubscribeAllTopics();
      dataReseted = true;
    }
    else{
      subscriptions.forEach((subscription) =>{
        if(!this.activeTopics.includes(subscription)){
          this.articleService.unsubscribeTopic(subscription + "?" + "country=" + this.chosenCountry + "&" + "lang=" + this.chosenLanguage);
        }
      })
    }

    this.chosenCountry = tempCountryValue;
    this.chosenLanguage = this.languageInput.nativeElement.value;
    let subscriptionRequest;

    for (var i = 0; this.activeTopics[i]; ++i) {
      if(!subscriptions.includes(this.activeTopics[i]) || dataReseted){
        subscriptionRequest = this.activeTopics[i] + "?" + "country=" + this.chosenCountry + "&" + "lang=" + this.chosenLanguage;
        this.articleService.subscribeTopic(subscriptionRequest);
      }
    }

    this.articleService.madeNewRequest$.next(true);
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
