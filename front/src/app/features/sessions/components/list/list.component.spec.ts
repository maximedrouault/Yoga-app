import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {expect, it} from '@jest/globals';
import {SessionService} from 'src/app/services/session.service';

import {ListComponent} from './list.component';
import {SessionApiService} from "../../services/session-api.service";
import {of} from "rxjs";
import {RouterTestingModule} from "@angular/router/testing";

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let mockSessionService: any;

  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of([
      { id: 1, name: 'Session 1', description: 'Description 1', date: new Date(), teacher_id: 1, users: [1, 2] },
      { id: 2, name: 'Session 2', description: 'Description 2', date: new Date(), teacher_id: 2, users: [3, 4] }
    ]))
  }

  const selectors = {
    createButton: '[data-testid="create-button"]',
    editButton: '[data-testid="edit-button"]'
  };

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: {
        admin: true
      }
    }

    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientModule, MatCardModule, MatIconModule, RouterTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should display create button if user is admin', () => {
    const createButtonElement: HTMLButtonElement = fixture.nativeElement.querySelector(selectors.createButton);

    expect(createButtonElement).toBeTruthy();
    expect(createButtonElement.textContent).toContain('Create');
  });

  it('should not display create button if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();

    const createButtonElement: HTMLButtonElement = fixture.nativeElement.querySelector(selectors.createButton);

    expect(component.user?.admin).toBe(false);
    expect(createButtonElement).toBeFalsy();
  });

  it('should display edit button for each session if user is admin', () => {
    const editButtonElements = fixture.nativeElement.querySelectorAll(selectors.editButton);

    expect(editButtonElements).toBeTruthy();
    expect(editButtonElements.length).toBe(2);
    editButtonElements.forEach((element: HTMLButtonElement) => {
      expect(element.textContent).toContain('Edit');
    });
  });

  it('should not display edit button for sessions if user is not admin', () => {
    mockSessionService.sessionInformation.admin = false;
    fixture.detectChanges();

    const editButtonElement: HTMLButtonElement = fixture.nativeElement.querySelector(selectors.editButton);

    expect(component.user?.admin).toBe(false);
    expect(editButtonElement).toBeFalsy();
  });
});
