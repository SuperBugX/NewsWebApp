import { Component, Input, OnInit } from '@angular/core';
import { Article } from 'src/app/models/Article';

@Component({
  selector: 'app-small-article',
  templateUrl: './small-article.component.html',
  styleUrls: ['./small-article.component.css']
})
export class SmallArticleComponent implements OnInit {

  @Input() article: Article;

  constructor() { }

  ngOnInit(): void {
  }

}
