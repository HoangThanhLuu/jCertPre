import {Component, OnInit} from '@angular/core';
import {Enrollments} from '../../shared/model/Enrollments';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {PagingData, ResponseData} from '../../shared/model/response-data.model';
import {PaginationComponent} from 'ngx-bootstrap/pagination';
import {FormsModule} from '@angular/forms';
import {EnrollmentDTO} from '../../shared/model/EnrollmentDTO';
import {debounceTime, Subject} from 'rxjs';

@Component({
  selector: 'app-enrollments',
  imports: [
    PaginationComponent,
    FormsModule
  ],
  templateUrl: './timetable.component.html',
  standalone: true,
  styleUrl: './timetable.component.scss'
})
export class TimetableComponent implements OnInit {
  data: PagingData<EnrollmentDTO> = new PagingData<EnrollmentDTO>();
  key: string = '';
  searchSubject = new Subject<string>();

  constructor(
    protected http: HttpClient,
    protected toast: ToastrService
  ) {
  }

  ngOnInit(): void {
    this.getData();
    this.searchSubject.pipe(
      debounceTime(500)
    ).subscribe(() => this.getData());
  }

  getData() {
    this.http.get<ResponseData<PagingData<EnrollmentDTO>>>(`api/enrollment?page${this.data.page}&size=${this.data.size}&key=${this.key}`)
      .subscribe(res => {
        if (res.success) {
          this.data = res.data;
        } else {
          this.toast.error(res.message);
        }
      });
  }

  pageChanged(event: any): void {
    this.data.page = event.page;
    this.getData();
  }

  search() {
    this.searchSubject.next(this.key);
  }
}
