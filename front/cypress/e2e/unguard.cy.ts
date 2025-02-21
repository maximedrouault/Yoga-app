describe('Unguard spec', () => {
  const urls = {
    loginUrl: '/login',
    sessionsUrl: '/sessions',
    loginApi: '/api/auth/login',
    sessionsApi: '/api/session'
  };

  const selectors = {
    emailInput: '[data-testid="email-input"]',
    passwordInput: '[data-testid="password-input"]',
    submitButton: '[data-testid="submit-button"]'
  };

  const loginUser = (loginFixture: string) => {
    cy.intercept('POST', urls.loginApi, { fixture: loginFixture });
    cy.intercept('GET', urls.sessionsApi, { fixture: 'sessions' });

    cy.visit(urls.loginUrl);
    cy.get(selectors.emailInput).type("yoga@studio.com");
    cy.get(selectors.passwordInput).type("test!1234");
    cy.get(selectors.submitButton).click();
  };

  it('should redirect to session list when user is logged', () => {
    loginUser('adminLoginResponse');

    cy.url().should('include', urls.sessionsUrl);
  });

  it('should redirect to login page when user is not logged', () => {
    cy.visit(urls.sessionsUrl);

    cy.url().should('include', urls.loginUrl);
  });
});
