import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css'; 

function RetroQuizPage() {
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Retro Classics Quiz
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        Get ready to test your knowledge on the golden age of gaming!
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p>This is the dedicated page for **Retro Classics** quiz logic.</p>
        <p>Here you will find unique questions, scoring, and UI elements related to retro games.</p>
        <p>Stay tuned for the full experience!</p>
        <button className="primary-btn" onClick={() => window.history.back()} data-aos="zoom-in" data-aos-delay="600">
          Go Back to Quiz Selection
        </button>
      </div>
      {/* Tu będzie konkretna logika quizu Retro */}
    </div>
  );
}

export default RetroQuizPage;