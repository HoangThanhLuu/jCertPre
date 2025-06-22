import {Component} from '@angular/core';
import {NgOptimizedImage} from '@angular/common';
import {LeftMenuComponent} from '../left-menu/left-menu.component';
import {BreadCrumbComponent} from '../bread-crumb/bread-crumb.component';
import {RouterOutlet} from '@angular/router';
import {AuthenticationService} from '../../shared/service/authentication.service';
import {UserService} from '../../shared/service/user.service';

@Component({
  selector: 'app-admin-layout',
  imports: [
    NgOptimizedImage,
    LeftMenuComponent,
    RouterOutlet,
    BreadCrumbComponent
  ],
  templateUrl: './layout.component.html',
  standalone: true,
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {

  constructor(
    protected auth: AuthenticationService,
    protected profile: UserService
  ) {
  }

  logout(): void {
    this.auth.logout();
  }
}
