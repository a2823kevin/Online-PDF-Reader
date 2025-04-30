import { Pipe, PipeTransform } from '@angular/core';
import { Book } from '../models/Book';

@Pipe({
  name: 'filterByCategory'
})
export class FilterByCategoryPipe implements PipeTransform {

  transform(books: Book[], category: string): Book[] {
    return books.filter((book)=>{
      return book.category===category;
    });
  }

}
