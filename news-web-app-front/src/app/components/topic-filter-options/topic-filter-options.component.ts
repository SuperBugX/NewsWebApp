import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-topic-filter-options',
  templateUrl: './topic-filter-options.component.html',
  styleUrls: ['./topic-filter-options.component.css']
})
export class TopicFilterOptionsComponent implements OnInit {

  @Input() topics: string[];

  constructor() { }

  ngOnInit(): void {
  }

}
