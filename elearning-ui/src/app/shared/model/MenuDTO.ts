export class MenuDTO {
  constructor(
    public name: string = '',
    public url: string = '',
    public active: boolean = false,
    public expanded: boolean = false,
    public icon: string = '',
    public children: ChildMenuDTO[] = []
  ) {
  }
}

export class ChildMenuDTO {
  constructor(
    public name: string = '',
    public url: string = '',
    public active: boolean = false
  ) {
  }
}
