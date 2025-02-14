import {HttpClientModule} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {MatCardModule} from '@angular/material/card';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {SessionService} from 'src/app/services/session.service';
import {expect} from "@jest/globals";

import {MeComponent} from './me.component';
import {UserService} from "../../services/user.service";
import {User} from "../../interfaces/user.interface";
import {of} from "rxjs";
import {Router} from "@angular/router";

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  };

  const mockUser: User = {
    id: mockSessionService.sessionInformation.id,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    admin: mockSessionService.sessionInformation.admin,
    password: 'hashedPassword',
    createdAt: new Date('2020-01-15'),
    updatedAt: new Date('2020-02-20')
  };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of(mockUser)),
    delete: jest.fn().mockReturnValue(of(null))
  };

  const mockMatSnackBar = {
    open: jest.fn()
  };

  const mockRouter = {
    navigate: jest.fn()
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  describe('User information fetching test suite', () => {
    it('should retrieve user onInit', () => {
      component.ngOnInit();

      expect(mockUserService.getById).toHaveBeenCalledWith(mockSessionService.sessionInformation!.id.toString());
      expect(component.user).toEqual(mockUser);
    });
  });


  describe('User information title area test suite', () => {
    it('should display user info title', () => {
      const titleElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-info-title"]');

      expect(titleElement).toBeTruthy();
      expect(titleElement.textContent).toContain('User information');
    });
  });


  describe('User information area test suite when user is defined', () => {
    it('should display the firstName and lastName', () => {
      const userNamesElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-names"]');

      expect(userNamesElement).toBeTruthy();
      expect(userNamesElement.textContent).toContain('Name');
      expect(userNamesElement.textContent).toContain(mockUser.firstName);
      expect(userNamesElement.textContent).toContain(mockUser.lastName.toUpperCase());
    });

    it('should display email', () => {
      const userEmailElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-email"]');

      expect(userEmailElement).toBeTruthy();
      expect(userEmailElement.textContent).toContain('Email');
      expect(userEmailElement.textContent).toContain(mockUser.email);
    });

    it('should display user account creation date', () => {
      const userCreatedAtElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-created-at"]');

      expect(userCreatedAtElement).toBeTruthy();
      expect(userCreatedAtElement.textContent).toContain('Create at');
      expect(userCreatedAtElement.textContent).toContain('January 15, 2020');
    });

    it('should display user account update date', () => {
      const userUpdatedAtElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-updated-at"]');

      expect(userUpdatedAtElement).toBeTruthy();
      expect(userUpdatedAtElement.textContent).toContain('Last update');
      expect(userUpdatedAtElement.textContent).toContain('February 20, 2020');
    });
  });


  describe('User information area test suite when user is undefined', () => {
    beforeEach(() => {
      component.user = undefined;
      fixture.detectChanges();
    });

    it('should not display the firstName and LastName when user names is undefined', () => {
      const userNamesElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-names"]');

      expect(userNamesElement).toBeNull();
    });

    it('should not display email when user email is undefined', () => {
      const userEmailElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-email"]');

      expect(userEmailElement).toBeNull();
    });

    it('should not display user account creation date when user creation date is undefined', () => {
       const userCreatedAtElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-created-at"]');

      expect(userCreatedAtElement).toBeNull();
    });

    it('should not display user account update date when user update date is undefined', () => {
      const userUpdatedAtElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-updated-at"]');

      expect(userUpdatedAtElement).toBeNull();
    });

    it('should not display user information area when user is undefined', () => {
      const userInfoAreaElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="user-info-area"]');

      expect(userInfoAreaElement).toBeNull();
    });
  });


  describe('User information area test suite when user is an admin', () => {
    it('should display admin status when user is an admin', () => {
      const adminStatusElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="admin-status"]');

      expect(adminStatusElement).toBeTruthy();
      expect(adminStatusElement.textContent).toContain('You are admin');
    });

    it('should not display delete button for admin user', () => {
      const deleteAccountButton: HTMLElement = fixture.nativeElement.querySelector('[data-testid="delete-account-button"]');

      expect(deleteAccountButton).toBeNull();
    });
  });


  describe('User information area test suite when user is not an admin', () => {
    beforeEach(() => {
      component.user!.admin = false;
      fixture.detectChanges();
    });

    it('should not display admin status when user is not an admin', () => {
      const adminStatusElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="admin-status"]');

      expect(adminStatusElement).toBeNull();
    });

    it('should display Delete account label when user is not an admin', () => {
      const deleteAccountElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="delete-account-label"]');

      expect(deleteAccountElement).toBeTruthy();
      expect(deleteAccountElement.textContent).toContain('Delete my account:');
    });

    it('should display delete user account button when user is not an admin', () => {
      const deleteAccountElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="delete-account-button"]');

      expect(deleteAccountElement).toBeTruthy();
      expect(deleteAccountElement.textContent).toContain('Delete');
    });

    it('should call userService.delete() when delete button is clicked', () => {
      const deleteSpy = jest.spyOn(mockUserService, 'delete');

      const deleteAccountButton: HTMLElement = fixture.nativeElement.querySelector('[data-testid="delete-account-button"]');
      deleteAccountButton.click();

      expect(deleteSpy).toHaveBeenCalledWith(mockSessionService.sessionInformation!.id.toString());
    });

    it('should open MatSnackBar when delete user account is called', () => {
      const matSnackBarSpy = jest.spyOn(mockMatSnackBar, 'open');

      const deleteAccountButton: HTMLElement = fixture.nativeElement.querySelector('[data-testid="delete-account-button"]');
      deleteAccountButton.click();

      expect(matSnackBarSpy).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    });

    it('should logout user after user account is deleted', () => {
      const logOutSpy = jest.spyOn(mockSessionService, 'logOut');

      const deleteAccountButton: HTMLElement = fixture.nativeElement.querySelector('[data-testid="delete-account-button"]');
      deleteAccountButton.click();

      expect(logOutSpy).toHaveBeenCalled();
    });

    it('should navigate to home page after user account is deleted', () => {
      const routerSpy = jest.spyOn(mockRouter, 'navigate');

      const deleteAccountButton: HTMLElement = fixture.nativeElement.querySelector('[data-testid="delete-account-button"]');
      deleteAccountButton.click();

      expect(routerSpy).toHaveBeenCalledWith(['/']);
    });
  });


  describe('Back button test suite', () => {
    it('should display back button', () => {
      const backButtonElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="back-button"]');

      expect(backButtonElement).toBeTruthy();
    });

    it('should go back to the previous page when back button is clicked', () => {
      const backSpy = jest.spyOn(window.history, 'back');

      const backButtonElement: HTMLElement = fixture.nativeElement.querySelector('[data-testid="back-button"]');
      backButtonElement.click();

      expect(backSpy).toHaveBeenCalled();
    });
  });
});
