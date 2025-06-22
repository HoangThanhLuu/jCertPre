import {Component} from '@angular/core';
import {LoginDTO} from '../../shared/model/LoginDTO';
import {HttpClient} from '@angular/common/http';
import {ToastrService} from 'ngx-toastr';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {AuthenticationService} from '../../shared/service/authentication.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login',
  imports: [
    FormsModule
  ],
  templateUrl: './login.component.html',
  standalone: true,
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  param: LoginDTO = new LoginDTO();

  constructor(
    protected http: HttpClient
    , protected toast: ToastrService
    , protected bsRef: BsModalRef
    , protected bsModal: BsModalService
    , protected authService: AuthenticationService
  ) {
  }

  submit() {
    if (!this.param.email || !this.param.password) {
      this.toast.warning('Please enter email and password');
      return;
    }

    this.authService.loginV3(this.param)
      .subscribe(res => {
        if (res.success) {
          this.toast.success('Login successfully');
          this.authService.redirectHome();
        } else {
          this.toast.error(res.message);
        }
      });
  }
}
