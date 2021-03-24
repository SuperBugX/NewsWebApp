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
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FooterComponent } from './components/layout/footer/footer/footer.component';
import { AccountCardComponent } from './components/account-card/account-card.component';
import { HomapageComponent } from './components/homapage/homapage.component';
import { DatePipe } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NewsFeedPageComponent } from './components/news-feed-page/news-feed-page.component';
import { MatGridListModule } from '@angular/material/grid-list';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  declarations: [
    AppComponent,
    MainNavigationComponent,
    ArticlesComponent,
    ArticleComponent,
    TopicCheckboxesComponent,
    FooterComponent,
    AccountCardComponent,
    HomapageComponent,
    NewsFeedPageComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    BrowserAnimationsModule,
    MatGridListModule,
    TranslateModule.forRoot({
      defaultLanguage: 'en'
    })
  ],
  providers: [ArticlesService, DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
