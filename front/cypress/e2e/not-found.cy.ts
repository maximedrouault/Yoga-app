describe('Not Found spec', () => {
  it('should display a 404 page', () => {
    cy.visit('/this-page-does-not-exist');

    cy.url().should('include', '/404');
    cy.contains('Page not found');
  });
});
