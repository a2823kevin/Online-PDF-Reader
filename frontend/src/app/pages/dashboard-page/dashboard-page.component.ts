import { Component } from '@angular/core';
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { SidebarComponent } from "../../components/sidebar/sidebar.component";

@Component({
  selector: 'app-dashboard-page',
  imports: [
    NzLayoutModule,
    NavbarComponent, SidebarComponent
],
  templateUrl: './dashboard-page.component.html',
  styleUrl: './dashboard-page.component.scss'
})
export class DashboardPageComponent {

}
