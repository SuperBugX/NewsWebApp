import { Component, OnInit } from '@angular/core';
import { Article } from '../../models/Article';
import { ArticlesService } from 'src/app/services/Articles/articles.service';
import { Router } from '@angular/router';
import { ErrorDialogComponent } from '../dialogs/error-dialog/error-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css'],
})
export class ArticlesComponent implements OnInit {
  //Variables
  articles: Map<string, Article[]>;
  showLoading: boolean;
  newRequest: boolean;
  resetData: boolean;

  constructor(
    private articleService: ArticlesService,
    private router: Router,
    public dialog: MatDialog
  ) {
    this.articles = new Map();

    this.articleService.articles$.subscribe((article) => {
      this.displayArticle(article);
    });

    this.articleService.searchedArticles$.subscribe((articles) => {
      articles.forEach((article) => {
        this.displayArticle(article);
      });
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

    router.events.subscribe((val) => {
      this.articles = new Map();
      this.showLoading = false;
    });
  }

  ngOnInit(): void {
    if (this.router.url != '/search-articles') {
      this.articleService
        .connect()
        .then((value) => { })
        .catch((value) => {
          let data = {
            data: {
              title: 'Connection Error',
              body: 'Unable to connect to server, please refresh the page',
            },
          };

          this.dialog.open(ErrorDialogComponent, data);
        });
    }
  }

  //Methods
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

  removeUnsubscribedContent(): void {
    if (!this.resetData) {
      let subscriptions = this.articleService.getSubscriptions();
      let currentArticleTopics = Array.from(this.articles.keys());

      currentArticleTopics.forEach((topic) => {
        if (!subscriptions.includes(topic)) {
          this.articles.delete(topic);
        }
      });
    } else {
      this.articles = new Map();
      this.resetData = false;
    }
  }
}
