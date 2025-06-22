import {Injectable} from '@angular/core';
import {RootScope} from '../model/RootScope';

@Injectable({
  providedIn: 'root'
})
export class ShareService {
  rootScope: RootScope = {};

  constructor() {
  }

  setBreadcrumb(breadCrumb: string, breadCrumbUrl: string) {
    this.rootScope.breadCrumb = breadCrumb;
    this.rootScope.breadCrumbUrl = breadCrumbUrl;
  }

  get getRootScope(): RootScope {
    return this.rootScope;
  }
}
