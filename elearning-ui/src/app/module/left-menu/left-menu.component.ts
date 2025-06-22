import {Component, OnInit} from '@angular/core';
import {ChildMenuDTO, MenuDTO} from '../../shared/model/MenuDTO';
import {Router} from '@angular/router';
import {NgClass} from '@angular/common';
import {ShareService} from '../../shared/service/share.service';

@Component({
  selector: 'app-left-menu',
  imports: [
    NgClass
  ],
  templateUrl: './left-menu.component.html',
  standalone: true,
  styleUrl: './left-menu.component.scss'
})
export class LeftMenuComponent implements OnInit {
  menus: MenuDTO[] = [];

  constructor(protected router: Router, protected shareService: ShareService) {
    this.menus = [
      new MenuDTO('Courses', '/courses', false, false, 'fa fa-book', [
        new ChildMenuDTO('List Courses', '/courses/list'),
        new ChildMenuDTO('Manage Course', '/courses/upsert'),
      ]),
      new MenuDTO('Enrollments', '/enrollments', false, false, 'fa fa-graduation-cap')
    ]
  }

  ngOnInit(): void {
    this.menus.forEach(e => {
      const isActive = this.router.url.includes(e.url);
      e.active = isActive;
      e.children.forEach(child => {
        child.active = this.router.url.includes(child.url);
      });
      e.expanded = isActive;
      if (isActive) {
        this.shareService.setBreadcrumb(e.name, e.url);
      }
    });
  }

  openMenu(menu: MenuDTO): void {
    this.menus.forEach(e => {
      e.active = false;
    });
    menu.active = true;
    this.shareService.setBreadcrumb(menu.name, menu.url);
    if (menu.children.length > 0) {
      menu.expanded = !menu.expanded;
    } else {
      this.router.navigate([menu.url]).then();
    }
  }

  openChildMenu(child: ChildMenuDTO): void {
    this.shareService.setBreadcrumb(child.name, child.url);
    this.router.navigate([child.url]).then(_ => {
      this.menus.forEach(e => {
        e.active = this.router.url.includes(e.url);
      });
    });
  }
}
