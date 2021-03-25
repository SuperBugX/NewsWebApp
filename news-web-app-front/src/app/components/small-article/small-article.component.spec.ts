import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SmallArticleComponent } from './small-article.component';

describe('SmallArticleComponent', () => {
  let component: SmallArticleComponent;
  let fixture: ComponentFixture<SmallArticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SmallArticleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SmallArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
