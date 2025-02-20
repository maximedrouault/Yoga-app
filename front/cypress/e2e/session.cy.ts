describe('Session spec', () => {
  const urls = {
    loginUrl: '/login',
    sessionUrl: '/session',
    sessionDetailUrl: '/sessions/detail/1',
    loginApi: '/api/auth/login',
    sessionApi: '/api/session',
    sessionDetailApi: '/api/session/1',
    teacherDetailApi: '/api/teacher/1',
    sessionUnParticipateApi: '/api/session/1/participate/1',
    sessionParticipateApi: '/api/session/1/participate/2'
  };

  const selectors = {
    emailInput: '[data-testid="email-input"]',
    passwordInput: '[data-testid="password-input"]',
    submitButton: '[data-testid="submit-button"]',
    detailButton: '[data-testid="detail-button"]',
    backButton: '[data-testid="back-button"]',
    sessionName: '[data-testid="session-name"]',
    sessionTeacher: '[data-testid="session-teacher"]',
    sessionAttendees: '[data-testid="session-attendees"]',
    sessionDate: '[data-testid="session-date"]',
    sessionDescription: '[data-testid="session-description"]',
    sessionCreationDate: '[data-testid="session-creation-date"]',
    sessionUpdateDate: '[data-testid="session-update-date"]',
    sessionDeleteButton: '[data-testid="delete-button"]',
    participateButton: '[data-testid="participate-button"]',
    unParticipateButton: '[data-testid="unparticipate-button"]'
  };

  const loginAndVisitSessionDetails = (loginFixture: string) => {
    cy.fixture(loginFixture).then((loginResponse) => {
      cy.intercept('POST', urls.loginApi, { body: loginResponse });
    });
    cy.fixture('sessions').then((sessions) => {
      cy.intercept('GET', urls.sessionApi, { body: sessions });
    });
    cy.fixture('session').then((session) => {
      cy.intercept('GET', urls.sessionDetailApi, { body: session });
      cy.intercept('DELETE', urls.sessionDetailApi, { body: {} });
    });
    cy.fixture('teacher').then((teacher) => {
      cy.intercept('GET', urls.teacherDetailApi, { body: teacher });
    });
    cy.intercept('DELETE', urls.sessionUnParticipateApi, { body: {} });
    cy.intercept('POST', urls.sessionParticipateApi, { body: {} });

    cy.visit(urls.loginUrl);
    cy.get(selectors.emailInput).type("yoga@studio.com");
    cy.get(selectors.passwordInput).type("test!1234");
    cy.get(selectors.submitButton).click();
    cy.get(selectors.detailButton).first().click();
  };

  it('should display the details of the session', () => {
    loginAndVisitSessionDetails('adminLoginResponse');

    cy.url().should('include', urls.sessionDetailUrl);

    cy.get(selectors.backButton).should('be.visible');
    cy.get(selectors.sessionName).should('contain.text', 'Test Session');
    cy.get(selectors.sessionTeacher).should('contain.text', 'Jane DOE');
    cy.get(selectors.sessionAttendees).should('contain.text', '1 attendees');
    cy.get(selectors.sessionDate).should('contain.text', 'February 10, 2025');
    cy.get(selectors.sessionDescription).should('contain.text', 'Test description');
    cy.get(selectors.sessionCreationDate).should('contain.text', 'February 7, 2025');
    cy.get(selectors.sessionUpdateDate).should('contain.text', 'February 8, 2025');
  });

  it('should display delete session button when user is an admin', () => {
    loginAndVisitSessionDetails('adminLoginResponse');

    cy.get(selectors.sessionDeleteButton)
      .should('be.visible')
      .should('contain.text', 'Delete');
  });

  it('should not display delete session button when user is not an admin', () => {
    loginAndVisitSessionDetails('nonAdminLoginResponse');

    cy.get(selectors.sessionDeleteButton).should('not.exist');
  });

  it('should delete session when delete button is clicked', () => {
    loginAndVisitSessionDetails('adminLoginResponse');

    cy.fixture('sessions').then((sessions) => {
      const sessionsAfterDelete = sessions.filter((session: { id: number; }) => session.id !== 1);

      cy.intercept('GET', urls.sessionApi, { body: sessionsAfterDelete });
    });

    cy.get(selectors.sessionDeleteButton).click();
    cy.url().should('include', urls.sessionUrl);
    cy.get(selectors.sessionDeleteButton).should('not.exist');
  });

  it('should display "Do not participate" button when user is an attendee', () => {
    loginAndVisitSessionDetails('attendeeLoginResponse');

    cy.get(selectors.unParticipateButton)
      .should('be.visible')
      .should('contain.text', 'Do not participate');
  });

  it('should display "Participate" button when user is not an attendee', () => {
    loginAndVisitSessionDetails('nonAttendeeLoginResponse');

    cy.get(selectors.participateButton)
      .should('be.visible')
      .should('contain.text', 'Participate');
  });

  it('should switch the attendee status to unAttendee when "Do not participate" button is clicked', () => {
    loginAndVisitSessionDetails('attendeeLoginResponse');

    cy.fixture('session').then((session) => {
      const sessionAfterUnParticipation = { ...session, users: [] };

      cy.intercept('GET', urls.sessionDetailApi, { body: sessionAfterUnParticipation });
    });

    cy.get(selectors.unParticipateButton).click();
    cy.get(selectors.participateButton)
      .should('be.visible')
      .should('contain.text', 'Participate');
  });

  it('should switch the attendee status to Attendee when "Participate" button is clicked', () => {
    loginAndVisitSessionDetails('nonAttendeeLoginResponse');

    cy.fixture('session').then((session) => {
      const sessionAfterParticipation = { ...session, users: [2] };

      cy.intercept('GET', urls.sessionDetailApi, { body: sessionAfterParticipation });
    });

    cy.get(selectors.participateButton).click();
    cy.get(selectors.unParticipateButton)
      .should('be.visible')
      .should('contain.text', 'Do not participate');
  });

  it('should redirect to session list when back button is clicked', () => {
    loginAndVisitSessionDetails('adminLoginResponse');

    cy.url().should('include', urls.sessionDetailUrl);
    cy.get(selectors.backButton).click();
    cy.url().should('include', urls.sessionUrl);
  });
});
