describe('Form spec', () => {
  const urls = {
    loginUrl:'/login',
    createSessionUrl: '/sessions/create',
    updateSessionUrl: '/sessions/update/1',
    sessionUrl: '/sessions',
    loginApi: '/api/auth/login',
    sessionApi: '/api/session',
    sessionDetailApi: '/api/session/1',
    teacherApi: '/api/teacher'
  };

  const selectors = {
    emailInput: '[data-testid="email-input"]',
    passwordInput: '[data-testid="password-input"]',
    sessionDate: '[data-testid="session-date"]',
    submitButton: '[data-testid="submit-button"]',
    createSessionButton: '[data-testid="create-button"]',
    editSessionButton: '[data-testid="edit-button"]',
    backButton: '[data-testid="back-button"]',
    createTitle: '[data-testid="create-title"]',
    updateTitle: '[data-testid="update-title"]',
    sessionName: '[data-testid="session-name"]',
    sessionTeacherField: '[data-testid="session-teacher-field"]',
    sessionTeacherOption: '[data-testid="session-teacher-option"]',
    sessionDescription: '[data-testid="session-description"]',
    saveButton: '[data-testid="save-button"]'
  };

  const loginAndVisitCreateSession = (loginFixture: string) => {
    cy.intercept('POST', urls.loginApi, { fixture: loginFixture });
    cy.intercept('GET', urls.sessionApi, { fixture: 'sessions' });
    cy.intercept('GET', urls.sessionDetailApi, { fixture: 'session' });
    cy.intercept('POST', urls.sessionApi, { fixture: 'session' });
    cy.intercept('GET', urls.teacherApi, { fixture: 'teachers' });

    cy.visit(urls.loginUrl);
    cy.get(selectors.emailInput).type("yoga@studio.com");
    cy.get(selectors.passwordInput).type("test!1234");
    cy.get(selectors.submitButton).click();
  };


  it('should display create session form when create button is clicked on session', () => {
    loginAndVisitCreateSession('adminLoginResponse');
    cy.get(selectors.createSessionButton).click();

    cy.url().should('include', urls.createSessionUrl);
    cy.get(selectors.backButton).should('be.visible');
    cy.get(selectors.createTitle).should('be.visible').and('have.text', 'Create session');
    cy.get(selectors.sessionName).should('be.visible').and('have.attr', 'required');
    cy.get(selectors.sessionDate).should('be.visible').and('have.attr', 'type', 'date').and('have.attr', 'required');
    cy.get(selectors.sessionTeacherField).should('be.visible');
    cy.get(selectors.sessionDescription).should('be.visible').and('have.attr', 'required');
    cy.get(selectors.saveButton).should('be.disabled');
    cy.get(selectors.saveButton).contains('Save');
  });

  it('should enable save button when all fields are filled', () => {
    loginAndVisitCreateSession('adminLoginResponse');
    cy.get(selectors.createSessionButton).click();

    cy.get(selectors.sessionName).type('Test session');
    cy.get(selectors.sessionDate).type('2025-02-15');
    cy.get(selectors.sessionTeacherField).click();
    cy.get(selectors.sessionTeacherOption).contains('Jane Doe').click();
    cy.get(selectors.sessionDescription).type('Test description');
    cy.get(selectors.saveButton).should('be.enabled');
  });

  it('should create a session when save button is clicked', () => {
    loginAndVisitCreateSession('adminLoginResponse');
    cy.get(selectors.createSessionButton).click();

    cy.get(selectors.sessionName).type('Test create session');
    cy.get(selectors.sessionDate).type('2025-02-15');
    cy.get(selectors.sessionTeacherField).click();
    cy.get(selectors.sessionTeacherOption).contains('Jane Doe').click();
    cy.get(selectors.sessionDescription).type('Test create description');

    const newSession = {
      id: 3,
      name: 'Test create session',
      description: 'Test create description',
      date: '2025-02-15T17:55:25',
      teacher_id: 1,
      users: [],
      createdAt: '2025-02-15T17:55:25',
      updatedAt: '2025-02-15T17:55:25'
    };

    cy.fixture('sessions').then((sessions) => {
      cy.intercept('GET', urls.sessionApi, { body: [...sessions, newSession] });
    });

    cy.get(selectors.saveButton).click();
    cy.url().should('include', urls.sessionUrl);
  });

  it('should display update session form when Edit button is clicked on session', () => {
    loginAndVisitCreateSession('adminLoginResponse');
    cy.get(selectors.editSessionButton).first().click();

    cy.url().should('include', urls.updateSessionUrl);
    cy.get(selectors.backButton).should('be.visible');
    cy.get(selectors.updateTitle).should('be.visible').and('have.text', 'Update session');
    cy.get(selectors.sessionName).should('be.visible').and('have.attr', 'required');
    cy.get(selectors.sessionName).should('have.value', 'Test session');
    cy.get(selectors.sessionDate).should('be.visible').and('have.attr', 'type', 'date').and('have.attr', 'required');
    cy.get(selectors.sessionDate).should('have.value', '2025-02-10');
    cy.get(selectors.sessionTeacherField).should('be.visible');
    cy.get(selectors.sessionTeacherField).contains('Jane Doe');
    cy.get(selectors.sessionDescription).should('be.visible').and('have.attr', 'required');
    cy.get(selectors.saveButton).should('be.enabled');
    cy.get(selectors.saveButton).contains('Save');
  });

  it('should update a session when save button is clicked', () => {
    loginAndVisitCreateSession('adminLoginResponse');
    cy.get(selectors.editSessionButton).first().click();

    cy.get(selectors.sessionName).clear();
    cy.get(selectors.sessionName).type('Test update session');
    cy.get(selectors.sessionDate).type('2025-02-17');
    cy.get(selectors.sessionTeacherField).click();
    cy.get(selectors.sessionTeacherOption).contains('Dylan Doe').click();
    cy.get(selectors.sessionDescription).clear();
    cy.get(selectors.sessionDescription).type('Test update description');
    cy.get(selectors.saveButton).should('be.enabled');

    const updatedSession = {
      id: 1,
      name: 'Test update session',
      description: 'Test update description',
      date: '2025-02-17T18:55:25',
      teacher_id: 2,
      users: [],
      createdAt: '2025-02-15T17:55:25',
      updatedAt: '2025-02-16T18:55:25'
    };

    cy.intercept('PUT', urls.sessionDetailApi, { body: updatedSession });

    cy.get(selectors.saveButton).click();
    cy.url().should('include', urls.sessionUrl);
  });
});
