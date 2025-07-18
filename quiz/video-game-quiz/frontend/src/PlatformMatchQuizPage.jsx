import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css'; // Używamy wspólnego CSS dla spójnego wyglądu

function PlatformMatchQuizPage() {
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Platform Match Quiz
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        Can you match the platforms for the given game? Test your console knowledge!
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p>This is the dedicated page for the **Platform Match** quiz logic.</p>
        <p>Here you'll find games and need to select the correct platforms they were released on.</p>
        <p>Stay tuned for the full experience!</p>
        <button className="primary-btn" onClick={() => window.history.back()} data-aos="zoom-in" data-aos-delay="600">
          Go Back to Quiz Selection
        </button>
      </div>
      {/* Tu będzie konkretna logika quizu Platform Match */}
    </div>
  );
}

export default PlatformMatchQuizPage;