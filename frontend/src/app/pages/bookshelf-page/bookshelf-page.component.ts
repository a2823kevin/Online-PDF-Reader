import { Component, OnInit} from '@angular/core';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { BookshelfComponent } from '../../components/bookshelf/bookshelf.component';
import { HttpClient } from '@angular/common/http';
import { UserContextService } from '../../services/user-context.service';
import { NzCardModule } from 'ng-zorro-antd/card';
import { Book } from '../../models/Book';
import { ExtractCategoriesPipe } from '../../pipes/extract-categories.pipe';

@Component({
  selector: 'app-bookshelf-page',
  imports: [
    NzLayoutModule, NzInputModule, NzIconModule, NzCardModule, 
    NavbarComponent,
    BookshelfComponent, 
    ExtractCategoriesPipe
],
  templateUrl: './bookshelf-page.component.html',
  styleUrl: './bookshelf-page.component.scss'
})
export class BookshelfPageComponent implements OnInit {
  constructor(public http: HttpClient, public userContextService: UserContextService) {}

  ngOnInit(): void {
    if (this.userContextService.getUserName()) {
      this.http.get("/api/bookshelf/books").subscribe((response: any)=>{
        this.userContextService.setBooks(response.data as Book[]);
      });
    }
  }
}
