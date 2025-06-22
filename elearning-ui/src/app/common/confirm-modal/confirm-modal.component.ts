import {Component, EventEmitter, Output, signal} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-confirm-modal',
  imports: [],
  templateUrl: './confirm-modal.component.html',
  standalone: true,
  styleUrl: './confirm-modal.component.scss'
})
export class ConfirmModalComponent {
  title = '';
  message = '';
  @Output() eventOut = new EventEmitter<boolean>();

  constructor(protected bsRef: BsModalRef) {
  }

  confirm() {
    this.eventOut.emit(true);
    this.bsRef.hide();
  }

  cancel() {
    this.eventOut.emit(false);
    this.bsRef.hide();
  }
}
