import { Component, OnInit, Input } from '@angular/core';
import axios from 'axios';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-topic-subscription-button',
  templateUrl: './topic-subscription-button.component.html',
  styleUrls: ['./topic-subscription-button.component.css']
})
export class TopicSubscriptionButtonComponent implements OnInit {

  @Input() topicName: string;

  constructor(private articleService: ArticleService) { }

  ngOnInit(): void {
  }

  getNews() {
    axios
      .get(`http://localhost:8060/NewsFetcherService/Demonstration2`)
      .then((response) => {
        // JSON responses are automatically parsed.
        console.log(response);
        this.articleService.setArticles(response.data);
      });
  }
}
