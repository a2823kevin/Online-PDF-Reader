import { Component, OnInit } from '@angular/core';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { PdfViewerModule } from 'ng2-pdf-viewer';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { Bookmark } from '../../models/Bookmark';
import { NzFlexModule } from 'ng-zorro-antd/flex';
import { Title } from '@angular/platform-browser';
import { SidebarComponent } from "../../components/sidebar/sidebar.component";

@Component({
  selector: 'app-reader-page',
  imports: [
    NzLayoutModule, NzFlexModule, PdfViewerModule,
    NavbarComponent, SidebarComponent
],
  templateUrl: './reader-page.component.html',
  styleUrl: './reader-page.component.scss'
})
export class ReaderPageComponent implements OnInit {
  id!: string;
  bookmark: Bookmark | undefined;
  pdfSrc: any;

  private _page: number = 1;
  get page() {
    return this._page;
  }
  set page(val: number) {
    this._page = val;
    this.http.put(`/api/reader/bookmark/${this.id}`, val).subscribe((response: any)=>{});
  }
  totalPage: number | undefined;

  constructor(private route: ActivatedRoute, public http: HttpClient, private titleService: Title) {}

  ngOnInit(): void {
    // get id
    this.route.paramMap.subscribe(params => {
      this.id = params.get('id')!;
    });

    // get bookmark
    this.http.get(`/api/reader/bookmark/${this.id}`).subscribe((response: any)=>{
      if (response.status==="ok") {
        this.bookmark = response.data as Bookmark;
        this.page = this.bookmark.page;
        this.totalPage = this.bookmark.totalPage;
        this.titleService.setTitle(`${this.bookmark.bookName} - PDF Reader`);
      }
      else {
        this.http.post(`/api/reader/bookmark/${this.id}`, null).subscribe((response: any)=>{
          if (response.status==="ok") {
            this.bookmark = response.data as Bookmark;
            this.totalPage = this.bookmark.totalPage;
            this.titleService.setTitle(`${this.bookmark.bookName} - PDF Reader`);
          }
        });
      }
    });
    
    // get content
    this.http.get(`/api/reader/content/${this.id}`, {responseType: "blob"}).subscribe({
      next: (response: any)=>{
        const url = window.URL.createObjectURL(response);
        this.pdfSrc = url;
      }, 
      error: (err) => {
        if (err.status === 403) {
          this.pdfSrc = "403";
        }
      }
    });
  }
}
