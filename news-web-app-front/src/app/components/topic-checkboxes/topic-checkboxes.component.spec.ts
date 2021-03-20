import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopicCheckboxesComponent } from './topic-checkboxes.component';

describe('TopicCheckboxesComponent', () => {
  let component: TopicCheckboxesComponent;
  let fixture: ComponentFixture<TopicCheckboxesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopicCheckboxesComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopicCheckboxesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
