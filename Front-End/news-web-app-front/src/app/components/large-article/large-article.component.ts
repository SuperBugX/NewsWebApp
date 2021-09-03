import { Component, Input, OnInit } from '@angular/core';
import { Article } from 'src/app/models/Article';

@Component({
  selector: 'app-large-article',
  templateUrl: './large-article.component.html',
  styleUrls: ['./large-article.component.css'],
})
export class LargeArticleComponent implements OnInit {

  @Input() article: Article;

  constructor() {

  }

  ngOnInit(): void {}
}
