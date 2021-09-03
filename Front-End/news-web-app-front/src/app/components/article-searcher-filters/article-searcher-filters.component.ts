import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ArticlesService } from 'src/app/services/Articles/articles.service';
import { ErrorDialogComponent } from '../dialogs/error-dialog/error-dialog.component';
// JQuery Var (Needed for JQuery)
declare var $: any;

@Component({
  selector: 'app-article-searcher-filters',
  templateUrl: './article-searcher-filters.component.html',
  styleUrls: ['./article-searcher-filters.component.css'],
})
export class ArticleSearcherFiltersComponent implements OnInit {
  @ViewChild('countryInput') countryInput: ElementRef;
  @ViewChild('keywordsInput') keywordsInput: ElementRef;

  // Variables
  activeTopic: string;
  keywords: string;
  chosenCountry: string;
  generalCheck: boolean;
  sportsCheck: boolean;
  healthCheck: boolean;
  businessCheck: boolean;
  technologyCheck: boolean;
  scienceCheck: boolean;
  entertainmentCheck: boolean;


  constructor(private articlesService: ArticlesService, public dialog: MatDialog) {
    this.activeTopic = '';
    this.keywords = '';
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
    $(document).ready(function () {
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
    });
  }

  // Methods
  getArticles(): void {
    let tempCountryValue: string;

    if (this.countryInput.nativeElement.value == '--') {
      tempCountryValue = '';
    } else {
      tempCountryValue = this.countryInput.nativeElement.value;
    }

    if(this.activeTopic){
      this.articlesService.madeNewRequest$.next(true);
      this.articlesService.getArticles(
        tempCountryValue,
        this.activeTopic,
        this.keywordsInput.nativeElement.value
      ).catch((error) => {
        let data = {
          data: {
            title: 'Error',
            body: error,
          },
        };

        this.dialog.open(ErrorDialogComponent, data);
      })
    }
    else{
      let data = {
        data: {
          title: 'Error',
          body: "You must select atleast one news category",
        },
      };

      this.dialog.open(ErrorDialogComponent, data);
    }
  }

  // Button Toggle Methods used for CSS Class Applying in HTML
  generalChecked(event): void {
    this.resetCategorySelection();
    this.activeTopic = event.srcElement.value;
    this.generalCheck = event.target.checked;
  }

  sportsChecked(event): void {
    this.resetCategorySelection();
    this.activeTopic = event.srcElement.value;
    this.sportsCheck = event.target.checked;
  }

  healthChecked(event): void {
    this.resetCategorySelection();
    this.activeTopic = event.srcElement.value;
    this.healthCheck = event.target.checked;
  }

  businessChecked(event): void {
    this.resetCategorySelection();
    this.activeTopic = event.srcElement.value;
    this.businessCheck = event.target.checked;
  }

  technologyChecked(event): void {
    this.resetCategorySelection();
    this.activeTopic = event.srcElement.value;
    this.technologyCheck = event.target.checked;
  }

  scienceChecked(event): void {
    this.resetCategorySelection();
    this.activeTopic = event.srcElement.value;
    this.scienceCheck = event.target.checked;
  }

  entertainmentChecked(event): void {
    this.resetCategorySelection();
    this.activeTopic = event.srcElement.value;
    this.entertainmentCheck = event.target.checked;
  }

  resetCategorySelection(): void {
    this.activeTopic = '';
    this.generalCheck = false;
    this.sportsCheck = false;
    this.healthCheck = false;
    this.businessCheck = false;
    this.technologyCheck = false;
    this.scienceCheck = false;
    this.entertainmentCheck = false;
  }
}
