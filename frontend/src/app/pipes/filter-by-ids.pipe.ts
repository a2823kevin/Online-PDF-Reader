import { Pipe, PipeTransform } from '@angular/core';
import { Book } from '../models/Book';

@Pipe({
  name: 'filterByIds'
})
export class FilterByIdsPipe implements PipeTransform {

  transform(books: Book[], ids: string[]): Book[] {
    let newBooks: Book[] = [];
    ids.forEach((id)=>{
      let bk = books.find((book)=>{return id===book.id;});
      if (bk) {
        newBooks.push(bk);
      }
    });
    return newBooks;
  }

}
