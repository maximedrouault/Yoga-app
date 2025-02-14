import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {expect, it} from '@jest/globals';

import {RegisterComponent} from './register.component';
import {RegisterRequest} from "../../interfaces/registerRequest.interface";
import Mocked = jest.Mocked;
import SpyInstance = jest.SpyInstance;
import {AuthService} from "../../services/auth.service";
import {of, throwError} from "rxjs";
import {Router} from "@angular/router";

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  const mockAuthService = {
    register: jest.fn().mockReturnValue(of(null))
  };

  const mockRouter = { navigate: jest.fn() }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  describe('Register form submit test suite', () => {
    const validRegisterRequest: Mocked<RegisterRequest> = {
      email: 'john.doe@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'testPassword'
    };

    const fillFormAndSubmit = (registerRequest: Mocked<RegisterRequest>) => {
      const firstNameInput = fixture.nativeElement.querySelector('[data-testid="first-name-input"]');
      const lastNameInput = fixture.nativeElement.querySelector('[data-testid="last-name-input"]');
      const emailInput = fixture.nativeElement.querySelector('[data-testid="email-input"]');
      const passwordInput = fixture.nativeElement.querySelector('[data-testid="password-input"]');
      const submitButton = fixture.nativeElement.querySelector('[data-testid="submit-button"]');

      firstNameInput.value = registerRequest.firstName;
      firstNameInput.dispatchEvent(new Event('input'));
      lastNameInput.value = registerRequest.lastName;
      lastNameInput.dispatchEvent(new Event('input'));
      emailInput.value = registerRequest.email;
      emailInput.dispatchEvent(new Event('input'));
      passwordInput.value = registerRequest.password;
      passwordInput.dispatchEvent(new Event('input'));

      fixture.detectChanges();
      submitButton.click();
    }


    it('should call submit() when register form is valid', () => {
      const submitSpy: SpyInstance = jest.spyOn(component, 'submit');

      fillFormAndSubmit(validRegisterRequest);

      expect(component.form.valid).toBeTruthy();
      expect(component.form.value).toEqual(validRegisterRequest);
      expect(submitSpy).toHaveBeenCalled();
    });

    it('should call authService.register() when register form is valid', () => {
      fillFormAndSubmit(validRegisterRequest);

      expect(mockAuthService.register).toHaveBeenCalledWith(validRegisterRequest);
    });

    it('should redirect to /login once registered', () => {
      fillFormAndSubmit(validRegisterRequest);

      expect(mockRouter.navigate).toHaveBeenCalledWith(['/login']);
    });

    it('should set onError to true when register fails', () => {
      mockAuthService.register.mockReturnValue(throwError((): Error => new Error('Register failed')));

      component.submit();

      expect(component.onError).toBeTruthy();
    });


    describe('Register form invalid cases', () => {
      it.each([
        { email: 'invalidEmail', firstName: 'Jo', lastName: 'D', password: '12' },
        { email: '', firstName: '', lastName: '', password: '' },
        { email: 'john.doe@example.com', firstName: 'J', lastName: 'Doe', password: '' }
      ])
      ('should not valid form when a field requirement is not correct', (invalidRegisterRequest) => {
        const submitSpy: SpyInstance = jest.spyOn(component, 'submit');

        fillFormAndSubmit(invalidRegisterRequest);

        expect(component.form.valid).toBeFalsy();
        expect(submitSpy).not.toHaveBeenCalled();
      });
    });
  });
});
