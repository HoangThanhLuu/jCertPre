import {Component, OnInit} from '@angular/core';
import {ShareService} from '../../shared/service/share.service';
import {EnrollmentsComponent} from '../enrollments/enrollments.component';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-home-admin',
  imports: [
    EnrollmentsComponent,
  ],
  templateUrl: './home.component.html',
  standalone: true,
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
  data: DashboardDTO = {};

  constructor(
    protected shareService: ShareService,
    protected http: HttpClient,
    protected toast: ToastrService
  ) {
  }

  ngOnInit(): void {

  }
}

export interface DashboardDTO {
  totalCourses?: number;
  totalQuiz?: number;
  totalStudent?: number;
  totalLesson?: number;
}
