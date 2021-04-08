import { Injectable } from '@angular/core';
import { Article } from '../../models/Article';
import { BehaviorSubject, Subject } from 'rxjs';
import { WebsocketService } from './websocket.service';
import { Frame } from 'stompjs';

@Injectable({
  providedIn: 'root',
})
export class ArticlesService {
  //Attributes
  articles$: Subject<Article>;
  madeNewRequest$: Subject<boolean>;
  needDataReset$: Subject<boolean>;
  hasSubscriptions$: BehaviorSubject<boolean>;
  newsTopicSubscriptions: string[];
  topicSubscriptionServiceURL: string;

  constructor(private webSocketService: WebsocketService) {
    this.hasSubscriptions$ = new BehaviorSubject<boolean>(false);
    this.articles$ = new Subject<Article>();
    this.madeNewRequest$ = new Subject<boolean>();
    this.needDataReset$ = new Subject<boolean>();
    this.newsTopicSubscriptions = [];
    this.topicSubscriptionServiceURL =
      'ws://localhost:8060/TopicSubscriptionService/stomp-endpoint/websocket';
    this.webSocketService.connect(this.topicSubscriptionServiceURL);
  }

  ngOnDestroy() {
    this.webSocketService.disconnect();
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

    let index = this.newsTopicSubscriptions.indexOf(topic.substr(0, topic.indexOf('?')));
    if(index != -1){
      this.newsTopicSubscriptions.splice(index,1);
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
