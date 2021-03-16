import { Component, OnInit } from '@angular/core';
import { ArticlesService } from 'src/app/services/Articles/articles.service';

@Component({
  selector: 'app-topic-checkboxes',
  templateUrl: './topic-checkboxes.component.html',
  styleUrls: ['./topic-checkboxes.component.css'],
})
export class TopicCheckboxesComponent implements OnInit {
  constructor(private articlesService: ArticlesService) {}

  topics: string[];

  ngOnInit(): void {
    this.topics = ['Sports', 'General'];
  }

  updateNewsFeed() {
    this.articlesService.sendMessage('YESYSYS');
  }

  unsub() {
    this.articlesService.unsub();
  }
}
