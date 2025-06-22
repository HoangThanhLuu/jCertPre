import {Component} from '@angular/core';
import {TabsModule} from 'ngx-bootstrap/tabs';
import {DetailComponent} from '../detail/detail.component';
import {CourseSyllabusComponent} from '../course-syllabus/course-syllabus.component';
import {CourseSyllabusUpsertComponent} from '../course-syllabus-upsert/course-syllabus-upsert.component';
import {CourseQuizzUpsertComponent} from '../course-quizz-upsert/course-quizz-upsert.component';

@Component({
  selector: 'app-course-detail',
  imports: [
    TabsModule,
    DetailComponent,
    CourseSyllabusComponent,
    CourseSyllabusUpsertComponent,
    CourseQuizzUpsertComponent,
  ],
  templateUrl: './course-detail.component.html',
  standalone: true,
  styleUrl: './course-detail.component.scss'
})
export class CourseDetailComponent {

}
