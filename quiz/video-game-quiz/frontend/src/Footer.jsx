// src/Footer.jsx
import React from 'react';
import { Link } from 'react-router-dom'; // Importuj Link do nawigacji
import './Footer.css';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
        <div className="footer-section footer-info">
          <h3 className="footer-heading">GameQuiz Central</h3>
          <p>&copy; {new Date().getFullYear()} GameQuiz Central. All rights reserved.</p>
          <p>Test your gaming knowledge and challenge your friends!</p>
        </div>

        <div className="footer-section footer-links">
          <h3 className="footer-heading">Quick Links</h3>
          <ul>
            <li><Link to="/quiz">All Quizzes</Link></li>
            <li><Link to="/leaderboard">Leaderboard</Link></li>
            <li><Link to="/profile">My Profile</Link></li>
            <li><Link to="/regulations">Regulations</Link></li> {/* Odwołanie do regulaminu */}
            {/* Dodaj więcej linków, jeśli potrzebujesz (np. Contact Us, Privacy Policy) */}
          </ul>
        </div>

        <div className="footer-section footer-social">
          <h3 className="footer-heading">Connect With Us</h3>
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
          <p className="footer-email">support@gamequizcentral.com</p>
        </div>
      </div>
    </footer>
  );
}

export default Footer;