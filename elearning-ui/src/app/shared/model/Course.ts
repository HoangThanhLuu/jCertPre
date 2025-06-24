import {LessonDTO} from './LessonDTO';

export class Course {
  constructor(
    public courseId: number = -1,
    public name: string = '',
    public description: string = '',
    public level: CourseLevel = new CourseLevel(),
    public thumbnail: string = '',
    public startDate: string = '',
    public endDate: string = '',
    public numberOfStudents: number = 0,
    public status: string = '',
    public numberLesson: number = 0,
    public isJoined: number = 0,
    public register: boolean = false,
    public lessons: LessonDTO[] = []
  ) {
  }
}

export class CourseLevel {
  constructor(
    public levelId: string = '',
    public name: string = '',
  ) {
  }
}
