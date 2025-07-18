import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function DailyQuizPage() {
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Daily Guess Challenge
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        Can you guess today's secret game? A new challenge every day!
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p>This is the dedicated page for the **Daily Guess** quiz.</p>
        <p>This quiz will have a unique, daily changing challenge and specific rules.</p>
        <p>Stay tuned for the full experience!</p>
        <button className="primary-btn" onClick={() => window.history.back()} data-aos="zoom-in" data-aos-delay="600">
          Go Back to Quiz Selection
        </button>
      </div>
      {/* Tu będzie konkretna logika quizu Daily Guess */}
    </div>
  );
}

export default DailyQuizPage;