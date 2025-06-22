import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {TransferFileService} from '../../../shared/service/transfer-file.service';
import {Course} from '../../../shared/model/Course';
import {BsDatepickerModule} from 'ngx-bootstrap/datepicker';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {ResponseData} from '../../../shared/model/response-data.model';
import {DateService} from '../../../shared/service/date.service';
import {BsModalRef} from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-detail',
  imports: [
    BsDatepickerModule,
    FormsModule
  ],
  templateUrl: './detail.component.html',
  standalone: true,
  styleUrls: ['./detail.component.scss'],
  providers: [TransferFileService],
})
export class DetailComponent implements OnInit {
  course: Course = new Course();
  today = new Date();
  startDate = new Date();
  endDate = new Date();
  isUpdate = false;
  @Output() close = new EventEmitter<boolean>();

  constructor(
    protected fileService: TransferFileService,
    protected http: HttpClient,
    protected toast: ToastrService,
    protected dateService: DateService,
    protected bsRef: BsModalRef
  ) {
  }

  ngOnInit(): void {
    this.getData();
  }

  getData() {

  }

  submit() {
    const param = {
      courseId: this.course.courseId,
      name: this.course.name,
      description: this.course.description,
      levelId: this.course.level.levelId,
      startDate: this.dateService.getFormatDate(this.startDate, 'yyyy-MM-dd'),
      endDate: this.dateService.getFormatDate(this.endDate, 'yyyy-MM-dd'),
    };

    if (!param.name) {
      this.toast.error('Course name is required');
      return;
    }
    if (this.startDate > this.endDate) {
      this.toast.error('Start date must be before end date');
      return;
    }

    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(param)], {type: 'application/json'}));
    if (this.fileService.fileData) {
      formData.append('thumbnail', this.fileService.fileData);
    }

    this.http.post<ResponseData<any>>('api/course', formData)
      .subscribe({
        next: res => {
          if (res.success) {
            this.toast.success('Success');
            if (this.isUpdate) {
              this.close.emit(true);
              this.fileService.reset();
              this.bsRef.hide();
            } else {
              this.course = new Course();
              this.startDate = new Date();
              this.endDate = new Date();
              this.clearUpload();
            }
          } else {
            this.toast.error(res.message);
          }
        },
        error: err => {
          this.toast.error(JSON.stringify(err));
        }
      });

  }

  clearUpload(): void {
    this.fileService.reset();
  }
}
