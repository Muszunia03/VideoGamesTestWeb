import React, { useEffect, useState } from 'react';
import './LeaderboardPage.css';
import AOS from 'aos'; 
import 'aos/dist/aos.css';

function LeaderboardPage() {
  const [leaderboardData, setLeaderboardData] = useState([]);

  useEffect(() => {
    AOS.init({ duration: 1000 });

    const fetchLeaderboard = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/leaderboard");
        const data = await res.json();

        if (!Array.isArray(data)) {
          console.error("The data received is not an array:", data);
          setLeaderboardData([]);
          return;
        }

        const sortedData = [...data]
          .sort((a, b) => (b.score || 0) - (a.score || 0))
          .slice(0, 15);

        setLeaderboardData(sortedData);
      } catch (err) {
        console.error("Error fetching leaderboard:", err);
        setLeaderboardData([]);
      }
    };

    fetchLeaderboard();
  }, []);

  return (
    <div className="leaderboard-page-container">
      <h1 className="leaderboard-main-title" data-aos="fade-down">Global Leaderboard</h1>

      <div className="leaderboard-grid-wrapper">
        <div className="leaderboard-grid" data-aos="fade-in" data-aos-delay="400">
          <div className="leaderboard-header">
            <span className="header-rank">Rank</span>
            <span className="header-username">Player</span>
            <span className="header-score">Score</span>
            <span className="header-quizzes">Quiz Type</span>
          </div>

          {leaderboardData.length === 0 ? (
            <div className="leaderboard-row">
            </div>
          ) : (
            leaderboardData.map((player, index) => (
              <div 
                key={index}
                className={`leaderboard-row ${index < 3 ? 'top-player' : ''}`}
                data-aos="fade-up"
                data-aos-delay={400 + index * 50}
              >
                <span className="player-rank">{index + 1}</span>
                <span className="player-username">{player.username || `User#${player.user_id}`}</span>
                <span className="player-score">{player.score ?? 0}</span>
                <span className="player-quizzes">{player.quizType || player.quiz_type || '-'}</span>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}

export default LeaderboardPage;
