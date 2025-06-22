export class EnrollCourseDTO {
  constructor(
    public courseName: string = '',
    public thumbnail: string = '',
    public startDate: string = '',
    public endDate: string = '',
    public enrollmentDate: string = '',
    public enrollmentStatus: string = '',
    public courseStatus: string = '',
  ) {
  }
}
