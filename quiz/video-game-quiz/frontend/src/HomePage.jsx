import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
  const navigate = useNavigate();

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const userToken = localStorage.getItem('authToken');
    if (userToken) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false);
    }
  }, []);

  return (
    <div className="home-page-container">
      <div className="home-hero-section">
        <div className="hero-content">
          <h1 className="site-main-title">GameQuiz Central</h1>
          <p className="site-welcome-message">
            Welcome to the ultimate video game quiz! Test your knowledge across eras and genres.
          </p>
        </div>
        <div className="hero-image">
          <img src="/CentralLogo.png" alt="Gaming Setup" />
        </div>
      </div>

      <section className="call-to-action-section">
        <h2>Ready to Test Your Skills?</h2>
        {!isLoggedIn && (
          <button className="primary-btn" onClick={() => navigate('/signup')}>
            Join the Challenge!
          </button>
        )}
      </section>
    </div>
  );
}

export default HomePage;