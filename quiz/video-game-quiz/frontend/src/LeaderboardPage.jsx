import React, { useEffect, useState } from 'react';
import './LeaderboardPage.css';
import AOS from 'aos'; 
import 'aos/dist/aos.css';

function LeaderboardPage() {
  const [leaderboardData, setLeaderboardData] = useState([]);

  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh(); 

    fetch("http://localhost:8090/leaderboard") 
      .then((res) => res.json())
      .then((data) => {
        setLeaderboardData(data);
      })
      .catch((err) => console.error("Error fetching leaderboard:", err));
  }, []);

  return (
    <div className="leaderboard-page-container">
      <h1 className="leaderboard-main-title" data-aos="fade-down">Global Leaderboard</h1>
      <p className="leaderboard-description" data-aos="fade-up" data-aos-delay="200">
        See who stands at the top of the GameQuiz Central challenge!
      </p>

      <div className="leaderboard-grid-wrapper">
        <div className="leaderboard-grid" data-aos="fade-in" data-aos-delay="400">
          <div className="leaderboard-header">
            <span className="header-rank">Rank</span>
            <span className="header-username">Player</span>
            <span className="header-score">Score</span>
            <span className="header-quizzes">Quizzes</span>
          </div>

          {leaderboardData.map((player, index) => (
            <div 
              key={index} 
              className={`leaderboard-row ${index < 3 ? 'top-player' : ''}`}
              data-aos="fade-up" 
              data-aos-delay={400 + (index * 50)} 
            >
              <span className="player-rank">{index + 1}</span>
              <span className="player-username">{player.username}</span>
              <span className="player-score">{player.score}</span>
              <span className="player-quizzes">{player.quizzesCompleted}</span>
            </div>
          ))}
        </div>
      </div>

      <p className="leaderboard-footer-note" data-aos="fade-up" data-aos-delay="1000">
        Challenge more quizzes to climb higher!
      </p>
    </div>
  );
}

export default LeaderboardPage;
