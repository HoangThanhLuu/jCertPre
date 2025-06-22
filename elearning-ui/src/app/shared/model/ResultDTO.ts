export class ResultDTO {
  constructor(
    public resultId: number = 0,
    public quizId: number = 0,
    public quizTitle: string = '',
    public scoreStr: string = '',
    public status: string = '',
    public startTime: string = '',
    public endTime: string = '',
  ) {
  }
}
