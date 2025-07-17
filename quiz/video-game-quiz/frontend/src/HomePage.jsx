// src/HomePage.js
import React, { useState, useEffect } from 'react'; // Dodaj useState i useEffect
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
  const navigate = useNavigate();
  
  // PRZYKŁADOWY SPOSÓB NA SPRAWDZENIE STATUSU LOGOWANIA
  // W PRAWDZIWEJ APLIKACJI POBIERZESZ TEN STAN Z KONTEKSTU/REDUX LUB Z LOCALSTORAGE
  const [isLoggedIn, setIsLoggedIn] = useState(false); // Domyślnie niezalogowany

  useEffect(() => {
    // Tutaj możesz sprawdzić, czy użytkownik jest zalogowany
    // Np. odczytać token z LocalStorage lub stan z Context API
    const userToken = localStorage.getItem('authToken'); // Przykład
    if (userToken) {
      setIsLoggedIn(true);
    } else {
      setIsLoggedIn(false);
    }
    // Możesz też subskrybować zmiany w kontekście logowania
    // const unsubscribe = authContext.onLoginStatusChange(status => setIsLoggedIn(status));
    // return () => unsubscribe(); // Cleanup
  }, []); // Pusta tablica zależności oznacza, że efekt uruchomi się raz po zamontowaniu komponentu

  return (
    <div className="home-page-container">
      <div className="home-hero-section">
        <div className="hero-content" data-aos="fade-right"> {/* Animacja dla treści */}
          <h1 className="site-main-title">GameQuiz Central</h1>
          <p className="site-welcome-message">
            Welcome to the ultimate video game quiz! Test your knowledge across eras and genres.
            Challenge yourself, learn facts, and climb leaderboards to become the GameQuiz Master!
          </p>
        </div>
        <div className="hero-image" data-aos="fade-left"> {/* Animacja dla obrazka */}
          <img src="/CentralLogo.png" alt="Gaming Setup" />
        </div>
      </div>

      <section className="features-section">
        <h2 data-aos="fade-up">What We Offer</h2> {/* Animacja dla tytułu sekcji */}
        <div className="features-grid">
          <div className="feature-card" data-aos="fade-up" data-aos-delay="100"> {/* Opóźnienie dla kart */}
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
          <div className="testimonial-card" data-aos="zoom-in" data-aos-delay="100"> {/* Zoom in dla opinii */}
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
        {/* Warunkowe renderowanie przycisku */}
        {!isLoggedIn && ( // Przycisk renderuje się TYLKO, jeśli isLoggedIn jest false
          <button className="primary-btn" onClick={() => navigate('/signup')}>
            Join the Challenge!
          </button>
        )}
      </section>
    </div>
  );
}

export default HomePage;