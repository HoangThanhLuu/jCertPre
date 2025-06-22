import {QuestionDTO} from './QuestionDTO';

export class Quiz {
  constructor(
    public quizId: number = 0,
    public lessonId: number = 0,
    public title: string = '',
    public description: string = '',
    public numberOfQuestions: number = 0,
    public expanded: boolean = false,
    public questions: QuestionDTO[] = [],
  ) {
  }
}
