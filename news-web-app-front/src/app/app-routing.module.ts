import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomapageComponent } from './components/homapage/homapage.component';
import { NewsFeedPageComponent } from './components/news-feed-page/news-feed-page.component';

const routes: Routes = [
  { path: '', component: HomapageComponent },
  { path: 'homepage', component: HomapageComponent },
  { path: 'news-feed-page', component: NewsFeedPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
