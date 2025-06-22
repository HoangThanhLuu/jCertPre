import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {QuizPayload} from '../../../shared/model/QuizPayload';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {ResponseData} from '../../../shared/model/response-data.model';
import {BsModalRef} from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-quiz-update',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './quiz-update.component.html',
  standalone: true,
  styleUrl: './quiz-update.component.scss'
})
export class QuizUpdateComponent implements OnInit {
  payload: QuizPayload = new QuizPayload();
  @Output() close = new EventEmitter<boolean>();

  constructor(protected http: HttpClient, protected toast: ToastrService, protected bsRef: BsModalRef) {
  }

  ngOnInit(): void {
  }

  submit() {
    this.http.patch<ResponseData<string>>('api/quizz', {
      quizId: this.payload.quizId,
      title: this.payload.title
    }).subscribe(res => {
      if (res.success) {
        this.toast.success('Update quiz successfully');
        this.close.emit(true);
        this.bsRef.hide();
      } else {
        this.toast.error(res.message);
      }
    });
  }
}
