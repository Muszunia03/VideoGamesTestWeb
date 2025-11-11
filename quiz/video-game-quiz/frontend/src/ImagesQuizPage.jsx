import React, { useEffect, useState } from "react";
import AOS from "aos";
import "aos/dist/aos.css";
import "./QuizCategoryPage.css";

function ImagesQuizPage() {
  const [currentImage, setCurrentImage] = useState(null);
  const [guess, setGuess] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [message, setMessage] = useState("");
  const [lives, setLives] = useState(5);
  const [isGameOver, setIsGameOver] = useState(false);
  const [score, setScore] = useState(0);

  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
    fetchRandomImage();
  }, []);

  const fetchRandomImage = async () => {
    const res = await fetch("http://localhost:8080/api/imagequiz/random");
    if (!res.ok) return;
    const data = await res.json();
    setCurrentImage(data);
    setMessage("");
    setLives(5);
    setIsGameOver(false);
  };

  const handleSearch = async (value) => {
    setGuess(value);
    if (value.length > 0) {
      const res = await fetch(`http://localhost:8080/api/imagequiz/search?query=${value}`);
      const data = await res.json();
      setSuggestions(data);
    } else {
      setSuggestions([]);
    }
  };

  const handleGuess = async (title) => {
    if (isGameOver) return;

    const res = await fetch("http://localhost:8080/api/imagequiz/check", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ guess: title, correct: currentImage.title })
    });
    const result = await res.json();

    setGuess("");
    setSuggestions([]);

    if (result.correct) {
      setMessage("✅ Correct Answer! Proceed to the next game.");
      setIsGameOver(true);
      setScore(prevScore => prevScore + 1);
    } else {
      const newLives = lives - 1;
      setLives(newLives);

      if (newLives <= 0) {
        setMessage(`❌ Out of attempts! The correct answer was: ${currentImage.title}`);
        setIsGameOver(true);
        setScore(0);
      } else {
        setMessage(`❌ Wrong guess! You have ${newLives} attempts left.`);
      }
    }
  };

  return (
    <div className="quiz-category-page-container">
      <h1 className="quiz-category-title">Guess the Game</h1>

      <div className="lives-counter-and-score" data-aos="fade-down">
        <span className="score-display">🏆 Score: {score}</span>
        <span className="lives-display">❤️ Lives: {lives} / 5</span>
      </div>

      {currentImage && (
        <div className="image-container" data-aos="zoom-in">
          <img
            src={currentImage.imageUrl}
            alt="Game"
            className="quiz-image"
          />
        </div>
      )}

      <div className="guess-box" data-aos="fade-up">
        <input
          type="text"
          value={guess}
          onChange={(e) => handleSearch(e.target.value)}
          placeholder={isGameOver ? "Game Over. Click Next." : "Type a game title..."}
          className="guess-input"
          disabled={isGameOver}
        />

        {suggestions.length > 0 && !isGameOver && (
          <ul className="suggestions-list">
            {suggestions.map((title, idx) => (
              <li key={idx} onClick={() => handleGuess(title)}>
                {title}
              </li>
            ))}
          </ul>
        )}
      </div>

      {message && <p className="result-message">{message}</p>}

      {(isGameOver || lives <= 0) && (
        <button
          className="primary-btn mt-4" 
          onClick={fetchRandomImage}
          data-aos="fade-up"
          data-aos-delay="400"
        >
          Next Image
        </button>
      )}

    </div>
  );
}

export default ImagesQuizPage;