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
        <div className="hero-content" data-aos="fade-right">
          <h1 className="site-main-title">GameQuiz Central</h1>
          <p className="site-welcome-message">
            Welcome to the ultimate video game quiz! Test your knowledge across eras and genres.
            Challenge yourself, learn facts, and climb leaderboards to become the GameQuiz Master!
          </p>
        </div>
        <div className="hero-image" data-aos="fade-left"> 
          <img src="/CentralLogo.png" alt="Gaming Setup" />
        </div>
      </div>

      <section className="features-section">
        <h2 data-aos="fade-up">What We Offer</h2> 
        <div className="features-grid">
          <div className="feature-card" data-aos="fade-up" data-aos-delay="100"> 
            <h3>Diverse Quizzes</h3>
            <p>From retro arcade classics to modern AAA blockbusters, explore quizzes on every gaming era.</p>
          </div>
          <div className="feature-card" data-aos="fade-up" data-aos-delay="200">
            <h3>Learn & Grow</h3>
            <p>Discover new facts and expand your gaming knowledge with every question you answer.</p>
          </div>
          <div className="feature-card" data-aos="fade-up" data-aos-delay="300">
            <h3>Leaderboards</h3>
            <p>Track your progress, earn achievements, and climb the ranks to become a top player.</p>
          </div>
        </div>
      </section>

      <section className="testimonials-section">
        <h2 data-aos="fade-up">What Our Players Say</h2>
        <div className="testimonial-grid">
          <div className="testimonial-card" data-aos="zoom-in" data-aos-delay="100">
            <p>"GameQuiz Central is addictive! I love challenging myself with new game trivia."</p>
            <p className="testimonial-author">- GamerPro_99</p>
          </div>
          <div className="testimonial-card" data-aos="zoom-in" data-aos-delay="200">
            <p>"Finally, a quiz site that truly understands gamers. The variety is amazing!"</p>
            <p className="testimonial-author">- PixelPioneer</p>
          </div>
          <div className="testimonial-card" data-aos="zoom-in" data-aos-delay="300">
            <p>"My go-to for quick fun and learning about games. Highly recommend!"</p>
            <p className="testimonial-author">- RetroKing</p>
          </div>
        </div>
      </section>

      <section className="call-to-action-section" data-aos="fade-up">
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