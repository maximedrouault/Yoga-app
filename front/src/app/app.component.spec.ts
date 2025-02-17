import {HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {MatToolbarModule} from '@angular/material/toolbar';
import {RouterTestingModule} from '@angular/router/testing';
import {expect, it} from '@jest/globals';

import {AppComponent} from './app.component';
import {AuthService} from "./features/auth/services/auth.service";
import {SessionService} from "./services/session.service";
import {Router} from "@angular/router";
import {of} from "rxjs";


describe('AppComponent', () => {
  let mockAuthService;
  let mockSessionService: any;
  let mockRouter: any;

  beforeEach(async () => {
    mockAuthService = jest.fn();
    mockSessionService = { $isLogged: jest.fn(), logOut: jest.fn() };
    mockRouter = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });


  describe('AppComponent test suite', () => {
    it('should return true when user is logged in', () => {
      mockSessionService.$isLogged.mockReturnValue(of(true));
      const fixture = TestBed.createComponent(AppComponent);
      const app = fixture.componentInstance;

      app.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBeTruthy();
      });
    });

    it('should return false when user is not logged in', () => {
      mockSessionService.$isLogged.mockReturnValue(of(false));
      const fixture = TestBed.createComponent(AppComponent);
      const app = fixture.componentInstance;

      app.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBeFalsy();
      });
    });

    it('should log out and navigate to home', () => {
      const fixture = TestBed.createComponent(AppComponent);
      const app = fixture.componentInstance;

      app.logout();

      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['']);
    });
  });
});
