import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteBookModalComponent } from './delete-book-modal.component';

describe('DeleteBookModalComponent', () => {
  let component: DeleteBookModalComponent;
  let fixture: ComponentFixture<DeleteBookModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteBookModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeleteBookModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
