import {Component} from '@angular/core';
import {ShareService} from '../../shared/service/share.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-bread-crumb',
  imports: [],
  templateUrl: './bread-crumb.component.html',
  standalone: true,
  styleUrl: './bread-crumb.component.scss'
})
export class BreadCrumbComponent {
  constructor(protected shareService: ShareService, protected router: Router) {
  }

  direct(url?: string): void {
    this.router.navigate([url]).then();
  }
}
