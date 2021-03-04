import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainNavigationComponent } from './components/layout/header/main-navigation/main-navigation.component';
import { TopicSubscriptionButtonComponent } from './components/topic-subscription-button/topic-subscription-button.component';
import { ArticlesComponent } from './components/articles/articles.component';
import { ArticleComponent } from './components/article/article.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { TopicSubscriptionButtonsComponent } from './components/topic-subscription-buttons/topic-subscription-buttons.component';
import { TopicFilterOptionsComponent } from './components/topic-filter-options/topic-filter-options.component';

@NgModule({
  declarations: [
    AppComponent,
    MainNavigationComponent,
    TopicSubscriptionButtonComponent,
    ArticlesComponent,
    ArticleComponent,
    TopicSubscriptionButtonsComponent,
    TopicFilterOptionsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
