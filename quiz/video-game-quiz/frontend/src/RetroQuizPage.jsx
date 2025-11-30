import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function RetroQuizPage() {
  const [question, setQuestion] = useState(null);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);

  useEffect(() => {
    AOS.init({ duration: 1000 });
    loadNextQuestion();
  }, []);

  const loadNextQuestion = () => {
    fetch("http://localhost:8080/api/retro-quiz/next")
      .then(res => {
        if (!res.ok) throw new Error("Błąd HTTP: " + res.status);
        return res.json();
      })
      .then(data => {
        console.log("Pobrane pytanie:", data);
        if (data) {
          setQuestion(data);
        } else {
          setFinished(true); // brak kolejnych pytań
        }
      })
      .catch(err => console.error("Błąd API:", err));
  };

  const handleAnswer = (answer) => {
    if (!question) return;

    const userAnswer = String(answer).trim();
    const correctAnswer = String(question.correctAnswer).trim();

    if (userAnswer.toLowerCase() === correctAnswer.toLowerCase()) {
      setScore(prev => prev + 1);
      loadNextQuestion();
    } else {
      setFinished(true);
    }
  };

  if (finished) {
    return (
      <div className="quiz-category-page-container">
        <h1 className="quiz-category-title">
          Twój wynik: {score}
        </h1>
        <button className="primary-btn" onClick={() => window.location.reload()}>
          Zagraj ponownie
        </button>
      </div>
    );
  }

  if (!question) return <p>Ładowanie pytania...</p>;

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title">Retro Classics Quiz</h1>
      <h2>{question.questionText}</h2>

      <div className="quiz-options">
        {question.options.map((opt, i) => (
          <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
            {opt}
          </button>
        ))}
      </div>
    </div>
  );
}

export default RetroQuizPage;
