export class LessonPayload {
  constructor(
    public lessonId: number = 0,
    public courseId: number = 0,
    public title: string = '',
    public description: string = '',
  ) {
  }
}
