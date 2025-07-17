// src/LeaderboardPage.js
import React, { useEffect } from 'react';
import './LeaderboardPage.css'; // Import the new CSS file
import AOS from 'aos'; // Assuming you're using AOS for animations
import 'aos/dist/aos.css';

function LeaderboardPage() {
  // Initialize AOS when the component mounts, if not already done globally
  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh(); // Refresh AOS to ensure animations apply to new content
  }, []);

  // Placeholder data for the leaderboard
  const dummyLeaderboardData = [
    { rank: 1, username: 'GamerPro_99', score: 12500, quizzesCompleted: 50 },
    { rank: 2, username: 'PixelPioneer', score: 11800, quizzesCompleted: 48 },
    { rank: 3, username: 'RetroKing', score: 10500, quizzesCompleted: 45 },
    { rank: 4, username: 'QuizMasterX', score: 9800, quizzesCompleted: 42 },
    { rank: 5, username: 'ArcadeAce', score: 9200, quizzesCompleted: 39 },
    { rank: 6, username: 'ElitePlayer', score: 8750, quizzesCompleted: 37 },
    { rank: 7, username: 'JoystickJedi', score: 8100, quizzesCompleted: 35 },
    { rank: 8, username: 'ConsoleChampion', score: 7500, quizzesCompleted: 32 },
    { rank: 9, username: 'LevelUpLegend', score: 6900, quizzesCompleted: 30 },
    { rank: 10, username: 'HighScorer', score: 6300, quizzesCompleted: 28 },
  ];

  return (
    <div className="leaderboard-page-container">
      <h1 className="leaderboard-main-title" data-aos="fade-down">Global Leaderboard</h1>
      <p className="leaderboard-description" data-aos="fade-up" data-aos-delay="200">
        See who stands at the top of the GameQuiz Central challenge!
      </p>

      <div className="leaderboard-grid-wrapper">
        <div className="leaderboard-grid" data-aos="fade-in" data-aos-delay="400">
          {/* Table Header */}
          <div className="leaderboard-header">
            <span className="header-rank">Rank</span>
            <span className="header-username">Player</span>
            <span className="header-score">Score</span>
            <span className="header-quizzes">Quizzes</span>
          </div>

          {/* Leaderboard Rows */}
          {dummyLeaderboardData.map((player) => (
            <div 
              key={player.rank} 
              className={`leaderboard-row ${player.rank <= 3 ? 'top-player' : ''}`}
              data-aos="fade-up" // Animation for each row
              data-aos-delay={400 + (player.rank * 50)} // Staggered animation
            >
              <span className="player-rank">{player.rank}</span>
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