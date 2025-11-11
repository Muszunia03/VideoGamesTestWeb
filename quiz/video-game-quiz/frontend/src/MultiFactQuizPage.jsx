import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function MultiFactQuizPage() {
  const [questions, setQuestions] = useState([]);
  const [current, setCurrent] = useState(0);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);

  useEffect(() => {
    AOS.init({ duration: 1000 });

    fetch("http://localhost:8080/api/multifactmix-quiz/start")
      .then(res => res.json())
      .then(data => setQuestions(data))
      .catch(err => console.error("API error:", err));
  }, []);

  const handleAnswer = (answer) => {
    if (answer === questions[current].correctAnswer) {
      setScore(prev => prev + 1);
    }

    if (current + 1 < questions.length) {
      setCurrent(prev => prev + 1);
    } else {
      setFinished(true);
    }
  };

  if (finished) {
    return (
      <div className="quiz-category-page-container">
        <h1>Your score: {score}/{questions.length}</h1>
        <button className="primary-btn" onClick={() => window.location.reload()}>
          Play Again
        </button>
      </div>
    );
  }

  if (questions.length === 0) {
    return <p>Loading questions...</p>;
  }

  const q = questions[current];

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title">Multi-Fact Mix Quiz</h1>
      <h2>{q.questionText}</h2>
      <div className="quiz-options">
        {q.options.map((opt, i) => (
          <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
            {opt}
          </button>
        ))}
      </div>
      <p>Question {current + 1} of {questions.length}</p>
    </div>
  );
}

export default MultiFactQuizPage;
