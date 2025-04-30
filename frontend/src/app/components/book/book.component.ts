import { Component, Input, Output, resource } from '@angular/core';
import { NzCardModule } from 'ng-zorro-antd/card';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { Book } from '../../models/Book';
import { Bookmark } from '../../models/Bookmark';
import { HttpClient } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { EditBookModalComponent } from '../modals/edit-book-modal/edit-book-modal.component';
import { NzMessageService } from 'ng-zorro-antd/message';
import { DeleteBookModalComponent } from '../modals/delete-book-modal/delete-book-modal.component';

@Component({
  selector: 'app-book',
  imports: [
    NzCardModule, NzIconModule, RouterLink, 
    EditBookModalComponent, DeleteBookModalComponent
  ],
  templateUrl: './book.component.html',
  styleUrl: './book.component.scss'
})
export class BookComponent {
  @Input() book!: Book;

  bookmark: Bookmark | undefined;
  isEditModalVisible: boolean = false;
  isDeleteModalVisible: boolean = false;

  constructor(public http: HttpClient, private messageService: NzMessageService) {}

  ngOnInit(): void {
    this.http.get(`/api/reader/bookmark/${this.book.id}`).subscribe((response: any)=>{
      if (response.status==="ok") {
        this.bookmark = response.data as Bookmark;
      }
    });
  }

  openEdit() {
    this.isEditModalVisible = true;
  }

  downloadPDF() {
    this.http.get(`/api/reader/content/${this.book.id}`, {responseType: "blob"}).subscribe({
      next: (response: any)=>{
        this.messageService.success(`download of ${this.book.bookname}.pdf started.`);
        const url = window.URL.createObjectURL(response)
        const a = document.createElement("a");
        a.href = url;
        a.download = `${this.book.bookname}.pdf`
        a.click();
        URL.revokeObjectURL(url);
      }, 
      error: (err) => {
        this.messageService.success(`download of ${this.book.bookname}.pdf failed.`);
      }
    });
  }

  openDelete() {
    this.isDeleteModalVisible = true;
  }
}
