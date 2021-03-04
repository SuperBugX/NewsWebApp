import { Injectable } from '@angular/core';
import { Article } from '../models/Article';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  articles: BehaviorSubject<Article[]>;

  constructor() {
    let tempArticles = [
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
    this.articles = new BehaviorSubject<Article[]>(tempArticles);
  }

  getArticles() {
    return this.articles.getValue;
  }

  setArticles(articles: Article[]) {
    this.articles.next(articles);
  }
}
