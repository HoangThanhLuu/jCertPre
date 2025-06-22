export class Enrollments {
  constructor(
    public enrollmentId: number = -1,
    public userId: number = -1,
    public email: string = '',
    public fullName: string = '',
    public courseId: number = -1,
    public courseName: string = '',
    public status: string = '',
    public createdAt: string = '',
  ) {
  }
}
