describe('Login spec', () => {
  const loginUrl = '/login';
  const loginApi = '/api/auth/login';
  const sessionApi = '/api/session';
  const emailInput = '[data-testid="email-input"]';
  const passwordInput = '[data-testid="password-input"]';
  const submitButton = '[data-testid="submit-button"]';
  const errorMessage = '[data-testid="error-message"]';

  it('Login successful', () => {
    cy.visit(loginUrl)

    cy.intercept('POST', loginApi, {
      body: { id: 1, username: 'userName', firstName: 'firstName', lastName: 'lastName', admin: true },
    })

    cy.intercept(
      {
        method: 'GET',
        url: sessionApi,
      },
      [])

    cy.get(emailInput).type("yoga@studio.com")
    cy.get(passwordInput).type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  });

  it('Login failed when email is not correct', () => {
    cy.visit(loginUrl)

    cy.intercept('POST', loginApi, {
      statusCode: 401,
      body: {
        path: loginApi,
        error: "Unauthorized",
        message: "Bad credentials",
        status: 401
      },
    })

    cy.get(emailInput).type("unknownUser@yoga.com")
    cy.get(passwordInput).type("test!1234")

    cy.get(submitButton).should('be.visible').and('contain.text', 'Submit')
    cy.get(submitButton).click()
    cy.get(errorMessage).should('be.visible').and("contain.text", 'An error occurred')
    cy.url().should('include', loginUrl)
  });

  it('Login failed when password is not correct', () => {
    cy.visit(loginUrl)

    cy.intercept('POST', loginApi, {
      statusCode: 401,
      body: {
        path: loginApi,
        error: "Unauthorized",
        message: "Bad credentials",
        status: 401
      },
    })

    cy.get(emailInput).type("yoga@studio.com")
    cy.get(passwordInput).type("wrongPassword")

    cy.get(submitButton).should('be.visible').and('contain.text', 'Submit')
    cy.get(submitButton).click()
    cy.get(errorMessage).should('be.visible').and("contain.text", 'An error occurred')
    cy.url().should('include', loginUrl)
  });

  it('should display email field in error when email is in wrong format', () => {
    cy.visit(loginUrl)

    cy.get(emailInput).type("wrongEmailFormat")
    cy.get(passwordInput).type("test!1234")

    cy.get(emailInput).should('have.attr', 'aria-invalid', 'true')
    cy.get(submitButton).should('be.disabled').and('contain.text', 'Submit')
    cy.url().should('include', loginUrl)
  });

  it('should display email field in error when email is empty', () => {
    cy.visit(loginUrl)

    cy.get(emailInput).focus()
    cy.get(emailInput).blur()

    cy.get(emailInput).should('have.class', 'ng-touched')
    cy.get(submitButton).should('be.disabled').and('contain.text', 'Submit')
  });

  it('should display password field in error when password is empty', () => {
    cy.visit(loginUrl)

    cy.get(passwordInput).focus()
    cy.get(passwordInput).blur()

    cy.get(passwordInput).should('have.class', 'ng-touched')
    cy.get(submitButton).should('be.disabled').and('contain.text', 'Submit')
    cy.url().should('include', loginUrl)
  });
});
