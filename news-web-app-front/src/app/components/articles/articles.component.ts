import { Component, ComponentFactoryResolver, OnInit, ViewChild, ViewContainerRef } from '@angular/core';
import { Article } from '../../models/Article';
import { ArticlesService } from 'src/app/services/Articles/articles.service';

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})

export class ArticlesComponent implements OnInit {

  articles: Article[];
  showLoading: boolean;

  constructor(private articleService: ArticlesService) {
    this.articles =[];
    this.articleService.articles$.subscribe((article) => {this.updateArticlesView(article)})
    this.articleService.hasSubscriptions.subscribe((value) => {this.showLoading = value});
  }

  ngOnInit(): void {
  }

  updateArticlesView(article:Article){
    this.showLoading = false;
    this.articles.push(article);
  }
}
