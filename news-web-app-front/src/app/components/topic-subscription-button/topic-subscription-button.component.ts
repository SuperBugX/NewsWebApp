import { Component, OnInit, Input } from '@angular/core';
import { ArticleService } from 'src/app/services/article.service';
import { MessageService } from 'src/app/services/message.service';



@Component({
  selector: 'app-topic-subscription-button',
  templateUrl: './topic-subscription-button.component.html',
  styleUrls: ['./topic-subscription-button.component.css']
})
export class TopicSubscriptionButtonComponent implements OnInit {

  @Input() topicName: string;

  constructor(private articleService: ArticleService, private messageService: MessageService) { }

  ngOnInit(): void {
  }



  getNews() {
    
    this.messageService._connect();
  }
}
