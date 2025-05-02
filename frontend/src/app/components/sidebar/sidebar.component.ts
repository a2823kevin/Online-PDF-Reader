import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFlexModule } from 'ng-zorro-antd/flex';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { UserContextService } from '../../services/user-context.service';
import { RegisterModalComponent } from "../modals/register-modal/register-modal.component";
import { LoginModalComponent } from '../modals/login-modal/login-modal.component';
import { LogoutModalComponent } from '../modals/logout-modal/logout-modal.component';
import { Role } from '../../models/Role';

@Component({
  selector: 'app-sidebar',
  imports: [
    RouterLink, NzFlexModule, NzButtonModule, NzIconModule, NzMenuModule, 
    RegisterModalComponent, LoginModalComponent, LogoutModalComponent
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss'
})
export class SidebarComponent {
  role = Role;
  @Input()
  page!: string;

  isLoginModalVisible: boolean = false;
  isRegisterModalVisible: boolean = false;
  isLogoutModalVisible: boolean = false;

  constructor(public userContextService: UserContextService) {}

  openLogin() {
    this.isLoginModalVisible = true;
  }
  openRegister() {
    this.isRegisterModalVisible = true;
  }
  openLogout() {
    this.isLogoutModalVisible = true;
  }
}
