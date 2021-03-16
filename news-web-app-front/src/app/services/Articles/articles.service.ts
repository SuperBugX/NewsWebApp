import { Injectable } from '@angular/core';
import { Article } from '../../models/Article';
import { BehaviorSubject } from 'rxjs';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root',
})
export class ArticlesService {
  articles: BehaviorSubject<Article[]>;
  topicSubscriptionServiceURL: string;
  websocket: WebSocket;
  stompClient: Stomp.Client;
  test: any;

  constructor() {
    this.topicSubscriptionServiceURL =
      'ws://localhost:8060/TopicSubscriptionService/gs-guide-websocket/websocket';


    let tempArticles = [
      {
        author: 'James',
        title: 'Royal Family in Ruins',
        description: 'Marriage issues',
        url: 'Broken/URL',
        source: 'BBC',
        imageUrl: 'Image/URL',
        category: 'Drama',
        language: 'English',
        countryOrigin: 'United Kingdom',
        publishedAt: '20/20/20',
      },

      {
        author: 'James',
        title: 'Royal Family in Ruins2',
        description: 'Marriage issues',
        url: 'Broken/URL',
        source: 'BBC',
        imageUrl: 'Image/URL',
        category: 'Drama',
        language: 'English',
        countryOrigin: 'United Kingdom',
        publishedAt: '20/20/20',
      },

      {
        author: 'James',
        title: 'Royal Family in Ruins3',
        description: 'Marriage issues',
        url: 'Broken/URL',
        source: 'BBC',
        imageUrl: 'Image/URL',
        category: 'Drama',
        language: 'English',
        countryOrigin: 'United Kingdom',
        publishedAt: '20/20/20',
      },
    ];
    this.articles = new BehaviorSubject<Article[]>(tempArticles);
    this.connectToSubscriptionService();
  }

  getArticles() {
    return this.articles.getValue;
  }

  setArticles(articles: Article[]) {
    this.articles.next(articles);
  }

  connectToSubscriptionService() {
    this.websocket = new WebSocket(this.topicSubscriptionServiceURL);
    this.stompClient = Stomp.over(this.websocket);

    this.stompClient.connect({}, (frame) => {
    this.test = this.stompClient.subscribe('/errors', (message) => {
        console.log('Error: ' + message);
      });

      this.test = this.stompClient.subscribe('/topic/general', (message) => {
        console.log('/topic/greetings: ' + message);
      });
    },
    (error) =>{
      alert("STOMP error " + error);
    });
  }

  unsub(){
    this.test.unsubscribe();
  }

  sendMessage(message){
    if(this.stompClient){
      this.stompClient.send("/app/general", {}, message);
    }
    else{
      alert("No connection");
    }
  }
}
