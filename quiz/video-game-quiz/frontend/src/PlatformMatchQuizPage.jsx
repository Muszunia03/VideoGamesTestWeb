import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function PlatformMatchQuizPage() {
    const [question, setQuestion] = useState(null);
    const [score, setScore] = useState(0);
    const [finished, setFinished] = useState(false);
    const [typedAnswer, setTypedAnswer] = useState("");

    useEffect(() => {
        AOS.init({ duration: 1000 });
        loadNextQuestion();
    }, []);

    const loadNextQuestion = () => {
        fetch("http://localhost:8080/api/platform-match/next")
            .then(res => res.json())
            .then(data => setQuestion(data))
            .catch(err => console.error("API error:", err));
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
                            quizType: "Platform Match",
                        }),
                    });
                } catch (err) {
                    console.error("Error saving result:", err);
                }
            }

            return;
        }

        setScore(prev => prev + 1);
        setTypedAnswer("");
        loadNextQuestion();
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
                    style={{ marginTop: '40px', fontStyle: 'italic', color: '#555' }}
                >
                    In this quiz, you must identify the correct platform information
                    for each video game. One mistake and the game ends — good luck!
                </div>
            </div>
        );
    }

    return (
        <div className="quiz-category-page-container">
            <h1 className="quiz-category-title" data-aos="fade-down">
                Platform Match Quiz
            </h1>

            <h2 data-aos="fade-up">{question.questionText}</h2>

            <div className="quiz-options" data-aos="zoom-in" data-aos-delay="200">
                {/* CASE 1: MULTIPLE CHOICE */}
                {question.options && question.options.length > 0 && (
                    question.options.map((opt, i) => (
                        <button
                            key={i}
                            className="primary-btn"
                            onClick={() => handleAnswer(opt)}
                        >
                            {opt}
                        </button>
                    ))
                )}

                {/* CASE 2: INPUT BOX TEMPLATE */}
                {question.options && question.options.length === 0 && (
                    <div style={{ marginTop: "20px" }}>
                        <input
                            type="text"
                            value={typedAnswer}
                            placeholder="Type your answer..."
                            onChange={(e) => setTypedAnswer(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === "Enter") {
                                    handleAnswer(typedAnswer);
                                }
                            }}
                            className="quiz-input"
                        />
                        <button
                            className="primary-btn"
                            style={{ marginLeft: "10px" }}
                            onClick={() => handleAnswer(typedAnswer)}
                        >
                            Submit
                        </button>
                    </div>
                )}
            </div>

            <p data-aos="fade-up" data-aos-delay="300">
                Current score: {score}
            </p>

            <div
                className="quiz-description"
                style={{ marginTop: '40px', fontStyle: 'italic', color: '#555' }}
            >
                In this quiz, you must identify the correct debut platform,
                platform count, exclusivity, or advanced platform details of
                classic games. The quiz continues as long as you keep answering
                correctly — aim for the highest score possible!
            </div>
        </div>
    );
}

export default PlatformMatchQuizPage;
