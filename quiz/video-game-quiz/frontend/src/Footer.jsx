import React from 'react';
import './Footer.css'; // Będziemy potrzebować osobnego pliku CSS dla stopki

function Footer() {
  return (
    <footer className="footer">
      <p>&copy; 2025 GameQuiz Central. All rights reserved.</p>
      <div className="social-links">
        <a href="https://www.facebook.com" target="_blank" rel="noopener noreferrer" aria-label="Facebook">
          <span className="icon">📘</span>
        </a>
        <a href="https://twitter.com" target="_blank" rel="noopener noreferrer" aria-label="Twitter">
          <span className="icon">🐦</span>
        </a>
        <a href="https://www.instagram.com" target="_blank" rel="noopener noreferrer" aria-label="Instagram">
          <span className="icon">📸</span>
        </a>
      </div>
    </footer>
  );
}

export default Footer;