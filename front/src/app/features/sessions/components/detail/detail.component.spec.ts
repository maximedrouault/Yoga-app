import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ReactiveFormsModule} from '@angular/forms';
import {MatSnackBar} from '@angular/material/snack-bar';
import {RouterTestingModule,} from '@angular/router/testing';
import {expect, it} from '@jest/globals';
import {SessionService} from '../../../../services/session.service';

import {DetailComponent} from './detail.component';
import {SessionApiService} from "../../services/session-api.service";
import {of} from "rxjs";
import {MatCardModule} from "@angular/material/card";
import {MatIconModule} from "@angular/material/icon";
import {ActivatedRoute, Router} from "@angular/router";
import {TeacherService} from "../../../../services/teacher.service";


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  };

  const mockSession = {
    id: 1,
    name: 'Test Session',
    description: 'Test Description',
    date: new Date(),
    teacher_id: 1,
    users: [2],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    delete: jest.fn().mockReturnValue(of(null)),
    participate: jest.fn().mockReturnValue(of(null)),
    unParticipate: jest.fn().mockReturnValue(of(null))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of({
      id: 1,
      lastName: 'Test',
      firstName: 'Teacher',
      createdAt: new Date(),
      updatedAt: new Date()
    }))
  };

  const mockMatSnackBar = { open: jest.fn() };

  const mockRouter = { navigate: jest.fn() };

  const mockActivatedRoute = {
    snapshot: {
      paramMap: {
        get: jest.fn().mockReturnValue(mockSession.id.toString())
      },
      root: {}
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatIconModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
        { provide: ActivatedRoute, useValue: mockActivatedRoute},
        { provide: TeacherService, useValue: mockTeacherService },
      ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    component.sessionId = mockSession.id.toString();
    component.userId = mockSessionService.sessionInformation.id.toString();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  describe('Detail component test suite', () => {
    it('should fetch session information onInit', () => {
      const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
      const sessionApiServiceSpy = jest.spyOn(mockSessionApiService, 'detail');

      component.ngOnInit();

      expect(fetchSessionSpy).toHaveBeenCalled();
      expect(sessionApiServiceSpy).toHaveBeenCalledWith(mockSession.id.toString());
      expect(component.session).toEqual(mockSession);
    });

    it('should participate in session', () => {
      component.participate();

      expect(mockSessionApiService.participate)
        .toHaveBeenCalledWith(mockSession.id.toString(), mockSessionService.sessionInformation.id.toString());
    });

    it('should unParticipate from session', () => {
      component.unParticipate();

      expect(mockSessionApiService.unParticipate)
        .toHaveBeenCalledWith(mockSession.id.toString(), mockSessionService.sessionInformation.id.toString());
    });

    it('should delete session', () => {
      component.delete();

      expect(mockSessionApiService.delete).toHaveBeenCalledWith(mockSession.id.toString());
    });

    it('should show a snack bar message when session is deleted', () => {
      component.delete();

      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
    });

    it('should navigate to sessions after deleting a session', () => {
      component.delete();

      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should navigate back on back method call', () => {
      const backSpy = jest.spyOn(window.history, 'back');

      component.back();

      expect(backSpy).toHaveBeenCalled();
    });

    it('should handle session not found', () => {
      mockSessionApiService.detail.mockReturnValueOnce(of(null));

      component.ngOnInit();

      expect(component.session).toBeFalsy();
    });

    it('should handle teacher not found', () => {
      mockTeacherService.detail.mockReturnValueOnce(of(null));

      component.ngOnInit();

      expect(component.teacher).toBeFalsy();
    });
  });
});

