import {QuizDTO} from './QuizDTO';

export class LessonQuizDTO{
  constructor(
    public lessonId: number = -1,
    public lessonName: string = '',
    public expanded: boolean = false,
    public quizzes: QuizDTO[] = []
  ) {
  }
}
