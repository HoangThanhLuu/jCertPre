import {Component, OnInit, signal} from '@angular/core';
import {PaginationComponent} from 'ngx-bootstrap/pagination';
import {PagingData, ResponseData} from '../../shared/model/response-data.model';
import {Course} from '../../shared/model/Course';
import {FormsModule} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {debounceTime, Subject} from 'rxjs';
import {DatePipe} from '@angular/common';
import {BsModalService} from 'ngx-bootstrap/modal';
import {DetailComponent} from './detail/detail.component';
import {ConfirmModalComponent} from '../../common/confirm-modal/confirm-modal.component';
import {Router} from '@angular/router';

@Component({
  selector: 'app-course',
  imports: [
    PaginationComponent,
    FormsModule,
    DatePipe
  ],
  templateUrl: './course.component.html',
  standalone: true,
  styleUrl: './course.component.scss'
})
export class CourseComponent implements OnInit {
  courses: PagingData<Course> = new PagingData<Course>();
  key = signal('');
  searchSubject = new Subject<string>();

  constructor(
    protected http: HttpClient,
    protected toast: ToastrService,
    protected bsModal: BsModalService,
    protected router: Router
  ) {
  }

  ngOnInit(): void {
    this.getData();
    this.searchSubject.pipe(
      debounceTime(500)
    ).subscribe(() => this.getData());
  }

  getData() {
    this.http.get<ResponseData<PagingData<Course>>>(`api/course?page=${this.courses.page}&size=${this.courses.size}&key=${this.key()}`)
      .subscribe({
        next: res => {
          if (res.success) {
            this.courses = res.data;
          } else {
            this.toast.error(res.message);
          }
        },
        error: err => {
          this.toast.error(err.message);
        }
      })
  }

  search(): void {
    this.searchSubject.next(this.key());
  }

  pageChanged(event: any): void {
    this.courses.page = event.page;
    this.getData();
  }

  update(item: Course) {
    this.bsModal.show(DetailComponent, {
      class: 'modal-lg modal-dialog-centered',
      initialState: {
        course: item,
        isUpdate: true
      }
    }).content?.close.subscribe(res => {
      if (res) {
        this.getData();
      }
    });
  }

  delete(item: Course) {
    this.bsModal.show(ConfirmModalComponent, {
      class: 'modal-dialog-centered',
      initialState: {
        title: 'Delete Course',
        message: 'Do you want to delete this course?'
      }
    }).content?.eventOut.subscribe(res => {
      if (res) {
        this.http.delete<ResponseData<any>>(`api/course/${item.courseId}`)
          .subscribe(data => {
            if (data.success) {
              this.toast.success('Delete success');
              this.getData();
            } else {
              this.toast.error(data.message);
            }
          });
      }
    });
  }

  view(item: Course) {
    this.router.navigate(['/courses/detail'], {
      queryParams: {
        cid: item.courseId
      }
    }).then();
  }

  register(item: Course): void {
    this.http.get <ResponseData<string>>(`api/course/register/${item.courseId}`)
      .subscribe(res => {
        if (res.success) {
          this.toast.success('Register success');
          this.getData()
        } else {
          this.toast.error('Register fail')
        }
      })
  }
}
