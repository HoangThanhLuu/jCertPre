import {Component, OnInit} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {QuestionDTO} from '../../../shared/model/QuestionDTO';
import {QuizDTO} from '../../../shared/model/QuizDTO';
import {Course} from '../../../shared/model/Course';
import {LessonDTO} from '../../../shared/model/LessonDTO';
import {PagingData, ResponseData} from '../../../shared/model/response-data.model';
import {ToastrService} from 'ngx-toastr';
import {NgSelectComponent} from '@ng-select/ng-select';

@Component({
  selector: 'app-course-quizz-upsert',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgSelectComponent
  ],
  templateUrl: './course-quizz-upsert.component.html',
  standalone: true,
  styleUrl: './course-quizz-upsert.component.scss'
})
export class CourseQuizzUpsertComponent implements OnInit {
  param: QuizzPayload = new QuizzPayload();
  courses: Course[] = [];
  lessons: LessonDTO[] = [];
  selectedCourseId: number = 0;

  constructor(
    protected http: HttpClient,
    protected toast: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.http.get<ResponseData<PagingData<Course>>>('api/course?size=10000')
      .subscribe(res => {
        if (res.success) {
          this.courses = res.data.contents;
          this.selectedCourseId = this.courses[0]?.courseId || 0;
          this.getLesson();
        } else {
          this.toast.error(res.message);
        }
      });
  }

  getLesson() {
    this.http.get<ResponseData<LessonDTO[]>>(`api/syllabus/course/${this.selectedCourseId}`)
      .subscribe(res => {
        if (res.success) {
          this.lessons = res.data;
        } else {
          this.toast.error(res.message);
        }
      });
  }

  addQuestion() {
    this.param.questions.push(new QuestionDTO(this.param.questions.length + 1));
  }

  submit() {
    if (!this.param.quiz.title
      || !this.param.quiz.description
      || this.param.quiz.lessonId !== -1
    ) {
      this.toast.error('Please input required field');
      return;
    }

    if (this.param.questions.length === 0) {
      this.toast.error('Please add question');
      return;
    }
    let checkFail = false;
    this.param.questions.forEach(e => {
      if (!e.optionA
        || !e.optionB
        || !e.optionC
        || !e.optionD
        || !e.content
        || !e.answer
      ) {
        checkFail = true;
      }
    });
    if (checkFail) {
      this.toast.error('Please input all field of question');
      return;
    }

    this.http.post<ResponseData<string>>('api/quizz', this.param)
      .subscribe(res => {
        if (res.success) {
          this.toast.success('Success');
          this.param = new QuizzPayload();
        } else {
          this.toast.error(res.message);
        }
      });
  }

  remove(index: number) {
    this.param.questions.splice(index, 1);
  }
}

export class QuizzPayload {
  constructor(
    public quiz: QuizDTO = new QuizDTO(),
    public questions: QuestionDTO[] = []
  ) {
  }
}
