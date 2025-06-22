import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {SyllabusUpdatePayload} from '../../../shared/model/SyllabusUpdatePayload';
import {NgClass} from '@angular/common';
import {ResponseData} from '../../../shared/model/response-data.model';
import {BsModalRef} from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-syllabus-update',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgClass
  ],
  templateUrl: './syllabus-update.component.html',
  standalone: true,
  styleUrl: './syllabus-update.component.scss'
})
export class SyllabusUpdateComponent implements OnInit {
  payload: SyllabusUpdatePayload = new SyllabusUpdatePayload();
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
    this.http.patch<ResponseData<string>>('api/syllabus', this.payload)
      .subscribe(res => {
        if (res.success) {
          this.toast.success('Update syllabus successfully');
          this.close.emit(true);
          this.bsRef.hide();
        } else {
          this.toast.error(res.message);
        }
      });
  }
}

