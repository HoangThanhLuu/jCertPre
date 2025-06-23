import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {Quiz} from '../../../shared/model/Quiz';
import {QuestionDTO} from '../../../shared/model/QuestionDTO';
import {ResponseData} from '../../../shared/model/response-data.model';
import {LessonQuizDTO} from '../../../shared/model/LessonQuizDTO';
import {QuizDTO} from '../../../shared/model/QuizDTO';
import {BsModalService} from 'ngx-bootstrap/modal';
import {QuizUpdateComponent} from '../quiz-update/quiz-update.component';
import {QuizPayload} from '../../../shared/model/QuizPayload';
import {ConfirmModalComponent} from '../../../common/confirm-modal/confirm-modal.component';
import {QuestionUpdateComponent} from '../question-update/question-update.component';

@Component({
  selector: 'app-course-quizz',
  imports: [],
  templateUrl: './course-quizz.component.html',
  standalone: true,
  styleUrl: './course-quizz.component.scss'
})
export class CourseQuizzComponent implements OnInit, OnChanges {
  data: LessonQuizDTO[] = [];
  @Input() courseId: number = 0;
  @Input() activeTab: string = '';

  constructor(
    protected http: HttpClient,
    protected toast: ToastrService,
    protected bsModal: BsModalService
  ) {
  }

  ngOnInit(): void {
    this.getData();
  }

  getData() {
    this.http.get<ResponseData<LessonQuizDTO[]>>(`api/quizz/course/${this.courseId}`)
      .subscribe(res => {
        if (res.success) {
          this.data = res.data;
        } else {
          this.toast.error(res.message);
        }
      });
  }

  getListQuestion(item: QuizDTO) {
    item.expanded = !item.expanded;
    if (item.expanded) {
      this.http.get<ResponseData<QuestionDTO[]>>(`api/quizz/${item.quizId}/questions`)
        .subscribe(res => {
          if (res.success) {
            item.questions = res.data;
          } else {
            this.toast.error(res.message);
          }
        });
    }
  }


  updateQuiz(item: QuizDTO) {
    this.bsModal.show(QuizUpdateComponent, {
      class: 'modal-dialog-centered modal-lg',
      initialState: {
        payload: new QuizPayload(item.quizId, item.title),
      }
    }).content?.close.subscribe(res => {
      this.getData();
    })
  }

  delete(item: QuizDTO) {
    this.bsModal.show(ConfirmModalComponent, {
      class: 'modal-dialog-centered',
      initialState: {
        title: 'Delete Quiz',
        message: 'Are you sure you want to delete this quiz?'
      }
    }).content?.eventOut.subscribe((result: boolean) => {
      if (result) {
        this.http.delete<ResponseData<Quiz>>(`api/quizz/${item.quizId}`)
          .subscribe(res => {
            if (res.success) {
              this.toast.success('Delete success');
              this.getData();
            } else {
              this.toast.error(res.message);
            }
          });
      }
    });
  }

  updateQuestion(item: QuestionDTO, q: QuizDTO) {
    this.bsModal.show(QuestionUpdateComponent, {
      class: 'modal-dialog-centered modal-lg',
      initialState: {
        q: item,
      }
    }).content?.close.subscribe(res => {
      if (res) {
        this.getListQuestion(q);
        q.expanded = true;
      }
    });
  }

  deleteQuestion(item: QuestionDTO, q: QuizDTO) {
    console.log(item.questionId);
    this.bsModal.show(ConfirmModalComponent, {
      class: 'modal-dialog-centered',
      initialState: {
        title: 'Delete Question',
        message: 'Are you sure you want to delete this question?'
      }
    }).content?.eventOut.subscribe((result: boolean) => {
      if (result) {
        this.http.delete<ResponseData<string>>(`api/questions/${item.questionId}`)
          .subscribe(res => {
            if (res.success) {
              this.toast.success('Delete success');
              this.getListQuestion(q);
              q.expanded = true;
            } else {
              this.toast.error(res.message);
            }
          });
      }
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['activeTab'] && this.activeTab === 'quiz') {
      this.getData();
    }
  }
}
