import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReaderPageComponent } from './reader-page.component';

describe('ReaderPageComponent', () => {
  let component: ReaderPageComponent;
  let fixture: ComponentFixture<ReaderPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReaderPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReaderPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
