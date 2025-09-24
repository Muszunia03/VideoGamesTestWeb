import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './QuizPage.css';

function QuizPage() {
  const navigate = useNavigate();
  const location = useLocation();
  const isActive = (path) => location.pathname === path;

  return (
    <div className="page-wrapper quiz-page-with-navbar-spacing"> 

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
            <h3>Guess the Game from Image</h3>
            <p className="quiz-description">Can you identify the game just from a screenshot or character?</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/genre')}>
            <img src="/GenreLogo.png" alt="Genre Challenge" />
            <h3>Genre Challenge</h3>
            <p className="quiz-description">From RPGs to FPS, master quizzes across diverse game genres.</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/daily')}>
            <img src="/DailyLogo.png" alt="Genre Challenge" />
            <h3>Daily Guess</h3>
            <p className="quiz-description">Guess todays secret game.</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/speedrun')}>
            <img src="/SpeedrunLogo.png" alt="Genre Challenge" />
            <h3>Speedrun</h3>
            <p className="quiz-description">Guess as many games as possible in 60 seconds.</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/platformmatch')}>
            <img src="/PlatformLogo.png" alt="Genre Challenge" />
            <h3>Platform Match</h3>
            <p className="quiz-description">Match the platforms for the given game</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/rating')}>
            <img src="/RatingLogo.png" alt="Genre Challenge" />
            <h3>Rating Estimator</h3>
            <p className="quiz-description">Guess if the game had rating above or below a given value</p>
          </div>
          <div className="quiz-card" onClick={() => navigate('/quiz/multifact')}>
            <img src="/GuessEvery.png" alt="Genre Challenge" />
            <h3>Multi-Fact Mix</h3>
            <p className="quiz-description">Guess the game without any restrictions</p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default QuizPage;