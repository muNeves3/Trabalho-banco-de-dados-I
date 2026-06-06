import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

test('renders login title', () => {
  render(<App />);
  const titleElement = screen.getByText(/login/i);
  expect(titleElement).toBeInTheDocument();
});
