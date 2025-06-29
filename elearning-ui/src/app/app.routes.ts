import {Routes} from '@angular/router';
import {LayoutComponent} from './module/layout/layout.component';
import {AuthGuard} from './shared/guards/auth.guard';
import {profileResolver} from './shared/service/user.service';
import {LoginComponent} from './module/login/login.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    resolve: {
      profile: profileResolver
    },
    children: [
      {
        path: '',
        redirectTo: 'courses',
        pathMatch: 'full'
      },
      {
        path: 'courses',
        children: [
          {
            path: '',
            redirectTo: 'list',
            pathMatch: 'full'
          },
          {
            path: 'list',
            loadComponent: () => import('./module/course/course.component')
              .then(m => m.CourseComponent)
          },
          {
            path: 'upsert',
            loadComponent: () => import('./module/course/course-detail/course-detail.component')
              .then(m => m.CourseDetailComponent)
          },
          {
            path: 'detail',
            loadComponent: () => import('./module/detail-course/detail-course.component')
              .then(m => m.DetailCourseComponent)
          }
        ]
      },
      {
        path: 'enrollments',
        children: [
          {
            path: '',
            redirectTo: 'list',
            pathMatch: 'full'
          },
          {
            path: 'list',
            loadComponent: () => import('./module/time-table/timetable.component')
              .then(m => m.TimetableComponent)
          },
          {
            path: 'mana',
            loadComponent: () => import('./module/time-table-mana/time-table-mana.component')
              .then(m => m.TimeTableManaComponent)
          }
        ]
      }
    ]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];
