import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { NzFlexModule } from 'ng-zorro-antd/flex';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzMessageService } from 'ng-zorro-antd/message';
import { NzUploadChangeParam, NzUploadModule } from 'ng-zorro-antd/upload';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-upload-area',
  imports: [NzFlexModule, NzUploadModule, NzIconModule],
  templateUrl: './upload-area.component.html',
  styleUrl: './upload-area.component.scss'
})
export class UploadAreaComponent {
  isUploading: boolean = false;

  constructor(public http: HttpClient, private messageService: NzMessageService) {}

  handleChange({ file }: NzUploadChangeParam): void {
    const status = file.status;

    if (status==="done") {
      this.messageService.success(`${file.name} uploaded successfully.`);
      setTimeout(()=>{
        console.log(file)
        window.location.href = `/reader/${file.response.bookId}`
      }, 1500);
    }
    else if (status==="error") {
      this.isUploading = false;
      this.messageService.error(`${file.name} upload failed.`);
    }
  }

  handleUpload = (item: any): Subscription => {
    const formData = new FormData();
    formData.append("file", item.file);
    this.isUploading = true;

    return this.http.post("/api/bookshelf/book", formData).subscribe({
      next: (response: any)=>{
        item.file.bookId = response.data.id;
        item.onSuccess(item.file);
      }, 
      error: (response)=>{
        item.onError(response, item.file);
      }
    });
  }
}
