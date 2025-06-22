export class FileData {
  constructor(
    public url: string = '',
    public name: string = '',
    public type: string = '',
    public size: number = 0,
    public file: any = null,
  ) {
  }
}
