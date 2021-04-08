import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Article } from '../../models/Article';
import { ArticlesService } from 'src/app/services/Articles/articles.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css'],
})
export class ArticlesComponent implements OnInit {
  @ViewChild('firstArticleColumn') firstArticleColumn: ElementRef;
  @ViewChild('secondArticleColumn') secondArticleColumn: ElementRef;

  articles: Article[];
  showLoading: boolean;
  showingArticles: boolean;
  newRequest: boolean;
  hasPreviouslyRequested: boolean;

  constructor(private articleService: ArticlesService) {
    this.articles = [];
    this.articleService.articles$.subscribe((article) => {
      this.displayArticle(article);
    });
    this.articleService.hasSubscriptions$.subscribe((value) => {
      this.showLoading = value;
    });
    this.newRequest = false;
    this.articleService.madeNewRequest$.subscribe((value) => {
      this.newRequest = value;
    });
  }

  ngOnInit(): void {}

  displayArticle(article: Article) {
    this.showLoading = false;

    if (this.newRequest) {
      this.clearView();
      this.newRequest = false;
    }

    this.articles.push(article);
  }

  clearView() {
    this.articles = [];
  }
}
