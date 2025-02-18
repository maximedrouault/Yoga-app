describe('Sessions spec', () => {
  const urls = {
    loginUrl: '/login',
    sessionUrl: '/sessions',
    loginApi: '/api/auth/login',
    sessionApi: '/api/session'
  };

  const selectors = {
    emailInput: '[data-testid="email-input"]',
    passwordInput: '[data-testid="password-input"]',
    submitButton: '[data-testid="submit-button"]'
  };

  const loginAndVisitSessions = (loginFixture: string) => {
    cy.fixture(loginFixture).then((loginResponse) => {
      cy.intercept('POST', urls.loginApi, { body: loginResponse });
    });
    cy.fixture('sessions').then((sessions) => {
      cy.intercept('GET', urls.sessionApi, { body: sessions });
    });

    cy.visit(urls.loginUrl);
    cy.get(selectors.emailInput).type("yoga@studio.com");
    cy.get(selectors.passwordInput).type("test!1234");
    cy.get(selectors.submitButton).click();
  };

  it('should display list of session', () => {
    loginAndVisitSessions('adminLoginResponse');

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
    loginAndVisitSessions('adminLoginResponse');

    cy.get('[data-testid="create-button"]')
      .should('be.visible')
      .should('have.length', 1)
      .should('contain.text', 'Create');
  });

  it('should not display create session button when user is not an admin', () => {
    loginAndVisitSessions('nonAdminLoginResponse');

    cy.get('[data-testid="create-button"]').should('not.exist');
  });

  it('should display edit session button when user is an admin', () => {
    loginAndVisitSessions('adminLoginResponse');

    cy.get('[data-testid="edit-button"]')
      .should('be.visible')
      .should('have.length', 2)
      .should('contain.text', 'Edit');
  });

  it('should not display edit session button when user is not an admin', () => {
    loginAndVisitSessions('nonAdminLoginResponse');

    cy.get('[data-testid="edit-button"]').should('not.exist');
  });
});
