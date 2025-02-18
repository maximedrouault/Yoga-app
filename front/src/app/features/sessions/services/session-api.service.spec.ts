import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect, it } from '@jest/globals';

import { SessionApiService } from './session-api.service';
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {Session} from "../interfaces/session.interface";

describe('SessionsService', () => {
  let sessionApiService: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule,
        HttpClientTestingModule
      ],
      providers:[SessionApiService]
    });
    sessionApiService = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(sessionApiService).toBeTruthy();
  });


  describe('SessionApiService test suite', () => {
    it('should retrieve all sessions', () => {
      const mockSessions: Partial<Session>[] = [{ id: 1, name: 'Session 1' }, { id: 2, name: 'Session 2' }];

      sessionApiService.all().subscribe(sessions => {
        expect(sessions.length).toBe(2);
        expect(sessions).toEqual(mockSessions);
      });

      const request = httpMock.expectOne('api/session');
      expect(request.request.method).toBe('GET');
      request.flush(mockSessions);
    });

    it('should retrieve session', () => {
      const mockSession: Partial<Session> = { id: 1, name: 'Session 1' };

      sessionApiService.detail('1').subscribe(session => {
        expect(session).toEqual(mockSession);
      });

      const request = httpMock.expectOne('api/session/1');
      expect(request.request.method).toBe('GET');
      request.flush(mockSession);
    });

    it('should delete a session', () => {
      sessionApiService.delete('1').subscribe(response => {
        expect(response).toBeTruthy();
      });

      const request = httpMock.expectOne('api/session/1');
      expect(request.request.method).toBe('DELETE');
      request.flush({});
    });

    it('should create a new session', () => {
      const mockNewSession = {
        id: 3,
        name: 'Session 3',
        description: 'Description 3',
        date: new Date(),
        teacher_id: 1,
        users: []
      };

      sessionApiService.create(mockNewSession).subscribe(session => {
        expect(session).toEqual(mockNewSession);
      });

      const request = httpMock.expectOne('api/session');
      expect(request.request.method).toBe('POST');
      request.flush(mockNewSession);
    });

    it('should update an existing session', () => {
      const updatedSession: Session = {
        id: 1,
        name: 'Updated Session',
        description: 'Updated Description',
        date: new Date(),
        teacher_id: 1,
        users: []
      };

      sessionApiService.update('1', updatedSession).subscribe(session => {
        expect(session).toEqual(updatedSession);
      });

      const request = httpMock.expectOne('api/session/1');
      expect(request.request.method).toBe('PUT');
      request.flush(updatedSession);
    });

    it('should participate in a session', () => {
      sessionApiService.participate('1', 'user1').subscribe(response => {
        expect(response).toBeUndefined();
      });

      const request = httpMock.expectOne('api/session/1/participate/user1');
      expect(request.request.method).toBe('POST');
      request.flush(null);
    });

    it('should unparticipate from a session', () => {
      sessionApiService.unParticipate('1', 'user1').subscribe(response => {
        expect(response).toBeUndefined();
      });

      const request = httpMock.expectOne('api/session/1/participate/user1');
      expect(request.request.method).toBe('DELETE');
      request.flush(null);
    });
  });
});
