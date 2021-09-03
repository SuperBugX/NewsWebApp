import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewsCategoryFilterComponent } from './news-category-filter.component';

describe('NewsCategoryFilterComponent', () => {
  let component: NewsCategoryFilterComponent;
  let fixture: ComponentFixture<NewsCategoryFilterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewsCategoryFilterComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewsCategoryFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
