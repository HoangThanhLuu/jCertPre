import {QuestionDTO} from './QuestionDTO';

export class QuizDetailDTO {
  constructor(
    public quizId: number = 0,
    public title: string = '',
    public questions: QuestionDTO[] = [],
  ) {
  }
}
