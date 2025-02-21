import {Injectable} from "@angular/core";
import {CanActivate, Router} from "@angular/router";
import { SessionService } from "../services/session.service";

@Injectable({providedIn: 'root'})
export class UnauthGuard implements CanActivate {

  constructor(
    private readonly router: Router,
    private readonly sessionService: SessionService,
  ) {
  }

  public canActivate(): boolean {
    if (this.sessionService.isLogged) {
      this.router.navigate(['sessions']);
      return false;
    }
    return true;
  }
}
