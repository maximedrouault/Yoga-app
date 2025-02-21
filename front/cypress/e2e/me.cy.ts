describe('Me spec', () => {
  const urls = {
    rootUrl: '/',
    loginUrl: '/login',
    sessionUrl: '/sessions',
    loginApi: '/api/auth/login',
    sessionsApi: '/api/session',
    userApi: '/api/user/1'
  };

  const selectors = {
    emailInput: '[data-testid="email-input"]',
    passwordInput: '[data-testid="password-input"]',
    submitButton: '[data-testid="submit-button"]',
    navAccountButton: '[data-testid="nav-account-button"]',
    backButton: '[data-testid="back-button"]',
    userInfoTitle: '[data-testid="user-info-title"]',
    userNames: '[data-testid="user-names"]',
    userEmail: '[data-testid="user-email"]',
    adminStatus: '[data-testid="admin-status"]',
    userCreatedAt: '[data-testid="user-created-at"]',
    userUpdatedAt: '[data-testid="user-updated-at"]',
    deleteAccountLabel: '[data-testid="delete-account-label"]',
    deleteAccountButton: '[data-testid="delete-account-button"]',

  };

  const loginAndVisitMe = (loginFixture: string) => {
    cy.fixture(loginFixture).then((loginResponse) => {
      cy.intercept('POST', urls.loginApi, {body: loginResponse});
    });
    cy.fixture('sessions').then((sessions) => {
      cy.intercept('GET', urls.sessionsApi, { body: sessions });
    });
    cy.fixture('user').then((me) => {
      cy.intercept('GET', urls.userApi, { body: me });
    });

    cy.visit(urls.loginUrl);
    cy.get(selectors.emailInput).type("yoga@studio.com");
    cy.get(selectors.passwordInput).type("test!1234");
    cy.get(selectors.submitButton).click();
  };


  it('should display the details of user account', () => {
    loginAndVisitMe('adminLoginResponse');

    cy.fixture('user').then((me) => {
      cy.intercept('GET', urls.userApi, { body: me });
    });

    cy.url().should('include', urls.sessionUrl);
    cy.get(selectors.navAccountButton).click();
    cy.url().should('include', '/me');
    cy.get(selectors.backButton).should('be.visible');
    cy.get(selectors.userInfoTitle).should('contain.text', 'User information');
    cy.get(selectors.userNames).should('contain.text', 'John DOE');
    cy.get(selectors.userEmail).should('contain.text', 'john.doe@example.com');
    cy.get(selectors.userCreatedAt).should('contain.text', 'February 3, 2025');
    cy.get(selectors.userUpdatedAt).should('contain.text', 'February 5, 2025');
  });

  it('should display the admin status when user is an admin', () => {
    loginAndVisitMe('adminLoginResponse');

    cy.fixture('user').then((me) => {
      cy.intercept('GET', urls.userApi, { body: { ...me, admin: true } });
    });

    cy.get(selectors.navAccountButton).click();
    cy.get(selectors.adminStatus)
      .should('be.visible')
      .should('contain.text', 'You are admin');
  });

  it('should not display the admin status if user is not an admin', () => {
    loginAndVisitMe('nonAdminLoginResponse');

    cy.get(selectors.navAccountButton).click();
    cy.get(selectors.adminStatus).should('not.exist');
  });

  it('should display the delete account button when user is not an admin', () => {
    loginAndVisitMe('nonAdminLoginResponse');

    cy.get(selectors.navAccountButton).click();
    cy.get(selectors.deleteAccountLabel)
      .should('be.visible')
      .should('contain.text', 'Delete my account');
    cy.get(selectors.deleteAccountButton)
      .should('be.visible')
      .should('contain.text', 'Delete');
  });

  it('should redirect to session list when back button is clicked', () => {
    loginAndVisitMe('adminLoginResponse');

    cy.get(selectors.navAccountButton).click();
    cy.get(selectors.backButton).click();
    cy.url().should('include', urls.sessionUrl);
  });

  it('should delete account when delete button is clicked', () => {
    loginAndVisitMe('nonAdminLoginResponse');

    cy.get(selectors.navAccountButton).click();

    cy.intercept('DELETE', urls.userApi, {});
    cy.get(selectors.deleteAccountButton).click();
    cy.url().should('include', urls.rootUrl);
  });
});
