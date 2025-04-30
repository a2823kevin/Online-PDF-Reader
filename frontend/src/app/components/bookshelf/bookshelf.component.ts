import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { scrollBy } from "seamless-scroll-polyfill";
import { BookComponent } from '../book/book.component';
import { HttpClient } from '@angular/common/http';
import { Book } from '../../models/Book';
import { UploadAreaComponent } from '../upload-area/upload-area.component';
import { FilterByQueryPipe } from '../../pipes/filter-by-query.pipe';
import { NavbarctlContextService } from '../../services/navbarctl-context.service';
import { UserContextService } from '../../services/user-context.service';
import { FilterByCategoryPipe } from '../../pipes/filter-by-category.pipe';
import { FilterByIdsPipe } from '../../pipes/filter-by-ids.pipe';

@Component({
  selector: 'app-bookshelf',
  imports: [
    NzIconModule, NzGridModule, NzButtonModule, 
    BookComponent, UploadAreaComponent, 
    FilterByQueryPipe, FilterByCategoryPipe, FilterByIdsPipe
  ],
  templateUrl: './bookshelf.component.html',
  styleUrl: './bookshelf.component.scss'
})

export class BookshelfComponent {
  @Input()
  category!: string;

  @ViewChild("rbookshelf", { static: false })
  rbookshelf!: ElementRef;

  recentReadIds: string[] = [];

  constructor(public http: HttpClient, public navbarctlContextService: NavbarctlContextService, public userContextService: UserContextService) {}

  ngOnInit(): void {
    if (this.category==="recent") {
      this.http.get("/api/bookshelf/books/recent/12").subscribe((response: any)=>{
        if (response.status==="ok") {
          this.recentReadIds = (response.data as Book[]).map((book)=>{return book.id});
        }
      });
    }
  }

  scrollLeft() {
    scrollBy(this.rbookshelf.nativeElement, {
      left: -this.rbookshelf.nativeElement.clientWidth, 
      behavior: "smooth"
    })
  }

  scrollRight() {
    scrollBy(this.rbookshelf.nativeElement, {
      left: this.rbookshelf.nativeElement.clientWidth, 
      behavior: "smooth"
    })
  }
}
