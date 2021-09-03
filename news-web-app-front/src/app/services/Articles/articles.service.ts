import { Injectable } from '@angular/core';
import { Article } from '../../models/Article';
import { BehaviorSubject, Subject } from 'rxjs';
import { WebsocketService } from '../Websocket/websocket.service';
import { Frame } from 'stompjs';
import { HttpClient } from '@angular/common/http';
import { NewsTopicFilters } from 'src/app/models/NewsTopicFilters';

@Injectable({
  providedIn: 'root',
})
export class ArticlesService {
  //Attributes
  articleSearcherBaseURL: string;
  searchedArticles$: BehaviorSubject<Article[]>;
  articles$: Subject<Article>;
  madeNewRequest$: Subject<boolean>;
  needDataReset$: Subject<boolean>;
  hasSubscriptions$: BehaviorSubject<boolean>;
  newsTopicSubscriptions: string[];
  topicSubscriptionServiceURL: string;

  constructor(
    private webSocketService: WebsocketService,
    private http: HttpClient
  ) {
    this.topicSubscriptionServiceURL =
      'ws://localhost:8060/TopicSubscriptionService/stomp-endpoint/websocket';
    this.articleSearcherBaseURL = 'http://localhost:8060/NewsFetcherService';
    this.searchedArticles$ = new BehaviorSubject<Article[]>([]);
    this.hasSubscriptions$ = new BehaviorSubject<boolean>(false);
    this.articles$ = new Subject<Article>();
    this.madeNewRequest$ = new Subject<boolean>();
    this.needDataReset$ = new Subject<boolean>();
    this.newsTopicSubscriptions = [];
  }

  ngOnDestroy() {
    this.webSocketService.disconnect();
  }

  getArticles(country: string, category: string, keywords: string): void {
    const headers = {
      'content-type': 'application/json',
      responsetype: 'application/json',
    };

    let filters = new NewsTopicFilters();
    filters.includedKeyWords = keywords.split(' ');
    filters.includedCountries = [country];

    this.http
      .post(
        this.articleSearcherBaseURL + '/SearchNews?category=' + category,
        JSON.stringify(filters),
        { headers: headers }
      )
      .toPromise()
      .then((message: string) => {
        let articlesJSON = JSON.parse(JSON.stringify(message));
        let articles = [];
        var article = null;

        for (let i = 0; i < articlesJSON.length; i++) {
          article = new Article();
          article.author = articlesJSON[i].author;
          article.category = articlesJSON[i].category;
          article.countryOrigin = articlesJSON[i].countryOrigin;
          article.description = articlesJSON[i].description;
          article.imageUrl = articlesJSON[i].imageUrl;
          article.language = articlesJSON[i].language;
          article.publishedAt = articlesJSON[i].publishedAt;
          article.source = articlesJSON[i].source;
          article.title = articlesJSON[i].title;
          article.url = articlesJSON[i].url;
          articles.push(article);
        }

        this.searchedArticles$.next(articles);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  async connect() {
    return this.webSocketService.connect(this.topicSubscriptionServiceURL);
  }

  getSubscriptions(): string[] {
    return this.newsTopicSubscriptions;
  }

  unsubscribeAllTopics(): void {
    this.webSocketService.unsubscribeAll();
    this.newsTopicSubscriptions = [];
    this.hasSubscriptions$.next(false);
  }

  unsubscribeTopic(topic: string): void {
    this.webSocketService.unsubscribe('/topic/' + topic);
    this.webSocketService.unsubscribe('/user/topic/' + topic);

    let index = this.newsTopicSubscriptions.indexOf(
      topic.substr(0, topic.indexOf('?'))
    );
    if (index != -1) {
      this.newsTopicSubscriptions[index] = null;
    }

    if (this.webSocketService.subscriptions.length == 0) {
      this.hasSubscriptions$.next(false);
    }
  }

  subscribeTopic(topic: string): void {
    this.hasSubscriptions$.next(true);
    if (!this.newsTopicSubscriptions.includes(topic)) {
      this.webSocketService.subscribe('/topic/' + topic, (message: Frame) => {
        let article = Object.assign(new Article(), JSON.parse(message.body));
        this.articles$.next(article);
      });

      this.webSocketService.subscribe(
        '/user/topic/' + topic,
        (message: Frame) => {
          let article = Object.assign(new Article(), JSON.parse(message.body));
          this.articles$.next(article);
        }
      );

      this.newsTopicSubscriptions.push(topic.substr(0, topic.indexOf('?')));
    }
  }
}
