import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect, it } from '@jest/globals';

import { NotFoundComponent } from './not-found.component';


describe('NotFoundComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  const selectors = {
    notFoundMessage: '[data-testid="not-found-message"]',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotFoundComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display a message "Page not found !"', () => {
    const messageElement: HTMLElement = fixture.nativeElement.querySelector(selectors.notFoundMessage);

    expect(messageElement?.textContent).toContain("Page not found !");
  });
});
