import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainNavigationComponent } from './components/layout/header/main-navigation/main-navigation.component';
import { ArticlesComponent } from './components/articles/articles.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ArticlesService } from './services/Articles/articles.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FooterComponent } from './components/layout/footer/footer/footer.component';
import { AccountCardComponent } from './components/account-card/account-card.component';
import { HomapageComponent } from './components/homapage/homapage.component';
import { DatePipe } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { NewsFeedPageComponent } from './components/news-feed-page/news-feed-page.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { SignInComponent } from './components/sign-in/sign-in.component';
import { NewsCategoryFilterComponent } from './components/news-category-filter/news-category-filter.component';
import { LargeArticleComponent } from './components/large-article/large-article.component';
import { SmallArticleComponent } from './components/small-article/small-article.component';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { AccountRegistrationComponent } from './components/account-registration/account-registration.component';
import { MatStepperModule } from '@angular/material/stepper';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { NotFoundPageComponent } from './components/not-found-page/not-found-page.component';
import { MyAccountComponent } from './components/my-account/my-account.component';

@NgModule({
  declarations: [
    AppComponent,
    MainNavigationComponent,
    ArticlesComponent,
    FooterComponent,
    AccountCardComponent,
    HomapageComponent,
    NewsFeedPageComponent,
    SignInComponent,
    NewsCategoryFilterComponent,
    LargeArticleComponent,
    SmallArticleComponent,
    AccountRegistrationComponent,
    NotFoundPageComponent,
    MyAccountComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    BrowserAnimationsModule,
    MatGridListModule,
    MatCheckboxModule,
    MatSelectModule,
    MatButtonModule,
    MatStepperModule,
    MatInputModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    TranslateModule.forRoot({
      defaultLanguage: 'en'
    })
  ],
  providers: [ArticlesService, DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
