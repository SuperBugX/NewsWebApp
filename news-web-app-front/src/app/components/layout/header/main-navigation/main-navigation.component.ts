import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';

import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.css']
})
export class MainNavigationComponent implements OnInit {

  // Variables
  currentDate: any;
  currentUrl: string;

  constructor(private datePipe: DatePipe, private router: Router) {
    // Code to retrieve todays date and transform it through an angular pipe to the current locale of the web app
    this.currentDate = datePipe.transform(new Date());
    router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe((event: NavigationEnd) =>{
      this.currentUrl = event.url;
    });
  }

  ngOnInit(): void {

  }
}