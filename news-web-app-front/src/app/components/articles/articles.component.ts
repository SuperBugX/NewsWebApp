import { Component, OnInit } from '@angular/core';
import { Article } from '../../models/Article';
import { ArticlesService } from 'src/app/services/Articles/articles.service';

export interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

@Component({
  selector: 'app-articles',
  templateUrl: './articles.component.html',
  styleUrls: ['./articles.component.css']
})
export class ArticlesComponent implements OnInit {


  articles: Article[];

  tiles: Tile[] = [
    { text: 'One', cols: 3, rows: 1, color: 'lightblue' },
    { text: 'Two', cols: 1, rows: 2, color: 'lightgreen' },
    { text: 'Three', cols: 1, rows: 1, color: 'lightpink' },
    { text: 'Four', cols: 2, rows: 1, color: '#DDBDF1' },
  ];

  constructor(/*private _articleService: ArticlesService*/) { }

  ngOnInit(): void {
    //this._articleService.articles.subscribe(x => this.articles = x);
  }
}
