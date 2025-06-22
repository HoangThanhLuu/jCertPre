export class SyllabusUpdatePayload {
  constructor(
    public lessonId: number = 0,
    public title: string = '',
    public description: string = '',
  ) {
  }
}
