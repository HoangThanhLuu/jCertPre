export class StudentDTO {
  constructor(
    public userId: number = -1,
    public email: string = '',
    public firstName: string = '',
    public lastName: string = '',
    public gender: string = '',
    public phone: string = '',
    public address: string = '',
    public dob: string = '',
    public avatar: string = '',
    public status: string = '',
  ) {
  }
}
