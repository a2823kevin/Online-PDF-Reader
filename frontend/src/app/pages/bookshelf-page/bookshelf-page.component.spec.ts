import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BookshelfPageComponent } from './bookshelf-page.component';

describe('BookshelfPageComponent', () => {
  let component: BookshelfPageComponent;
  let fixture: ComponentFixture<BookshelfPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookshelfPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BookshelfPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
