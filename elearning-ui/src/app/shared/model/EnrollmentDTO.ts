export class EnrollmentDTO {
  constructor(
    public timeTableId: number = -1,
    public courseId: number = -1,
    public userId: number = -1,
    public time: string = '',
    public status: string = '',
    public email: string = '',
    public courseName: string = '',
    public thumbnail: string = '',
    public createdDate: string = '',
    public updatedDate: string = '',
  ) {
  }
}
