import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function GenreQuizPage() {
  const [question, setQuestion] = useState(null);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);
  const [inputValue, setInputValue] = useState('');

  useEffect(() => {
    AOS.init({ duration: 1000 });
    loadNextQuestion();
  }, []);

  const loadNextQuestion = () => {
    fetch("http://localhost:8080/api/genre-challenge/next")
      .then(res => res.json())
      .then(data => setQuestion(data))
      .catch(err => console.error("API Error:", err));
  };

  const handleAnswer = async (answer) => {
    if (!question) return;

    const submittedAnswer = String(answer).trim();
    const isCorrect = submittedAnswer.toLowerCase() === question.correctAnswer.toLowerCase();

    if (!isCorrect) {
      setFinished(true);

      const username = localStorage.getItem("username");
      if (username) {
        try {
          await fetch("http://localhost:8080/api/results/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              username,
              score,
              quizType: "Genre Challenge",
            }),
          });
          console.log("Result saved successfully!");
        } catch (err) {
          console.error("Failed to save result:", err);
        }
      }
      return;
    }

    setScore(prev => prev + 1);
    setInputValue('');
    loadNextQuestion();
  };

  const handleInputKey = (e) => {
    if (e.key === 'Enter') {
      handleInputSubmit();
    }
  };

  const handleInputSubmit = () => {
    const value = inputValue.trim();
    if (value) handleAnswer(value);
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
        Genre Challenge Quiz
      </h1>
      <h2 data-aos="fade-up">{question.questionText}</h2>

{(question.templateType === 1 || question.templateType === 2) ? (
  <div className="input-answer-container">
    <input
      type={question.templateType === 2 ? "number" : "text"}
      placeholder={question.templateType === 2 ? "Enter a number..." : "Type your answer..."}
      value={inputValue}
      onChange={(e) => setInputValue(e.target.value)}
      onKeyDown={(e) => e.key === 'Enter' && handleInputSubmit()}
      className="input-field"
    />
    <button
      className="primary-btn submit-btn"
      onClick={handleInputSubmit}
      disabled={!inputValue.trim()}
    >
      Zatwierdź Odpowiedź
    </button>
  </div>
) : (
  question.options && question.options.map((opt, i) => (
    <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
      {opt}
    </button>
  ))
)}

      <p data-aos="fade-up" data-aos-delay="300">
        Current score: {score}
      </p>
      <div className="quiz-description" style={{ marginTop: '40px', fontStyle: 'italic', color: '#555' }}>
        In this quiz, you answer questions about video games quiz genres.
        The quiz continues as long as you answer correctly — try to score as many points as possible!
      </div>
    </div>
  );
}

export default GenreQuizPage;