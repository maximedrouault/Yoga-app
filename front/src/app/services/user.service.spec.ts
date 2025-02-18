import {HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {expect, it} from '@jest/globals';

import {UserService} from './user.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {User} from "../interfaces/user.interface";

describe('UserService', () => {
  let userService: UserService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    userService = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(userService).toBeTruthy();
  });


  describe('UserService test suite', () => {
    it('should retrieve user by id', () => {
      const mockUser: User = {
        id: 1,
        email: 'john.doe@example.com',
        lastName: 'Doe',
        firstName: 'John',
        admin: false,
        password: 'password',
        createdAt: new Date(),
        updatedAt: new Date()
      };

      userService.getById('1').subscribe(user => {
        expect(user).toEqual(mockUser);
      });

      const request = httpMock.expectOne('api/user/1');
      expect(request.request.method).toBe('GET');
      request.flush(mockUser);
    });

    it('should handle error when retrieving user by id', () => {
      userService.getById('1').subscribe({
        next: () => fail('should have failed with 404 error'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const request = httpMock.expectOne('api/user/1');
      expect(request.request.method).toBe('GET');
      request.flush('User not found', { status: 404, statusText: 'Not Found' });
    });

    it('should delete user by id', () => {
        userService.delete('1').subscribe(response => {
          expect(response).toBeTruthy();
        });

        const request = httpMock.expectOne('api/user/1');
        expect(request.request.method).toBe('DELETE');
        request.flush(null);
    });

    it('should handle error when deleting user by id', () => {
      userService.delete('1').subscribe({
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const request = httpMock.expectOne('api/user/1');
      request.flush('User not found', { status: 404, statusText: 'Not Found' });
    });
  });
});
