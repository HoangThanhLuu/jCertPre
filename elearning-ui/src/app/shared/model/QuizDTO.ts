import {QuestionDTO} from './QuestionDTO';

export class QuizDTO {
  constructor(
    public quizId: number = -1,
    public lessonId: number = -1,
    public title: string = '',
    public description: string = '',
    public numberOfQuestion: number = 0,
    public questions: QuestionDTO[] = [],
    public expanded: boolean = false
  ) {
  }
}

