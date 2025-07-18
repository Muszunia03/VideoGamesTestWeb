import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function LatestQuizPage() {
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Latest Releases Quiz
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        Are you up-to-date with the newest games hitting the market?
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p>This is the dedicated page for **Latest Releases** quiz logic.</p>
        <p>It will feature questions about recently launched games and industry trends.</p>
        <p>Stay tuned for the full experience!</p>
        <button className="primary-btn" onClick={() => window.history.back()} data-aos="zoom-in" data-aos-delay="600">
          Go Back to Quiz Selection
        </button>
      </div>
      {/* Tu będzie konkretna logika quizu Latest */}
    </div>
  );
}

export default LatestQuizPage;