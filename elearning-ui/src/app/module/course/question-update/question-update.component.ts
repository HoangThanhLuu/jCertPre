import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {ToastrService} from 'ngx-toastr';
import {HttpClient} from '@angular/common/http';
import {QuestionDTO} from '../../../shared/model/QuestionDTO';
import {ResponseData} from '../../../shared/model/response-data.model';

@Component({
  selector: 'app-question-update',
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './question-update.component.html',
  standalone: true,
  styleUrl: './question-update.component.scss'
})
export class QuestionUpdateComponent implements OnInit {
  q: QuestionDTO = new QuestionDTO();
  @Output() close = new EventEmitter<boolean>();

  constructor(
    protected http: HttpClient,
    protected toast: ToastrService,
    protected bsRef: BsModalRef
  ) {
  }

  ngOnInit(): void {
  }

  submit() {
    this.http.patch<ResponseData<string>>('api/questions', this.q)
      .subscribe(res => {
        if (res.success) {
          this.toast.success('Update question success');
          this.close.emit(true);
          this.bsRef.hide();
        } else {
          this.toast.error(res.message);
        }
      });
  }
}
