import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicFilterOptionsComponent } from './topic-filter-options.component';

describe('TopicFilterOptionsComponent', () => {
  let component: TopicFilterOptionsComponent;
  let fixture: ComponentFixture<TopicFilterOptionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicFilterOptionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicFilterOptionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
