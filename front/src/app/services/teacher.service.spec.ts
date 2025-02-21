import {HttpClientModule} from '@angular/common/http';
import {TestBed} from '@angular/core/testing';
import {expect, it} from '@jest/globals';

import {TeacherService} from './teacher.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Teacher} from "../interfaces/teacher.interface";

describe('TeacherService', () => {
  let teacherService: TeacherService;
  let httpMock: HttpTestingController;

  const urls = {
    teachersApi: 'api/teacher',
    teacherApi: 'api/teacher/1'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        HttpClientTestingModule
      ]
    });
    teacherService = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(teacherService).toBeTruthy();
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('TeacherService test suite', () => {
    it('should retrieve all teachers', () => {
      const mockTeachers: Teacher[] = [
        {id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date()},
        {id: 2, lastName: 'Smith', firstName: 'Jane', createdAt: new Date(), updatedAt: new Date()}
      ];

      teacherService.all().subscribe(teachers => {
        expect(teachers).toEqual(mockTeachers);
      });

      const request = httpMock.expectOne(urls.teachersApi);
      expect(request.request.method).toBe('GET');
      request.flush(mockTeachers);
    });

    it('should retrieve teacher', () => {
      const mockTeacher: Teacher = {
        id: 1, lastName: 'Doe', firstName: 'John', createdAt: new Date(), updatedAt: new Date()
      };

      teacherService.detail('1').subscribe(teacher => {
        expect(teacher).toEqual(mockTeacher);
      });

      const request = httpMock.expectOne(urls.teacherApi);
      expect(request.request.method).toBe('GET');
      request.flush(mockTeacher);
    });

    it('should handle error when retrieving all teachers', () => {
      teacherService.all().subscribe({
        next: () => fail('should have failed with 500 status'),
        error: (error) => {
          expect(error.status).toBe(500);
        }
      });

      const request = httpMock.expectOne(urls.teachersApi);
      request.flush('error', { status: 500, statusText: 'Server Error' });
    });

    it('should handle error when retrieving teacher details by id', () => {
      teacherService.detail('1').subscribe({
        next: () => fail('should have failed with 404 status'),
        error: (error) => {
          expect(error.status).toBe(404);
        }
      });

      const request = httpMock.expectOne(urls.teacherApi);
      request.flush('error', { status: 404, statusText: 'Not Found' });
    });
  });
});
