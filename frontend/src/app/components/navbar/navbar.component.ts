import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NzFlexModule } from 'ng-zorro-antd/flex';
import { NzMenuModule } from 'ng-zorro-antd/menu';
import { LogoComponent } from '../logo/logo.component';
import { NzIconModule } from 'ng-zorro-antd/icon';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzInputNumberModule } from 'ng-zorro-antd/input-number';
import { NzSliderModule } from 'ng-zorro-antd/slider';
import { RouterLink } from '@angular/router';
import { UserContextService } from '../../services/user-context.service';
import { RegisterModalComponent } from "../modals/register-modal/register-modal.component";
import { LoginModalComponent } from '../modals/login-modal/login-modal.component';
import { LogoutModalComponent } from '../modals/logout-modal/logout-modal.component';
import { NavbarctlContextService } from '../../services/navbarctl-context.service';
import { FormsModule } from '@angular/forms';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { Role } from '../../models/Role';

@Component({
  selector: 'app-navbar',
  imports: [
    RouterLink, FormsModule, NzMenuModule, NzFlexModule, NzGridModule, NzIconModule, NzInputModule, NzInputNumberModule, NzSliderModule, 
    LogoComponent, RegisterModalComponent, LoginModalComponent, LogoutModalComponent
],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  role = Role;

  @Input()
  page!: string;
  userName: string | undefined;
  bookQuery: string = "";

  @Input()
  sliderMaxValue: number | undefined;

  @Input()
  sliderValue!: number;
  @Output()
  sliderValueChange: EventEmitter<number> = new EventEmitter();

  isLoginModalVisible: boolean = false;
  isRegisterModalVisible: boolean = false;
  isLogoutModalVisible: boolean = false;

  constructor(public userContextService: UserContextService, public navbarctlContextService: NavbarctlContextService) {}

  handleBookQuery(value: string) {
    this.navbarctlContextService.setBookNameContainString(value);
  }

  handlePageChange(value: number) {
    this.sliderValue = value;
    this.sliderValueChange.emit(value);
  }

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

