import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleSearcherPageComponent } from './article-searcher-page.component';

describe('ArticleSearcherPageComponent', () => {
  let component: ArticleSearcherPageComponent;
  let fixture: ComponentFixture<ArticleSearcherPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArticleSearcherPageComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleSearcherPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
