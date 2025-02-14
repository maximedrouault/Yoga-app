import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {RouterTestingModule} from '@angular/router/testing';
import {expect} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';

import {LoginComponent} from './login.component';
import {AuthService} from "../../services/auth.service";
import {LoginRequest} from "../../interfaces/loginRequest.interface";
import Mocked = jest.Mocked;
import SpyInstance = jest.SpyInstance;
import {of, throwError} from "rxjs";
import {Router} from "@angular/router";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logIn: jest.fn()
  };

  const mockAuthService = {
    login: jest.fn().mockReturnValue(of(mockSessionService.sessionInformation))
  }

  const mockRouter = {
    navigate: jest.fn()
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  describe('Login form submit test suite', () => {
    const validLoginRequest: Mocked<LoginRequest> = { email: 'john.doe@example.com', password: 'testPassword' };
    const invalidLoginRequest: Mocked<LoginRequest> = { email: 'invalidEmail', password: '!?' };

    const fillFormAndSubmit = (loginRequest: Mocked<LoginRequest>) => {
      const emailInput = fixture.nativeElement.querySelector('[data-testid="email-input"]');
      const passwordInput = fixture.nativeElement.querySelector('[data-testid="password-input"]');
      const submitButton = fixture.nativeElement.querySelector('[data-testid="submit-button"]');

      emailInput.value = loginRequest.email;
      emailInput.dispatchEvent(new Event('input'));
      passwordInput.value = loginRequest.password;
      passwordInput.dispatchEvent(new Event('input'));

      fixture.detectChanges();
      submitButton.click();
    };

    it('should call submit() when login form is valid', () => {
      const submitSpy: SpyInstance = jest.spyOn(component, 'submit');

      fillFormAndSubmit(validLoginRequest);

      expect(component.form.valid).toBeTruthy();
      expect(component.form.value).toEqual(validLoginRequest);
      expect(submitSpy).toHaveBeenCalled();
    });

    it('should call authService.login() when login form is valid', () => {
      fillFormAndSubmit(validLoginRequest);

      expect(mockAuthService.login).toHaveBeenCalledWith(validLoginRequest);
    });

    it('should call sessionService.logIn() and log user when login form is valid', () => {
      fillFormAndSubmit(validLoginRequest);

      expect(mockSessionService.logIn).toHaveBeenCalledWith(mockSessionService.sessionInformation);
    });

    it('should redirect to /sessions once logged in', () => {
      fillFormAndSubmit(validLoginRequest);

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    });

    it('should set onError to true when login fails', () => {
      mockAuthService.login.mockReturnValue(throwError((): Error => new Error('Login failed')));

      component.submit();

      expect(component.onError).toBeTruthy();
    });

    it('should not submit form login when it is not valid', () => {
      const submitSpy: SpyInstance = jest.spyOn(component, 'submit');

      fillFormAndSubmit(invalidLoginRequest);

      expect(component.form.valid).toBeFalsy();
      expect(submitSpy).not.toHaveBeenCalled();
    });

    it('should hide submit button when email is not in a valid format', () => {
      fillFormAndSubmit(invalidLoginRequest);

      const submitButton: HTMLButtonElement = fixture.nativeElement.querySelector('[data-testid="submit-button"]');

      expect(component.hide).toBeTruthy();
      expect(submitButton.disabled).toBeTruthy();
    });
  });
});
