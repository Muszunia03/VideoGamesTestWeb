import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function LatestQuizPage() {
  const [questions, setQuestions] = useState([]);
  const [current, setCurrent] = useState(0);
  const [score, setScore] = useState(0);
  const [finished, setFinished] = useState(false);

  useEffect(() => {
    AOS.init({ duration: 1000 });

    fetch("http://localhost:8080/api/new-releases/start")
      .then(res => {
        if (!res.ok) throw new Error("HTTP error " + res.status);
        return res.json();
      })
      .then(data => {
        console.log("Dane quizu Latest Releases:", data);
        setQuestions(data);
      })
      .catch(err => console.error("Błąd API:", err));
  }, []);

  useEffect(() => {
    if (finished) {
      const saveResult = async () => {
        const resultData = {
          userId: 1, 
          gameId: questions[current]?.gameId || null,
          score: score,
          timeTakenSeconds: 120
        };

        try {
          const res = await fetch("http://localhost:8080/api/results/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(resultData),
          });

          const responseText = await res.text();
          console.log("Wynik zapisany:", responseText);
        } catch (err) {
          console.error("Błąd zapisu wyniku:", err);
        }
      };

      saveResult();
    }
  }, [finished]);

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
      <h1 className="quiz-category-title">Twój wynik: {score}/{questions.length}</h1>
    <button className="primary-btn" onClick={() => window.location.reload()}>
      Zagraj ponownie
     </button>
     </div>
    );
   }

  if (questions.length === 0) {
    return <p>Ładowanie pytania...</p>;
  }

  const q = questions[current];

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Latest Releases Quiz
      </h1>
      <h2 data-aos="fade-up">{q.questionText}</h2>

      <div className="quiz-options" data-aos="zoom-in" data-aos-delay="200">
        {q.options.map((opt, i) => (
          <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
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

export default LatestQuizPage;
