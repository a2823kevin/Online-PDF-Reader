import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzModalModule, NzModalService } from 'ng-zorro-antd/modal';
import { NzFormModule } from 'ng-zorro-antd/form';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register-modal',
  imports: [ReactiveFormsModule, NzIconModule, NzButtonModule, NzModalModule, NzFormModule, NzInputModule],
  templateUrl: './register-modal.component.html',
  styleUrl: './register-modal.component.scss'
})

export class RegisterModalComponent {
  @Input() isVisible: boolean = false;
  @Output() isVisibleChange: EventEmitter<boolean> = new EventEmitter();
  isLoading: boolean = false;

  formGroup: FormGroup = new FormGroup({
    username: new FormControl("", [Validators.required, Validators.minLength(8), Validators.maxLength(32)]), 
    password: new FormControl("", [Validators.required])
  });

  constructor(public http: HttpClient, private modalService: NzModalService) {}

  close() {
    this.isVisible = false;
    this.isLoading = false;
    this.formGroup.controls["username"].reset("");
    this.formGroup.controls["password"].reset("");
    this.isVisibleChange.emit(this.isVisible);
  }

  register() {
    this.isLoading = true;
    this.http.post(
      "/api/auth/register", 
      {
        username: this.formGroup.controls["username"].value, 
        password: this.formGroup.controls["password"].value
      }
    ).subscribe({
      next: (response: any)=>{
        console.log(response);
        this.close();
        this.modalService.success({
          nzTitle: 'Registration succeed',
          nzContent: response.message, 
          nzOkText: "OK"
        });
        localStorage.setItem("opr_token", response.data);
        setTimeout(() => {
          window.location.reload();
        }, 1500);
      }, 
      error: (response: any)=>{
        const modal = this.modalService.error({
          nzTitle: 'Registration failed',
          nzContent: response.error.message, 
          nzOkText: "OK"
        });
        this.isLoading = false;
        setTimeout(() => modal.destroy(), 1500);
      }
    })
  }

  onEnter() {
    this.register();
  }
}
