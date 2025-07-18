import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function ImagesQuizPage() {
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Guess the Game Quiz
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        Can you identify the game just from a screenshot or character?
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p>This is the dedicated page for the **Guess the Game** quiz.</p>
        <p>Expect image-based questions and a unique interface for visual recognition challenges.</p>
        <p>Stay tuned for the full experience!</p>
        <button className="primary-btn" onClick={() => window.history.back()} data-aos="zoom-in" data-aos-delay="600">
          Go Back to Quiz Selection
        </button>
      </div>
      {/* Tu będzie konkretna logika quizu Guess the Game */}
    </div>
  );
}

export default ImagesQuizPage;