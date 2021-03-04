import { Component, OnInit, Input } from '@angular/core';
import { ArticleService } from 'src/app/services/article.service';
import axios from 'axios';
import { WebsocketService } from 'src/app/services/websocket.service';



@Component({
  selector: 'app-topic-subscription-button',
  templateUrl: './topic-subscription-button.component.html',
  styleUrls: ['./topic-subscription-button.component.css']
})
export class TopicSubscriptionButtonComponent implements OnInit {

  @Input() topicName: string;

  constructor(private articleService: ArticleService, private websocketService: WebsocketService) { }

  ngOnInit(): void {
  }

  getNews() {
    /*
    axios
      .get(`http://localhost:8060/NewsFetcherService/Demonstration2`)
      .then((response) => {
        // JSON responses are automatically parsed.
        console.log(response);
        this.articleService.setArticles(response.data);
      });
    */
    this.websocketService.connect("ws://localhost:8060/subscription-service/subscribe/websocket");

  }
}
