import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function MultiFactQuizPage() {
  const [question, setQuestion] = useState(null);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);
  const [inputValue, setInputValue] = useState("");

  useEffect(() => {
    AOS.init({ duration: 1000 });
    loadNextQuestion();
  }, []);

  const loadNextQuestion = () => {
    fetch("http://localhost:8080/api/multifactmix-quiz/next")
      .then(res => res.json())
      .then(data => {
        setQuestion(data);
        setInputValue("");
      })
      .catch(err => console.error("API error:", err));
  };

  const handleAnswer = async (answer) => {
    if (!question) return;

    const isCorrect = answer.trim().toLowerCase() === question.correctAnswer.trim().toLowerCase();

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
              quizType: "Multi-Fact Mix",
            }),
          });
        } catch (err) {
          console.error("Błąd zapisu:", err);
        }
      }
      return;
    }

    setScore(prev => prev + 1);
    loadNextQuestion();
  };

  const handleInputSubmit = () => {
    handleAnswer(inputValue);
  };

  if (!question && !finished) return <p>Loading question...</p>;

  if (finished) {
    return (
      <div className="quiz-category-page-container">
        <h1 className="quiz-category-title">Your score: {score}</h1>
        <button className="primary-btn" onClick={() => window.location.reload()}>
          Play Again
        </button>
        <div
          className="quiz-description"
          style={{ marginTop: "40px", fontStyle: "italic", color: "#555" }}
        >
          Multi-Fact Mix quiz tests your knowledge across multiple aspects of games.
          Try to keep answering correctly as long as possible!
        </div>
      </div>
    );
  }

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title">Multi-Fact Mix Quiz</h1>
      <h2>{question.questionText}</h2>

      {question.options && question.options.length > 0 ? (
        <div className="quiz-options">
          {question.options.map((opt, i) => (
            <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
              {opt}
            </button>
          ))}
        </div>
      ) : (
        <div className="input-answer-container">
          <input
            type="text"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            placeholder="Wpisz odpowiedź..."
            className="input-field"
            autoFocus
            onKeyDown={(e) => {
              if (e.key === 'Enter') handleInputSubmit();
            }}
          />
          <button className="primary-btn submit-btn" onClick={handleInputSubmit}>
            Submit
          </button>
        </div>
      )}

      <p className="question-counter">Current score: {score}</p>
    </div>
  );
}

export default MultiFactQuizPage;