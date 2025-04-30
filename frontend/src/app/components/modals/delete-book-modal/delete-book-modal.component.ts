import { HttpClient } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { Book } from '../../../models/Book';
import { NzMessageService } from 'ng-zorro-antd/message';
import { UserContextService } from '../../../services/user-context.service';

@Component({
  selector: 'app-delete-book-modal',
  imports: [NzButtonModule, NzModalModule],
  templateUrl: './delete-book-modal.component.html',
  styleUrl: './delete-book-modal.component.scss'
})
export class DeleteBookModalComponent {
  @Input() isVisible: boolean = false;
  @Output() isVisibleChange: EventEmitter<boolean> = new EventEmitter();
  @Input() book!: Book;

  isLoading: boolean = false;

  constructor(public http: HttpClient, private messageService: NzMessageService, public userContextService: UserContextService) {}

  delete() {
    this.http.delete(`/api/bookshelf/book/${this.book.id}`).subscribe((response: any)=>{
      if (response.status==="ok") {
        this.updateBooks();
        this.messageService.success(`${this.book.bookname} has been deleted successfully.`);
      }
      else {
        this.messageService.error(`deletion of ${this.book.bookname} failed.`);
      }
      this.close();
    });
  }

  updateBooks(): void {
    let books = this.userContextService.getBooks();
    const bookIdx = books.indexOf(books.find(bk=>bk.id===this.book.id) as Book);
    this.userContextService.setBooks([...books.slice(0, bookIdx), ...books.slice(bookIdx+1)]);
  }

  close() {
    this.isVisible = false;
    this.isLoading = false;
    this.isVisibleChange.emit(this.isVisible);
  }
}
