export class LessonDTO {
  constructor(
    public lessonId: number = -1,
    public courseId: number = -1,
    public title: string = '',
    public description: string = '',
    public sequence: number = 0,
    public expanded: boolean = false,
    public media: MediaLesson[] = []
  ) {
  }
}

export class MediaLesson {
  constructor(
    public mediaId: number = 0,
    public lessonId: number = 0,
    public title: string = '',
    public url: string = '',
    public urlBlob: any = null,
    public type: string = '',
  ) {
  }
}
