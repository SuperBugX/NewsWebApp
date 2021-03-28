import { Component, OnInit } from '@angular/core';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.css']
})
export class MainNavigationComponent implements OnInit {

  // Variables
  currentDate: any;

  constructor(private datePipe: DatePipe) {
    // Code to retrieve todays date and transform it through an angular pipe to the current locale of the web app
    this.currentDate = datePipe.transform(new Date());
  }

  ngOnInit(): void {

  }
}