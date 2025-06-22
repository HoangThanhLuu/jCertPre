import {Component, OnInit} from '@angular/core';
import {BsDatepickerDirective, BsDatepickerInputDirective} from 'ngx-bootstrap/datepicker';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TransferFileService} from '../../../shared/service/transfer-file.service';
import {LessonPayload} from '../../../shared/model/LessonPayload';
import {FileData} from '../../../shared/model/FileData';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {PagingData, ResponseData} from '../../../shared/model/response-data.model';
import {Course} from '../../../shared/model/Course';
import {NgSelectComponent} from '@ng-select/ng-select';

@Component({
  selector: 'app-course-syllabus-upsert',
  imports: [
    BsDatepickerDirective,
    BsDatepickerInputDirective,
    FormsModule,
    ReactiveFormsModule,
    NgSelectComponent
  ],
  templateUrl: './course-syllabus-upsert.component.html',
  standalone: true,
  styleUrls: ['./course-syllabus-upsert.component.scss'],
  providers: [TransferFileService]
})
export class CourseSyllabusUpsertComponent implements OnInit {
  payload: LessonPayload = new LessonPayload();
  courses: Course[] = [];

  constructor(
    protected fileService: TransferFileService,
    protected http: HttpClient,
    protected toast: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.http.get<ResponseData<PagingData<Course>>>('api/course?size=1000000')
      .subscribe(res => {
        if (res.success) {
          this.courses = res.data.contents;
        }
      });
  }

  clearUpload(): void {
    this.fileService.reset();
  }

  submit() {
    if (this.payload.title === '') {
      this.toast.error('Title is required');
      return;
    }
    const formData = new FormData();
    formData.append('data', new Blob([JSON.stringify(this.payload)], {type: 'application/json'}));
    if (this.fileService.files) {
      this.fileService.files.forEach((file: FileData) => {
        if (file.size > 20) {
          this.toast.error(`File ${file.name} size should not exceed 20MB`);
          return;
        }
        formData.append('files', file.file);
      });
    }

    this.http.post<ResponseData<any>>('api/syllabus', formData)
      .subscribe(res => {
        if (res.success) {
          this.toast.success('Create success');
        } else {
          this.toast.error(res.message);
        }
      });
  }
}

