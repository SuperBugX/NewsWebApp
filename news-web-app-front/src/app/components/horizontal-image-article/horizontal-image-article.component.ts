import { Component, Input, OnInit  } from '@angular/core';
import { Article } from 'src/app/models/Article';

@Component({
  selector: 'app-horizontal-image-article',
  templateUrl: './horizontal-image-article.component.html',
  styleUrls: ['./horizontal-image-article.component.css']
})
export class HorizontalImageArticleComponent implements OnInit {

  @Input() article: Article;

  constructor() {
  }

  ngOnInit(): void {
  }

}
