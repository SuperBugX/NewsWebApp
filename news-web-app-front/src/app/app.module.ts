import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainNavigationComponent } from './components/layout/header/main-navigation/main-navigation.component';
import { ArticlesComponent } from './components/articles/articles.component';
import { ArticleComponent } from './components/article/article.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TopicCheckboxesComponent } from './components/topic-checkboxes/topic-checkboxes.component';
import { ArticlesService } from './services/Articles/articles.service';

@NgModule({
  declarations: [
    AppComponent,
    MainNavigationComponent,
    ArticlesComponent,
    ArticleComponent,
    TopicCheckboxesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule
  ],
  providers: [ArticlesService],
  bootstrap: [AppComponent]
})
export class AppModule { }
