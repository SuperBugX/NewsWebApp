import { Component, OnInit } from '@angular/core';
import { Article } from '../../models/Article';
import { ArticlesService } from 'src/app/services/Articles/articles.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css'],
})
export class ArticlesComponent implements OnInit {
  articles: Map<string, Article[]>;
  showLoading: boolean;
  newRequest: boolean;
  resetData: boolean;

  constructor(private articleService: ArticlesService) {
    this.articles = new Map();

    this.articleService.articles$.subscribe((article) => {
      this.displayArticle(article);
    });

    this.showLoading = false;
    this.articleService.hasSubscriptions$.subscribe((value) => {
      this.showLoading = value;
    });

    this.newRequest = false;
    this.articleService.madeNewRequest$.subscribe((value) => {
      this.newRequest = value;
      this.removeUnsubscribedContent();
    });

    this.resetData = false;
    this.articleService.needDataReset$.subscribe((value) => {
      this.resetData = value;
    });
  }

  ngOnInit(): void {}

  displayArticle(article: Article) {
    this.showLoading = false;

    if (this.articles.has(article.category)) {
      this.articles.get(article.category).push(article);
    } else {
      let newArray: Article[] = [];
      newArray.push(article);
      this.articles.set(article.category, newArray);
    }
  }

  getMapKeys(map) {
    return Array.from(map.keys());
  }

  removeUnsubscribedContent() {
    if (!this.resetData) {
      let subscriptions = this.articleService.getSubscriptions();
      let currentArticleTopics = Array.from(this.articles.keys());

      alert('Current : ' + currentArticleTopics);
      alert('Sub : ' + subscriptions);

      currentArticleTopics.forEach((topic) => {
        if (!subscriptions.includes(topic)) {
          alert('rgtrh   : ' + topic);
          this.articles.delete(topic);
        }
      });
    } else {
      this.articles = new Map();
      this.resetData = false;
    }
  }
}
