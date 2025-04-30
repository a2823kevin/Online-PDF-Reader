import { Pipe, PipeTransform } from '@angular/core';
import { Book } from '../models/Book';

@Pipe({
  name: 'filterByQuery'
})
export class FilterByQueryPipe implements PipeTransform {

  transform(books: Book[], qs: string | undefined): Book[] {
    if (!qs) {
      return books;
    }
    return books.filter((book)=>{
      return book.bookname.toLowerCase().includes(qs.toLowerCase());
    });
  }

}
