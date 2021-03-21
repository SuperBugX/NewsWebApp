import { Component, OnInit } from '@angular/core';
import {formatDate} from '@angular/common';

@Component({
  selector: 'app-main-navigation',
  templateUrl: './main-navigation.component.html',
  styleUrls: ['./main-navigation.component.css']
})
export class MainNavigationComponent implements OnInit {

  currentDate: any;

  constructor() {
    this.currentDate = formatDate(new Date(), 'dd/MM/yyyy', 'en');
  }

  ngOnInit(): void {

  }
}