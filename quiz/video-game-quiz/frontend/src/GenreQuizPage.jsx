import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css'; 

function GenreQuizPage() {
    const [questions, setQuestions] = useState([]);
    const [current, setCurrent] = useState(0);
    const [score, setScore] = useState(0);
    const [finished, setFinished] = useState(false);
    const [startTime, setStartTime] = useState(null);
    const [inputValue, setInputValue] = useState('');

    useEffect(() => {
        AOS.init({ duration: 1000 });
        setStartTime(Date.now());

        fetch("http://localhost:8080/api/genre-challenge/start")
            .then(res => {
                if (!res.ok) {
                    throw new Error("HTTP error " + res.status);
                }
                return res.json();
            })
            .then(data => {
                setQuestions(data);
            })
            .catch(err => console.error("Błąd API:", err));
    }, []);

    useEffect(() => {
        if (finished) {
            const durationSeconds = Math.round((Date.now() - startTime) / 1000);

            const saveResult = async () => {
                const lastGameId = questions[current - 1]?.gameId || null;

                const resultData = {
                    userId: 1,
                    gameId: lastGameId,
                    score: score,
                    timeTakenSeconds: durationSeconds
                };

                try {
                    const res = await fetch("http://localhost:8080/api/genre-challenge/save-result", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify(resultData),
                    });

                    await res.text();
                } catch (err) {
                    console.error("Błąd zapisu wyniku:", err);
                }
            };

            saveResult();
        }
    }, [finished, questions, score, startTime, current]);

    const handleAnswer = (answer) => {
        const submittedAnswer = String(answer).trim(); 
        
        if (questions[current] && submittedAnswer === questions[current].correctAnswer) {
            setScore(prev => prev + 1);
        }

        setInputValue(''); 

        if (current + 1 < questions.length) {
            setCurrent(prev => prev + 1);
        } else {
            setFinished(true);
        }
    };

    const handleInputKey = (e) => {
        if (e.key === 'Enter') {
            const value = e.target.value.trim();
            
            if (value) {
                handleAnswer(value);
            }
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
                Genre Challenge Quiz
            </h1>
            <h2 data-aos="fade-up">{q.questionText}</h2>

            <div className="quiz-question-options" data-aos="zoom-in" data-aos-delay="200">
                {q.options && q.options.length > 0 ? (
                    q.options.map((opt, i) => (
                        <button key={i} className="primary-btn" onClick={() => handleAnswer(opt)}>
                            {opt}
                        </button>
                    ))
                ) : (
                    <input
                        type={q.templateType === 2 ? "number" : "text"}
                        placeholder="Wpisz swoją odpowiedź..."
                        value={inputValue}
                        onChange={(e) => setInputValue(e.target.value)}
                        onKeyPress={handleInputKey}
                    />
                )}
            </div>

            <p data-aos="fade-up" data-aos-delay="300">
                Pytanie {current + 1} z {questions.length}
            </p>
        </div>
    );
}

export default GenreQuizPage;