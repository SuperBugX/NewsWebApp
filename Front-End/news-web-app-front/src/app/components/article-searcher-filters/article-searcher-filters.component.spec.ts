import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleSearcherFiltersComponent } from './article-searcher-filters.component';

describe('ArticleSearcherFiltersComponent', () => {
  let component: ArticleSearcherFiltersComponent;
  let fixture: ComponentFixture<ArticleSearcherFiltersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArticleSearcherFiltersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleSearcherFiltersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
