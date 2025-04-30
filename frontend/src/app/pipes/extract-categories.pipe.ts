import { Pipe, PipeTransform } from '@angular/core';
import { Book } from '../models/Book';

@Pipe({
  name: 'extractCategories'
})
export class ExtractCategoriesPipe implements PipeTransform {

  transform(books: Book[]): string[] {
    return books.map((book)=>{
      return book.category;
    }).filter((category, index, arr)=>{
      return arr.indexOf(category)===index;
    });
  }

}
