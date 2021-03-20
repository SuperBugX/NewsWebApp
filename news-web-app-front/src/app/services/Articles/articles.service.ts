import { Injectable } from '@angular/core';
import { Article } from '../../models/Article';
import { BehaviorSubject } from 'rxjs';
import * as Stomp from 'stompjs';
import axios from 'axios';

@Injectable({
  providedIn: 'root',
})
export class ArticlesService {
  articles: BehaviorSubject<Article[]>;
  topicSubscriptionServiceURL: string;
  currentTopicSubscriptions: string[];
  websocket: WebSocket;
  stompClient: Stomp.Client;

  constructor() {
    this.topicSubscriptionServiceURL = 'ws://localhost:8060/TopicSubscriptionService/topics-websocket/websocket';


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
      this.stompClient.subscribe('/errors', (message) => {
        console.log('Error: ' + message);
      });

    },
      (error) => {
        alert("STOMP error " + error);
      });
  }

  unsubscribeTopic(topic: string) {
    this.stompClient.unsubscribe(topic);

    let index = this.currentTopicSubscriptions.indexOf(topic);
    if (index !== -1) {
      this.currentTopicSubscriptions.splice(index, 1);
    }
  }

  subscribeTopic(topic: string) {
    this.stompClient.subscribe(topic, (message) => {
      let newJSONArticles = Object.assign(new Article(), message.body);
      this.articles.value.push(newJSONArticles);
    });
    this.currentTopicSubscriptions.push(topic);
  }

  sendMessage(destination: string, message: string) {
    if (this.stompClient) {
      this.stompClient.send(destination, {}, message);
    }
    else {
      alert("No connection");
    }
  }

  requestNewsArticles(topics) {

    let url = "http://localhost:8060/NewsFetcherService/Demonstration2?categories=";

    for (let i = 0; i < topics.length; i++) {
      url += topics[i];

      if (i != topics.length - 1) {
        url += ",";
      }
    }

    axios.get(url)
      .then(response => {
        console.log(response);
      });
  }
}
