import {TestBed} from '@angular/core/testing';
import {expect, it} from '@jest/globals';

import {SessionService} from './session.service';
import {SessionInformation} from "../interfaces/sessionInformation.interface";

describe('SessionService', () => {
  let sessionService: SessionService;

  const mockSessionInfo: SessionInformation = {
    token: 'abc123',
    type: 'Bearer',
    id: 1,
    username: 'test',
    firstName: 'test',
    lastName: 'test',
    admin: true
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    sessionService = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(sessionService).toBeTruthy();
  });


  describe('SessionService test suite', () => {
    it('should log in a user', () => {
      sessionService.logIn(mockSessionInfo);

      expect(sessionService.isLogged).toBe(true);
      expect(sessionService.sessionInformation).toEqual(mockSessionInfo);
    });

    it('should log out a user', () => {
      sessionService.logIn(mockSessionInfo);
      sessionService.logOut();

      expect(sessionService.isLogged).toBe(false);
      expect(sessionService.sessionInformation).toBeUndefined();
    });

    it('should emit true when user logs in', (done) => {
      sessionService.$isLogged().subscribe(isLogged => {
        if (isLogged) {
          expect(isLogged).toBe(true);
          done();
        }
      });

      sessionService.logIn(mockSessionInfo);
    });

    it('should emit false when user logs out', (done) => {
      sessionService.logIn(mockSessionInfo);

      sessionService.$isLogged().subscribe(isLogged => {
        if (!isLogged) {
          expect(isLogged).toBe(false);
          done();
        }
      });

      sessionService.logOut();
    });
  });
});
