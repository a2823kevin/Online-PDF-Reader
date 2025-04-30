import { Injectable, signal } from '@angular/core';
import { jwtDecode, JwtPayload } from "jwt-decode";
import { Book } from '../models/Book';
import { Role } from '../models/Role';

@Injectable({
  providedIn: 'root'
})
export class UserContextService {
  private userName: string | undefined;
  private userRoles: Role[] = [];
  private booksSignal = signal<Book[]>([]);

  constructor() {
    const jwt = localStorage.getItem("opr_token");
    if (jwt) {
      const payload = jwtDecode(jwt) as any;
      if (this.isExpired(payload)) {
        localStorage.removeItem("opr_token");
      }
      else {
        if (payload.sub) {
          this.setUserName(payload.sub);
        }
        if (payload.roles) {
          this.setUserRoles(payload.roles);
        }
      }
    }
  }

  public setUserName(name: string) {
    this.userName = name;
  }

  public getUserName(): string | undefined {
    return this.userName;
  }

  public setUserRoles(roles: Role[]) {
    this.userRoles = roles;
  }

  public getUserRoles(): Role[] {
    return this.userRoles;
  }

  public setBooks(books: Book[]) {
    this.booksSignal.set(books);
  }

  public getBooks(): Book[] {
    return this.booksSignal();
  }

  private isExpired(payload: JwtPayload): boolean {
    try {
      if (payload.exp) {
        const currentTime = Date.now() / 1000;
        return payload.exp < currentTime;
      }
      return true;
    }
    catch (e) {
      return true;
    }
  }
}
