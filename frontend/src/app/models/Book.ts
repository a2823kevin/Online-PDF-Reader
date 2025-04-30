export enum Visibility {
    PRIVATE = "private", 
    WITHLINK = "withlink", 
    PUBLIC = "public"
}

export interface Book {
  id: string;
  bookname: string;
  category: string;
  visibility: Visibility;
  owner: string;
  thumbnail: string;
}