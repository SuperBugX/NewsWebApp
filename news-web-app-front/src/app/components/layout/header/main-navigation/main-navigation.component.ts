import { Component, OnInit } from '@angular/core';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.css']
})
export class MainNavigationComponent implements OnInit {

  currentDate: any;

  constructor(private datePipe: DatePipe) {
    this.currentDate = datePipe.transform(new Date());
  }

  ngOnInit(): void {

  }
}