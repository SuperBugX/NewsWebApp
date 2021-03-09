import { Component, OnInit } from '@angular/core';
import { Article } from '../../models/Article';
import { ArticleService } from 'src/app/services/article.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {

  articles: Article[];

  constructor(private articleService: ArticleService) { }

  ngOnInit(): void {
    this.articleService.articles.subscribe(x => this.articles = x);
  }
}
