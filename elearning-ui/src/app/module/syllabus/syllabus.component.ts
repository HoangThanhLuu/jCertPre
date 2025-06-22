import {Component, OnInit} from '@angular/core';
import {CourseSyllabusComponent} from '../course/course-syllabus/course-syllabus.component';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-syllabus',
  imports: [
    CourseSyllabusComponent
  ],
  templateUrl: './syllabus.component.html',
  standalone: true,
  styleUrl: './syllabus.component.scss'
})
export class SyllabusComponent implements OnInit {
  courseId = 0;

  constructor(protected route: ActivatedRoute) {
    this.route.queryParams.subscribe(param => {
      this.courseId = param['cid'];
    });
  }

  ngOnInit(): void {
  }
}
