import React, { useEffect, useState } from 'react';
import AOS from 'aos';
import 'aos/dist/aos.css';
import './QuizCategoryPage.css';

function SpeedrunQuizPage() {
  const [questions, setQuestions] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState('');
  const [score, setScore] = useState(0);
  const [quizFinished, setQuizFinished] = useState(false);

  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();

    // Poprawiony endpoint
    fetch('http://localhost:8080/api/speedrun/start?count=10') 
      .then((res) => res.json())
      .then((data) => setQuestions(data))
      .catch((err) => console.error('Error fetching questions:', err));
  }, []);

  if (questions.length === 0) {
    return <p>Loading questions...</p>;
  }

  const currentQuestion = questions[currentIndex];

  const handleAnswer = (answer) => {
    setSelectedAnswer(answer);

    if (answer === currentQuestion.correctAnswer) {
      setScore(score + 1);
    }

    if (currentIndex + 1 < questions.length) {
      setCurrentIndex(currentIndex + 1);
      setSelectedAnswer('');
    } else {
      setQuizFinished(true);
    }
  };

  if (quizFinished) {
    return (
      <div className="quiz-category-page-container">
        <h1>Speedrun Finished!</h1>
        <p>
          You scored {score} out of {questions.length}
        </p>
        <button className="primary-btn" onClick={() => window.location.reload()}>
          Restart Quiz
        </button>
      </div>
    );
  }

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title" data-aos="fade-down">
        Speedrun Challenge
      </h1>
      <p className="quiz-category-description" data-aos="fade-up" data-aos-delay="200">
        How many games can you guess in 60 seconds? Test your speed!
      </p>

      <div className="quiz-placeholder-content" data-aos="fade-in" data-aos-delay="400">
        <p><strong>Question {currentIndex + 1}:</strong> {currentQuestion.questionText}</p>

        <div className="options-container">
          {currentQuestion.options?.map((option, idx) => (
            <button
              key={idx}
              className={`option-btn ${selectedAnswer === option ? 'selected' : ''}`}
              onClick={() => handleAnswer(option)}
            >
              {option}
            </button>
          ))}

          {!currentQuestion.options && (
            <button
              className="option-btn"
              onClick={() => handleAnswer(currentQuestion.correctAnswer)}
            >
              Submit Answer
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

export default SpeedrunQuizPage;
