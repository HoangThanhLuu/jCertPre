export class QuestionDTO {
  constructor(
    public no: number = 0,
    public questionId: number = 0,
    public quizId: number = 0,
    public content: string = '',
    public optionA: string = '',
    public optionB: string = '',
    public optionC: string = '',
    public optionD: string = '',
    public answer: string = '',
    public userAnswer: string = '',
  ) {
  }
}
