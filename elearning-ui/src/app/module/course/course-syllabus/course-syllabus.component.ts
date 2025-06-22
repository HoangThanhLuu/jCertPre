import {Component, Input, OnInit} from '@angular/core';
import {LessonDTO} from '../../../shared/model/LessonDTO';
import {HttpClient} from '@angular/common/http';
import {PaginationComponent} from 'ngx-bootstrap/pagination';
import {ResponseData} from '../../../shared/model/response-data.model';
import {ToastrService} from 'ngx-toastr';
import {BsModalService} from 'ngx-bootstrap/modal';
import {ConfirmModalComponent} from '../../../common/confirm-modal/confirm-modal.component';
import {Router} from '@angular/router';
import {SyllabusUpdatePayload} from '../../../shared/model/SyllabusUpdatePayload';
import {SyllabusUpdateComponent} from '../syllabus-update/syllabus-update.component';
import {TransferFileService} from '../../../shared/service/transfer-file.service';

@Component({
  selector: 'app-course-syllabus',
  imports: [
    PaginationComponent
  ],
  templateUrl: './course-syllabus.component.html',
  standalone: true,
  styleUrl: './course-syllabus.component.scss',
  providers: [TransferFileService]
})
export class CourseSyllabusComponent implements OnInit {
  lessons: LessonDTO[] = [];
  @Input() courseId = 0;

  constructor(
    protected http: HttpClient,
    protected toast: ToastrService,
    protected bsModal: BsModalService,
    protected router: Router,
    protected fileService: TransferFileService
  ) {
  }

  ngOnInit(): void {
    this.getData();
  }

  click(courseId: number) {
    this.router.navigate(['/course/syllabus', courseId]);
  }

  getData() {
    this.http.get<ResponseData<LessonDTO[]>>(`api/syllabus/course/${this.courseId}`)
      .subscribe(res => {
        if (res.success) {
          this.lessons = res.data;
        } else {
          this.toast.error(res.message);
        }
      });
  }

  deleteLesson(lessonId: number) {
    this.bsModal.show(ConfirmModalComponent, {
      class: 'modal-dialog-centered',
      initialState: {
        title: 'Delete Lesson',
        message: 'Are you sure you want to delete this lesson?'
      }
    }).content?.eventOut.subscribe((result: boolean) => {
      if (result) {
        this.http.delete<ResponseData<LessonDTO>>(`api/syllabus/${lessonId}`)
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

  deleteMedia(mediaId: number) {
    this.bsModal.show(ConfirmModalComponent, {
      class: 'modal-dialog-centered',
      initialState: {
        title: 'Delete Media',
        message: 'Are you sure you want to delete this media?'
      }
    }).content?.eventOut.subscribe((result: boolean) => {
      if (result) {
        this.http.delete<ResponseData<LessonDTO>>(`api/syllabus/media/${mediaId}`)
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

  updateLesson(item: LessonDTO) {
    this.bsModal.show(SyllabusUpdateComponent, {
      class: 'modal-lg modal-dialog-centered draggable',
      initialState: {
        payload: new SyllabusUpdatePayload(item.lessonId, item.title, item.description)
      }
    }).content?.close.subscribe(res => {
      if (res) {
        this.getData();
      }
    });
  }

  uploadMedia(event: any, item: LessonDTO) {
    this.fileService.processFileObs(event)
      .subscribe(file => {
        const formData = new FormData();
        formData.append('file', file.file);
        formData.append('lessonId', `${item.lessonId}`);
        this.http.post<ResponseData<string>>('api/syllabus/media', formData)
          .subscribe(res => {
            if (res.success) {
              this.toast.success('Upload success');
              this.getData();
            } else {
              this.toast.error(res.message);
            }
          });
      });
  }
}
