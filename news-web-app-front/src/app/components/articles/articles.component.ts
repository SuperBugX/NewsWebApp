import { Component, OnInit } from '@angular/core';
import { Article } from '../../models/Article';
import { ArticlesService } from 'src/app/services/Articles/articles.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {

  articles: Article[];

  constructor(private _articleService: ArticlesService) { }

  ngOnInit(): void {
    this._articleService.articles.subscribe(x => this.articles = x);
  }
}
