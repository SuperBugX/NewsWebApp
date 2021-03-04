import { Component, OnInit } from '@angular/core';
import { Article } from '../../models/Article';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {

  articles: Article[];

  constructor() { }

  ngOnInit(): void {
    this.articles = [
      {
        author: "James",
        title: "Royal Family in Ruins",
        description: "Marriage issues",
        url: "Broken/URL",
        source: "BBC",
        imageUrl: "Image/URL",
        category: "Drama",
        language: "English",
        countryOrigin: "United Kingdom",
        publishedAt: "20/20/20"
      },

      {
        author: "James",
        title: "Royal Family in Ruins2",
        description: "Marriage issues",
        url: "Broken/URL",
        source: "BBC",
        imageUrl: "Image/URL",
        category: "Drama",
        language: "English",
        countryOrigin: "United Kingdom",
        publishedAt: "20/20/20"
      },

      {
        author: "James",
        title: "Royal Family in Ruins3",
        description: "Marriage issues",
        url: "Broken/URL",
        source: "BBC",
        imageUrl: "Image/URL",
        category: "Drama",
        language: "English",
        countryOrigin: "United Kingdom",
        publishedAt: "20/20/20"
      }
    ];
  }

}
