// QuizPage.js
import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './QuizPage.css'; // Ensure your CSS is linked

function QuizPage() {
  const navigate = useNavigate();
  const location = useLocation();

  // handleLogout zostało przeniesione do NavBar.js
  // console.log("User logged out!"); zostało usunięte z NavBar.js

  const isActive = (path) => location.pathname === path;

  return (
    <div className="page-wrapper quiz-page-with-navbar-spacing"> {/* Dodaj klasę do odstępu */}
      {/* USUNIĘTO: Top Navigation Bar - teraz jest w NavBar.js */}

      {/* Main content */}
      <div className="quiz-page">
        <h1>Test Your Video Game Knowledge!</h1>
        <p>Dive into different eras and genres, and see how well you know the gaming world. Select a quiz type to begin your challenge!</p>

        <div className="quiz-options">
          <div className="quiz-card" onClick={() => navigate('/quiz/retro')}>
            <img src="/RetroLogo.png" alt="Retro Games Quiz" />
            <h3>Retro Classics</h3>
            <p className="quiz-description">Explore the golden age of gaming with pixelated challenges!</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/latest')}>
            <img src="/NewRealeseLogo.png" alt="Latest Releases" />
            <h3>New Releases</h3>
            <p className="quiz-description">Stay current! Quizzes on the hottest games hitting the market now.</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/images')}>
            <img src="/GuessGameLogo.png" alt="Guess From Image" />
            <h3>Guess the Game</h3>
            <p className="quiz-description">Can you identify the game just from a screenshot or character?</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/genre')}>
            <img src="/GenreLogo.png" alt="Genre Challenge" />
            <h3>Genre Challenge</h3>
            <p className="quiz-description">From RPGs to FPS, master quizzes across diverse game genres.</p>
          </div>
        </div>
      </div>

      {/* Simple Footer */}
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
    </div>
  );
}

export default QuizPage;