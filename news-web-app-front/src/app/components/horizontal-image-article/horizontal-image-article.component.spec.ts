import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HorizontalImageArticleComponent } from './horizontal-image-article.component';

describe('HorizontalImageArticleComponent', () => {
  let component: HorizontalImageArticleComponent;
  let fixture: ComponentFixture<HorizontalImageArticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HorizontalImageArticleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HorizontalImageArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
