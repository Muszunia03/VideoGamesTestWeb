import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function GenreQuizPage() {
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Genre Challenge Quiz
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        Master quizzes across diverse game genres from RPGs to FPS!
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p>This is the dedicated page for the **Genre Challenge** quiz.</p>
        <p>It will allow you to select a specific genre or tackle a mixed-genre challenge.</p>
        <p>Stay tuned for the full experience!</p>
        <button className="primary-btn" onClick={() => window.history.back()} data-aos="zoom-in" data-aos-delay="600">
          Go Back to Quiz Selection
        </button>
      </div>
      {/* Tu będzie konkretna logika quizu Genre Challenge */}
    </div>
  );
}

export default GenreQuizPage;