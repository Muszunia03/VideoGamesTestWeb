import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function RetroQuizPage() {
  const [questions, setQuestions] = useState([]);
  const [current, setCurrent] = useState(0);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);

  useEffect(() => {
    AOS.init({ duration: 1000 });

    fetch("http://localhost:8080/api/retro-quiz/start") // <- zmienione na /start
      .then(res => {
        if (!res.ok) throw new Error("Błąd HTTP: " + res.status);
        return res.json();
      })
      .then(data => {
        console.log("Dane z backendu:", data); // debug w konsoli
        setQuestions(data);
      })
      .catch(err => console.error("Błąd API:", err));
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
        <h1>Twój wynik: {score}/{questions.length}</h1>
        <button className="primary-btn" onClick={() => window.location.reload()}>Zagraj ponownie</button>
      </div>
    );
  }

  if (questions.length === 0) {
    return <p>Ładowanie pytania...</p>;
  }

  const q = questions[current];

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title">Retro Classics Quiz</h1>
      <h2>{q.questionText}</h2>
      <div className="quiz-options">
        {q.options.map((opt, i) => (
          <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
            {opt}
          </button>
        ))}
      </div>
      <p>Pytanie {current + 1} z {questions.length}</p>
    </div>
  );
}

export default RetroQuizPage;
