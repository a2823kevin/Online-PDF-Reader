import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NavbarctlContextService {
  private bookNameContainString: string | undefined;

  constructor() { }

  public getBookNameContainString(): string | undefined {
    return this.bookNameContainString;
  }

  public setBookNameContainString(qs: string) {
    if (qs==="") {
      this.bookNameContainString = undefined;
    }
    else {
      this.bookNameContainString = qs;
    }
  }
}
