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
        if (data) {
          setQuestion(data);
        } else {
          setFinished(true);
        }
      })
      .catch(err => console.error("Błąd API:", err));
  };

  const handleAnswer = async (answer) => {
    if (!question) return;

    const userAnswer = String(answer).trim();
    const correctAnswer = String(question.correctAnswer).trim();

    if (userAnswer.toLowerCase() === correctAnswer.toLowerCase()) {
      setScore(prev => prev + 1);
      loadNextQuestion();
    } else {
      const username = localStorage.getItem("username");

      if (username) {
        try {
          await fetch("http://localhost:8080/api/results/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              username: username,
              score: score,
              quizType: "Retro Classics"
            }),
          });
        } catch (err) {
          console.error(err);
        }
      }

      setFinished(true);
    }
  };

  if (finished) {
    return (
      <div className="quiz-category-page-container">
        <h1 className="quiz-category-title">
          Your score: {score}
        </h1>
        <button className="primary-btn" onClick={() => window.location.reload()}>
          Play again
        </button>
      </div>
    );
  }

  if (!question) return <p>Loading question...</p>;

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">Retro Classics Quiz</h1>
      <h2 data-aos="fade-up">{question.questionText}</h2>

      <div className="quiz-options" data-aos="zoom-in" data-aos-delay="200">
        {question.options.map((opt, i) => (
          <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
            {opt}
          </button>
        ))}
      </div>

      <p data-aos="fade-up" data-aos-delay="300" style={{ marginTop: '20px' }}>
         Current score: {score}
      </p>
      <div className="quiz-description" style={{ marginTop: '40px', fontStyle: 'italic', color: '#555' }}>
        In this quiz, you answer questions about video games released before 2000.
        The quiz continues as long as you answer correctly — try to score as many points as possible!
      </div>
    </div>
  );
}

export default RetroQuizPage;