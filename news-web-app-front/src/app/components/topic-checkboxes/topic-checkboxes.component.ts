import { Component, OnInit } from '@angular/core';
import { ArticlesService } from 'src/app/services/Articles/articles.service';

@Component({
  selector: 'app-topic-checkboxes',
  templateUrl: './topic-checkboxes.component.html',
  styleUrls: ['./topic-checkboxes.component.css'],
})
export class TopicCheckboxesComponent implements OnInit {
  constructor(private articlesService: ArticlesService) { }

  topics: string[];

  ngOnInit(): void {
    this.topics = ['Sports', 'General', 'Business', 'Entertainment', 'Health', 'Science', 'Technology'];
  }

  updateTopicSubscriptions() {

    let topicsArray: string[];
    let selectedTopicBoxesElements = document.getElementById("topicCheckBoxes").querySelector("input:checked");
    this.articlesService.currentTopicSubscriptions.forEach((topic) => { this.articlesService.unsubscribeTopic(topic) });

    for (var i = 0; selectedTopicBoxesElements[i]; ++i) {
      this.articlesService.subscribeTopic(selectedTopicBoxesElements[i]);
      topicsArray.push(selectedTopicBoxesElements[i]);
    }
    this.articlesService.requestNewsArticles(topicsArray);
  }
}
