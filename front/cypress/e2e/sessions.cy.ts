describe('Sessions spec', () => {
  const urls = {
    loginUrl: '/login',
    sessionUrl: '/sessions',
    loginApi: '/api/auth/login',
    sessionApi: '/api/session'
  };

  const adminLoginResponse = {
    token: "abc123",
    type: "Bearer",
    id: 1,
    username: "yoga@studio.com",
    firstName: "Yoga",
    lastName: "Studio",
    admin: true
  };

  const nonAdminLoginResponse = { ...adminLoginResponse, admin: false };

  const sessions = [
    {
      id: 1,
      name: "Test session",
      description: "Test description",
      date: "2025-02-10T00:00:00.000+00:00",
      teacher_id: 1,
      users: [],
      createdAt: "2025-02-07T14:43:32",
      updatedAt: "2025-02-07T14:44:03"
    },
    {
      id: 2,
      name: "Test session 2",
      description: "Test description 2",
      date: "2025-02-18T00:00:00.000+00:00",
      teacher_id: 2,
      users: [],
      createdAt: "2025-02-15T17:55:25",
      updatedAt: "2025-02-15T17:55:25"
    }
  ];

  const selectors = {
    emailInput: '[data-testid="email-input"]',
    passwordInput: '[data-testid="password-input"]',
    submitButton: '[data-testid="submit-button"]'
  };

  const loginAndVisitSessions = (loginResponse) => {
    cy.intercept('POST', urls.loginApi, { body: loginResponse });
    cy.intercept('GET', urls.sessionApi, { body: sessions });
    cy.visit(urls.loginUrl);
    cy.get(selectors.emailInput).type("yoga@studio.com");
    cy.get(selectors.passwordInput).type("test!1234");
    cy.get(selectors.submitButton).click();
  };

  it('should display list of session', () => {
    loginAndVisitSessions(adminLoginResponse);

    cy.url().should('include', urls.sessionUrl);
    cy.get('[data-testid="session-list"]').should('have.length', 2);
    cy.get('[data-testid="session-list"]').first()
      .should('contain.text', 'Test session')
      .should('contain.text', 'Session on February 10, 2025')
      .should('contain.text', 'Test description');
    cy.get('[data-testid="session-list"]').last()
      .should('contain.text', 'Test session 2')
      .should('contain.text', 'Session on February 18, 2025')
      .should('contain.text', 'Test description 2');
    cy.get('[data-testid="detail-button"]')
      .should('have.length', 2)
      .should('contain.text', 'Details');
  });

  it('should display create session button when user is an admin', () => {
    loginAndVisitSessions(adminLoginResponse);

    cy.get('[data-testid="create-button"]')
      .should('be.visible')
      .should('have.length', 1)
      .should('contain.text', 'Create');
  });

  it('should not display create session button when user is not an admin', () => {
    loginAndVisitSessions(nonAdminLoginResponse);

    cy.get('[data-testid="create-button"]').should('not.exist');
  });

  it('should display edit session button when user is an admin', () => {
    loginAndVisitSessions(adminLoginResponse);

    cy.get('[data-testid="edit-button"]')
      .should('be.visible')
      .should('have.length', 2)
      .should('contain.text', 'Edit');
  });

  it('should not display edit session button when user is not an admin', () => {
    loginAndVisitSessions(nonAdminLoginResponse);

    cy.get('[data-testid="edit-button"]').should('not.exist');
  });
});
