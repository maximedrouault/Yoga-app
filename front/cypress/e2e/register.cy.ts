describe('Register spec', () => {
  const selectors = {
    firstNameInput: '[data-testid="first-name-input"]',
    lastNameInput: '[data-testid="last-name-input"]',
    emailInput: '[data-testid="email-input"]',
    passwordInput: '[data-testid="password-input"]',
    submitButton: '[data-testid="submit-button"]',
    errorMessage: '[data-testid="error-message"]'
  };

  const urls = {
    registerUrl: '/register',
    registerApi: '/api/auth/register',
    sessionApi: '/api/session',
  }

  it('Register successful', () => {
    cy.visit(urls.registerUrl)

    cy.intercept('POST', urls.registerApi, {
      body: { "message":"User registered successfully!" },
    })

    cy.intercept(
      {
        method: 'GET',
        url: urls.sessionApi,
      },
      [])

    cy.get(selectors.firstNameInput).type("firstName")
    cy.get(selectors.lastNameInput).type("lastName")
    cy.get(selectors.emailInput).type("yoga@studio.com")
    cy.get(selectors.passwordInput).type("test!1234")
    cy.get(selectors.submitButton).click()

    cy.url().should('include', '/login')
  });

  it('Register failed when the email is already taken', () => {
    cy.visit(urls.registerUrl)

    cy.intercept('POST', urls.registerApi, {
      statusCode: 400,
      body: { "message":"Error: Email is already taken!" },
    })

    cy.get(selectors.firstNameInput).type("firstName")
    cy.get(selectors.lastNameInput).type("lastName")
    cy.get(selectors.emailInput).type("alreadyExistUser@studio.com")
    cy.get(selectors.passwordInput).type("test!1234")
    cy.get(selectors.submitButton).click()

    cy.get(selectors.errorMessage).should('contain.text', 'An error occurred')
    cy.url().should('include', urls.registerUrl)
  });

  it('Register failed when the firstName is not correct', () => {
    cy.visit(urls.registerUrl)

    cy.get(selectors.lastNameInput).type("lastName")
    cy.get(selectors.emailInput).type("user@yoga.com")
    cy.get(selectors.passwordInput).type("test!1234")

    cy.get(selectors.submitButton).should('be.disabled').and('contain.text', 'Submit')
    cy.url().should('include', urls.registerUrl)
  });

  it('Register failed when lastName is not correct', () => {
    cy.visit(urls.registerUrl)

    cy.get(selectors.firstNameInput).type("firstName")
    cy.get(selectors.emailInput).type("user@studio.com")
    cy.get(selectors.passwordInput).type("testPassword")

    cy.get(selectors.submitButton).should('be.disabled').and('contain.text', 'Submit')
    cy.url().should('include', urls.registerUrl)
  });

  it('Register failed when email is not correct', () => {
    cy.visit(urls.registerUrl)

    cy.get(selectors.firstNameInput).type("firstName")
    cy.get(selectors.lastNameInput).type("lastName")
    cy.get(selectors.passwordInput).type("testPassword")

    cy.get(selectors.submitButton).should('be.disabled').and('contain.text', 'Submit')
    cy.url().should('include', urls.registerUrl)
  });

  it('Register failed when password is not correct', () => {
    cy.visit(urls.registerUrl)

    cy.get(selectors.firstNameInput).type("firstName")
    cy.get(selectors.lastNameInput).type("lastName")
    cy.get(selectors.emailInput).type("user@studio.com")

    cy.get(selectors.submitButton).should('be.disabled').and('contain.text', 'Submit')
    cy.url().should('include', urls.registerUrl)
  });
});
