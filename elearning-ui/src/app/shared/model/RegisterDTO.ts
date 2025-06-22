export class RegisterDTO {
  constructor(
    public email: string = '',
    public password: string = '',
    public rePassword: string = '',
    public firstName: string = '',
    public lastName: string = '',
  ) {
  }
}
