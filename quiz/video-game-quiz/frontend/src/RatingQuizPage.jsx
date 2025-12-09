import React, { useEffect, useState } from "react";
import AOS from "aos";
import "aos/dist/aos.css";
import "./QuizCategoryPage.css";

function RatingQuizPage() {
  const [question, setQuestion] = useState(null);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);
  const [inputValue, setInputValue] = useState(""); // dla input box

  useEffect(() => {
    AOS.init({ duration: 1000 });
    loadNextQuestion();
  }, []);

  const loadNextQuestion = () => {
    fetch("http://localhost:8080/api/rating-estimator/next")
      .then((res) => res.json())
      .then((data) => {
        setQuestion(data);
        setInputValue(""); // reset inputu przy nowym pytaniu
      })
      .catch((err) => console.error("API error:", err));
  };

  const handleAnswer = async (answer) => {
    if (!question) return;

    const isCorrect =
      answer.trim().toLowerCase() === question.correctAnswer.trim().toLowerCase();

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
              quizType: "Rating Estimator",
            }),
          });
        } catch (err) {
          console.error("Błąd zapisu:", err);
        }
      }

      return;
    }

    setScore((prev) => prev + 1);
    loadNextQuestion();
  };

  const handleInputSubmit = () => {
    handleAnswer(inputValue);
  };

  if (!question && !finished) return <p>Ładowanie pytania...</p>;

  if (finished) {
    return (
      <div className="quiz-category-page-container">
        <h1 className="quiz-category-title">Twój wynik: {score}</h1>
        <button
          className="primary-btn"
          onClick={() => window.location.reload()}
          data-aos="zoom-in"
        >
          Zagraj ponownie
        </button>

        <div
          className="quiz-description"
          style={{ marginTop: "40px", fontStyle: "italic", color: "#555" }}
        >
          In this quiz you estimate the rating of different games.
          The quiz continues as long as your answers are correct — how far can you go?
        </div>
      </div>
    );
  }

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Rating Estimator Quiz
      </h1>

      <h2 data-aos="fade-up">{question.questionText}</h2>

      {question.options.length > 0 ? (
        <div className="quiz-options" data-aos="fade-in">
          {question.options.map((opt, i) => (
            <button
              key={i}
              className="primary-btn"
              onClick={() => handleAnswer(opt)}
            >
              {opt}
            </button>
          ))}
        </div>
      ) : (
        <div className="quiz-options" data-aos="fade-in">
          <input
            type="text"
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            placeholder="Wpisz odpowiedź..."
            className="input-box"
          />
          <button
            className="primary-btn"
            onClick={handleInputSubmit}
            style={{ marginLeft: "10px" }}
          >
            Submit
          </button>
        </div>
      )}

      <p data-aos="fade-up" data-aos-delay="300">
        Current score: {score}
      </p>

      <div
        className="quiz-description"
        style={{ marginTop: "40px", fontStyle: "italic", color: "#555" }}
      >
      <div className="quiz-description" style={{ marginTop: '40px', fontStyle: 'italic', color: '#555' }}>
        In this quiz, you answer questions about video games quiz ratings.
        The quiz continues as long as you answer correctly — try to score as many points as possible!
      </div>
      </div>
    </div>
  );
}

export default RatingQuizPage;
