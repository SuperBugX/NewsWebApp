import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-topic-subscription-button',
  templateUrl: './topic-subscription-button.component.html',
  styleUrls: ['./topic-subscription-button.component.css']
})
export class TopicSubscriptionButtonComponent implements OnInit {

  @Input() topicName: string;

  constructor() { }

  ngOnInit(): void {
  }

}
