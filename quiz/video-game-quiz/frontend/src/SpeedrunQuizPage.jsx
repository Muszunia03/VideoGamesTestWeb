import React, { useEffect } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function SpeedrunQuizPage() {
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Speedrun Challenge
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        How many games can you guess in 60 seconds? Test your speed!
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p>This is the dedicated page for the **Speedrun** quiz.</p>
        <p>It will feature a timer, rapid-fire questions, and a focus on quick answers.</p>
        <p>Stay tuned for the full experience!</p>
        <button className="primary-btn" onClick={() => window.history.back()} data-aos="zoom-in" data-aos-delay="600">
          Go Back to Quiz Selection
        </button>
      </div>
      {/* Tu będzie konkretna logika quizu Speedrun */}
    </div>
  );
}

export default SpeedrunQuizPage;