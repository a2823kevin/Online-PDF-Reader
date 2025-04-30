import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzRadioModule } from 'ng-zorro-antd/radio';
import { NzModalModule } from 'ng-zorro-antd/modal';
import { Book, Visibility } from '../../../models/Book';
import { NzFlexModule } from 'ng-zorro-antd/flex';
import { UserContextService } from '../../../services/user-context.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-edit-book-modal',
  imports: [FormsModule, NzFlexModule, NzIconModule, NzModalModule, NzInputModule, NzRadioModule],
  templateUrl: './edit-book-modal.component.html',
  styleUrl: './edit-book-modal.component.scss'
})
export class EditBookModalComponent implements OnInit {
  @Input() isVisible: boolean = false;
  @Output() isVisibleChange: EventEmitter<boolean> = new EventEmitter();
  @Input() book!: Book;

  name: string = "";
  isNameEditing: boolean = false;
  category: string = "";
  isCategoryEditing: boolean = false;
  visibility: Visibility = Visibility.PRIVATE;

  constructor(public http: HttpClient, public userContextService: UserContextService) {}

  ngOnInit(): void {
    this.name = this.book.bookname;
    this.category = this.book.category;
    this.visibility = this.book.visibility;
  }

  enableNameEdit(): void {
    this.isNameEditing = true;
  }
  confirmNameEdit(): void {
    this.isNameEditing = false;
    this.http.put(`/api/bookshelf/book/name/${this.book.id}`, this.name).subscribe((response: any)=>{
      if (response.status==="ok") {
        this.book.bookname = this.name;
        this.updateBooks();
      }
    });
  }
  cancelNameEdit(): void {
    this.name = this.book.bookname;
    this.isNameEditing = false;
  }

  enableCategoryEdit(): void {
    this.isCategoryEditing = true;
  }
  confirmCategoryEdit(): void {
    this.isCategoryEditing = false;
    this.http.put(`/api/bookshelf/book/category/${this.book.id}`, this.category).subscribe((response: any)=>{
      if (response.status==="ok") {
        this.book.category = this.category;
        this.updateBooks();
      }
    });
  }
  cancelCategoryEdit(): void {
    this.category = this.book.category;
    this.isCategoryEditing = false;
  }

  confirmVisibilityEdit(visibility: Visibility): void {
    this.http.put(`/api/bookshelf/book/visibility/${this.book.id}`, `"${visibility}"`, {
      headers : new HttpHeaders({"Content-Type": "application/json"})
    }).subscribe((response: any)=>{
      if (response.status==="ok") {
        this.book.visibility = visibility;
        this.updateBooks();
      }
    });
  }

  updateBooks(): void {
    let books = this.userContextService.getBooks();
    const bookIdx = books.indexOf(books.find(bk=>bk.id===this.book.id) as Book);
    this.userContextService.setBooks([...books.slice(0, bookIdx), this.book, ...books.slice(bookIdx+1)]);
  }

  close() {
    this.isVisible = false;
    this.isVisibleChange.emit(this.isVisible);
  }
}
