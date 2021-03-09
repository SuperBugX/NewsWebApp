import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicSubscriptionButtonComponent } from './topic-subscription-button.component';

describe('TopicSubscriptionButtonComponent', () => {
  let component: TopicSubscriptionButtonComponent;
  let fixture: ComponentFixture<TopicSubscriptionButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicSubscriptionButtonComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicSubscriptionButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
