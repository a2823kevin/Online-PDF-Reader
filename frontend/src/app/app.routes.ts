import { Routes } from '@angular/router';
import { BookshelfPageComponent } from './pages/bookshelf-page/bookshelf-page.component';
import { ReaderPageComponent } from './pages/reader-page/reader-page.component';
import { DashboardPageComponent } from './pages/dashboard-page/dashboard-page.component';
import { adminAuthGuard } from './guards/admin-auth.guard';

export const routes: Routes = [
    {
      path: "",
      redirectTo: "/bookshelf",
      pathMatch: "full"
    },
    {
      path: "bookshelf",
      title: "Bookshelf - PDF Reader", 
      component: BookshelfPageComponent,
    },
    {
      path: "dashboard", 
      title: "Dashboard - PDF Reader", 
      canActivate: [adminAuthGuard], 
      component: DashboardPageComponent,
    },
    {
        path: "reader", 
        children:[{
            path: ":id", 
            component: ReaderPageComponent
        }]
    }
];