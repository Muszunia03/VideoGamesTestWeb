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
  const [comparison, setComparison] = useState(null);

  useEffect(() => {
    AOS.init({ duration: 800, once: false });
    fetchRandomImage();
  }, []);

  const fetchRandomImage = async () => {
    setComparison(null);
    setMessage("");
    setGuess("");
    setSuggestions([]);
    setIsGameOver(false);
    setLives(5);

    const res = await fetch("http://localhost:8080/api/imagequiz/random");
    if (!res.ok) return;
    const data = await res.json();
    const fixed = { ...data, imageUrl: data.imageUrl || data.backgroundImage || data.background_image };
    setCurrentImage(fixed);
  };

  const handleSearch = async (value) => {
    setGuess(value);
    if (!value || value.length === 0) {
      setSuggestions([]);
      return;
    }
    const res = await fetch(`http://localhost:8080/api/imagequiz/search?query=${encodeURIComponent(value)}`);
    if (!res.ok) return;
    const data = await res.json();
    setSuggestions(data || []);
  };

  const handleGuess = async (title) => {
    if (isGameOver || !currentImage) return;

    const payload = { guess: title, correct: currentImage.title };
    const res = await fetch("http://localhost:8080/api/imagequiz/compare", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });
    if (!res.ok) {
      setMessage("Server error");
      return;
    }
    const comp = await res.json();
    setComparison(comp);
    setGuess("");
    setSuggestions([]);

    if (comp.titleStatus === "unknown") {
      setMessage("That title is not in the database.");
      return;
    }

    if (comp.titleStatus === "green") {
      setMessage("✅ Correct! Proceed to next image.");
      setScore(prev => prev + 1);
      setIsGameOver(true);
      return;
    }

    const newLives = lives - 1;
    setLives(newLives);

    if (newLives <= 0) {
      setMessage(`❌ Out of attempts! Correct answer: ${currentImage.title}`);
      setIsGameOver(true);

      // TU dodajemy zapis wyniku do bazy po utracie wszystkich żyć
      const username = localStorage.getItem("username");
      if (username) {
        try {
          await fetch("http://localhost:8080/api/results/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              username,
              score,
              quizType: "Guess the Image"
            }),
          });
          console.log("Wynik zapisany do bazy!");
        } catch (err) {
          console.error("Błąd zapisu wyniku:", err);
        }
      }

    } else {
      setMessage(`❌ Wrong guess. ${newLives} attempts left.`);
    }
  };

  const renderBox = (label, status, value, extraClass) => {
    let cls = "red";
    let desc = "";

    switch(status) {
      case "green": cls = "green"; desc = "✅ Correct"; break;
      case "yellow": cls = "yellow"; desc = "⚠️ Partially correct"; break;
      case "red": cls = "red"; desc = "❌ Wrong"; break;
      case "lower": cls = "red"; desc = "⬇ Your guess is lower"; break;
      case "higher": cls = "red"; desc = "⬆ Your guess is higher"; break;
      default: cls = ""; desc = "";
    }

    return (
      <div className={`comp-box ${cls} ${extraClass || ""}`}>
        <div className="comp-label">{label}</div>
        <div className="comp-value">{Array.isArray(value) ? (value || []).join(", ") : (value ?? "-")}</div>
        {(status === "lower" || status === "higher") && <div className="comp-arrow">{status === "lower" ? "⬇" : "⬆"}</div>}
        {desc && <div className="comp-desc">{desc}</div>}
      </div>
    );
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
            onError={(e) => { e.target.src = "/fallback.png"; }}
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
              <li key={idx} onClick={() => handleGuess(title)}>{title}</li>
            ))}
          </ul>
        )}
      </div>

      {message && <p className="result-message">{message}</p>}

      {comparison && comparison.titleStatus && comparison.titleStatus !== "unknown" && (
        <div className="comparison-grid" data-aos="fade-up">
          {renderBox("Title", comparison.titleStatus, comparison.guessedGame?.title)}
          {renderBox("Genres", comparison.genresStatus, comparison.guessedGame?.genres)}
          {renderBox("Platforms", comparison.platformsStatus, comparison.guessedGame?.platforms)}
          {renderBox("Rating", comparison.ratingStatus, comparison.guessedGame?.rating)}
          {renderBox("Release year", comparison.yearStatus, comparison.guessedGame?.releaseYear)}
        </div>
      )}

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
