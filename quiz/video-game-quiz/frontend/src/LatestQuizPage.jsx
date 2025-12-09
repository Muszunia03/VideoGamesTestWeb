import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function LatestQuizPage() {
  const [question, setQuestion] = useState(null);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);

  useEffect(() => {
    AOS.init({ duration: 1000 });
    loadNextQuestion();
  }, []);

  const loadNextQuestion = () => {
    fetch("http://localhost:8080/api/new-releases/next")
      .then(res => res.json())
      .then(data => setQuestion(data))
      .catch(err => console.error("API Error:", err));
  };

  const handleAnswer = async (answer) => {
    const isCorrect = answer === question.correctAnswer;

    if (!isCorrect) {
      const username = localStorage.getItem("username");
      if (username) {
        try {
          await fetch("http://localhost:8080/api/results/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              username,
              score,
              quizType: "Latest Releases"
            }),
          });
        } catch (err) {
          console.error("Error saving result:", err);
        }
      } else {
        console.error("No logged-in user!");
      }

      setFinished(true);
      return;
    }

    setScore(prev => prev + 1);
    loadNextQuestion();
  };

  if (!question && !finished) return <p>Loading question...</p>;

  if (finished) {
    return (
      <div className="quiz-category-page-container">
        <h1 className="quiz-category-title">Your score: {score}</h1>
        <button className="primary-btn" onClick={() => window.location.reload()}>
          Play Again
        </button>
      </div>
    );
  }

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        New Releases Quiz
      </h1>
      <h2 data-aos="fade-up">{question.questionText}</h2>

      <div className="quiz-options" data-aos="zoom-in" data-aos-delay="200">
        {question.options.map((opt, i) => (
          <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
            {opt}
          </button>
        ))}
      </div>

      <p data-aos="fade-up" data-aos-delay="300">
        Current score: {score}
      </p>

      <div className="quiz-description" style={{ marginTop: '40px', fontStyle: 'italic', color: '#555' }}>
        In this quiz, you answer questions about video games released from 2020 onwards.
        The quiz continues as long as you answer correctly — try to score as many points as possible!
      </div>
    </div>
  );
}

export default LatestQuizPage;
