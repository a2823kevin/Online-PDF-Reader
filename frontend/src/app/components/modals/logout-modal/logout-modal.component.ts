import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzModalModule, NzModalService } from 'ng-zorro-antd/modal';
import { HttpClient } from '@angular/common/http';
import { UserContextService } from '../../../services/user-context.service';

@Component({
  selector: 'app-logout-modal',
  imports: [NzButtonModule, NzModalModule],
  templateUrl: './logout-modal.component.html',
  styleUrl: './logout-modal.component.scss'
})

export class LogoutModalComponent {
  @Input() isVisible: boolean = false;
  @Output() isVisibleChange: EventEmitter<boolean> = new EventEmitter();
  isLoading: boolean = false;

  constructor(public http: HttpClient, public userContextService: UserContextService, private modalService: NzModalService) {}

  logout() {
    this.isLoading = true;
    this.http.post(
      "/api/auth/logout", null).subscribe({
      next: (response: any)=>{
        this.modalService.success({
          nzTitle: 'Logout succeed',
          nzContent: response.message, 
          nzOkText: "OK"
        });
        localStorage.removeItem("opr_token");
        setTimeout(() => {
          window.location.reload();
        }, 750);
      }, 
      error: (response: any)=>{
        localStorage.removeItem("opr_token");
        setTimeout(() => {
          window.location.reload();
        }, 750);
      }
    })
  }

  close() {
    this.isVisible = false;
    this.isLoading = false;
    this.isVisibleChange.emit(this.isVisible);
  }
}
