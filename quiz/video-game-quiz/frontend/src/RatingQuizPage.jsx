import React, { useEffect, useState } from "react";
import AOS from "aos";
import "aos/dist/aos.css";
import "./QuizCategoryPage.css";

function RatingQuizPage() {
  const [questions, setQuestions] = useState([]);
  const [current, setCurrent] = useState(0);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    AOS.init({ duration: 1000 });

    fetch("http://localhost:8080/api/rating-estimator/start")
      .then((res) => {
        if (!res.ok) throw new Error("Błąd HTTP: " + res.status);
        return res.json();
      })
      .then((data) => {
        console.log("Dane z backendu:", data);
        setQuestions(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Błąd API:", err);
        setLoading(false);
      });
  }, []);

  const handleAnswer = (answer) => {
    if (answer === questions[current].correctAnswer) {
      setScore((prev) => prev + 1);
    }

    if (current + 1 < questions.length) {
      setCurrent((prev) => prev + 1);
    } else {
      setFinished(true);
      saveResult();
    }
  };

  const saveResult = () => {
    const result = {
      userId: 1, // na razie testowo
      gameId: questions[current]?.gameId || -1,
      score: score,
      timeTakenSeconds: 0,
    };

    fetch("http://localhost:8080/api/rating-estimator/save", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(result),
    })
      .then((res) => res.text())
      .then((msg) => console.log("Zapis wyniku:", msg))
      .catch((err) => console.error("Błąd zapisu:", err));
  };

  if (loading) {
    return <p>Ładowanie pytań...</p>;
  }

  if (finished) {
    return (
      <div className="quiz-category-page-container">
        <h1 className="quiz-category-title" >Twój wynik: {score}/{questions.length}</h1>
        <button
          className="primary-btn"
          onClick={() => window.location.reload()}
          data-aos="zoom-in"
        >
          Zagraj ponownie
        </button>
      </div>
    );
  }

  if (questions.length === 0) {
    return <p>Brak pytań do wyświetlenia.</p>;
  }

  const q = questions[current];

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Rating Estimator Quiz
      </h1>

      <h2 data-aos="fade-up">{q.questionText}</h2>

      <div className="quiz-options" data-aos="fade-in">
        {q.options.map((opt, i) => (
          <button
            key={i}
            className="primary-btn"
            onClick={() => handleAnswer(opt)}
          >
            {opt}
          </button>
        ))}
      </div>

      <p data-aos="fade-up" data-aos-delay="300">
        Pytanie {current + 1} z {questions.length}
      </p>
    </div>
  );
}

export default RatingQuizPage;
