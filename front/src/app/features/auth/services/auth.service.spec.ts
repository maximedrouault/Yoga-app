import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {AuthService} from './auth.service';
import {LoginRequest} from '../interfaces/loginRequest.interface';
import {RegisterRequest} from '../interfaces/registerRequest.interface';
import {SessionInformation} from 'src/app/interfaces/sessionInformation.interface';
import {expect, it} from "@jest/globals";

describe('AuthService', () => {
  let authService: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    authService = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should register a user successfully', () => {
    const registerRequest: RegisterRequest = {
      email: 'john.doe@example.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'testPassword'
    };

    authService.register(registerRequest).subscribe(response => {
      expect(response).toBeUndefined();
    });

    const request = httpMock.expectOne('api/auth/register');
    expect(request.request.method).toBe('POST');
    request.flush(null);
  });

  it('should login a user successfully', () => {
    const loginRequest: LoginRequest = {
      email: 'john.doe@example.com',
      password: 'testPassword'
    };

    const sessionInfo: SessionInformation = {
      token: 'abc123',
      type: 'type',
      id: 1,
      username: 'john.doe@example.com',
      firstName: 'John',
      lastName: 'Doe',
      admin: false
    };

    authService.login(loginRequest).subscribe(response => {
      expect(response).toEqual(sessionInfo);
    });

    const request = httpMock.expectOne('api/auth/login');
    expect(request.request.method).toBe('POST');
    request.flush(sessionInfo);
  });

  it('should handle register error', () => {
    const registerRequest: RegisterRequest = {
      email: 'testUser',
      firstName: 'John',
      lastName: 'Doe',
      password: 'testPassword'
    };

    authService.register(registerRequest).subscribe({
      error: (error) => {
        expect(error.status).toBe(400);
      }
    });

    const request = httpMock.expectOne('api/auth/register');
    expect(request.request.method).toBe('POST');
    request.flush('Invalid request', { status: 400, statusText: 'Bad Request' });
  });

  it('should handle login error', () => {
    const loginRequest: LoginRequest = {
      email: 'testUser',
      password: 'testPassword'
    };

    authService.login(loginRequest).subscribe({
      error: (error) => {
        expect(error.status).toBe(401);
      }
    });

    const request = httpMock.expectOne('api/auth/login');
    expect(request.request.method).toBe('POST');
    request.flush('Unauthorized', { status: 401, statusText: 'Unauthorized' });
  });
});
