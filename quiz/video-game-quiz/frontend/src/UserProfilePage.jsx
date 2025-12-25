import React, { useEffect, useState, useMemo } from 'react';
import { useParams, Link } from 'react-router-dom';
import './UserProfilePage.css';
import AOS from 'aos';
import 'aos/dist/aos.css';

function UserProfilePage() {
  const { username } = useParams();
  const [profile, setProfile] = useState(null);
  const [results, setResults] = useState([]);

  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  useEffect(() => {
    async function loadProfile() {
      try {
        const userRes = await fetch(`http://localhost:8080/api/users/profile/${username}`);
        const userData = await userRes.json();
        setProfile(userData);

        const resultsRes = await fetch(`http://localhost:8080/api/results/user/${username}`);
        const resultsData = await resultsRes.json();

        if (Array.isArray(resultsData)) {
          setResults(resultsData);
        }

      } catch (e) {
        console.error("Błąd ładowania profilu:", e);
      }
    }

    loadProfile();
  }, [username]);
  
  const totalCalculatedScore = useMemo(() => {
    return results.reduce((sum, result) => {
      const scoreValue = parseInt(result.score, 10);
      return sum + (isNaN(scoreValue) ? 0 : scoreValue);
    }, 0);
  }, [results]);

  const getResultColor = (score) => {
    const scoreNum = parseInt(score, 10);
    if (scoreNum >= 10) {
      return '#1abc9c';
    } else if (scoreNum >= 5) {
      return '#f39c12';
    } else if (scoreNum > 0) {
      return '#00E0FF';
    }
    return '#c0c0c0';
  };
  
  const getRowStyle = () => {
    return {
      borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
      backgroundColor: 'rgba(26, 26, 46, 0.9)', 
      transition: 'all 0.3s ease',
    };
  };

  const isAdmin = profile && (profile.username === "MachM" || profile.username === "Admin");

  if (!profile) {
    return (
      <div className="profile-page-container">
        <h1 className="profile-main-title">Loading...</h1>
      </div>
    );
  }

  return (
    <div className="profile-page-container" style={{ position: 'relative' }}>

      {isAdmin && (
        <Link 
          to="/admin"
          data-aos="fade-left"
          style={{ 
            position: 'absolute', 
            top: '20px', 
            right: '20px', 
            display: 'flex', 
            alignItems: 'center', 
            gap: '8px',
            textDecoration: 'none', 
            color: '#00f7ffff', 
            border: '1px solid rgba(7, 238, 255, 1)',
            backgroundColor: 'rgba(0, 0, 0, 0.6)',
            padding: '10px 15px',
            borderRadius: '30px',
            fontWeight: 'bold',
            fontSize: '0.9em',
            transition: 'all 0.3s ease',
            cursor: 'pointer',
            zIndex: 100, 
            backdropFilter: 'blur(5px)'
          }}
          onMouseEnter={(e) => {
             e.currentTarget.style.backgroundColor = 'rgba(0, 0, 0, 0.47)';
             e.currentTarget.style.boxShadow = '0 0 15px rgba(4, 0, 255, 0.4)';
             e.currentTarget.style.transform = 'scale(1.05)';
          }}
          onMouseLeave={(e) => {
             e.currentTarget.style.backgroundColor = 'rgba(0, 0, 0, 0.6)';
             e.currentTarget.style.boxShadow = 'none';
             e.currentTarget.style.transform = 'scale(1)';
          }}
        >
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
          </svg>
          ADMIN PANEL
        </Link>
      )}

      <h1 className="profile-main-title" data-aos="fade-down">
        {profile.username}'s Profile
      </h1>

      <div className="profile-stats-grid-wrapper">
        <div className="profile-stats-grid">

          <div className="stat-card" data-aos="zoom-in" data-aos-delay="400">
            <h3>Total Score</h3>
            <p className="stat-value">{totalCalculatedScore}</p>
          </div>

          <div className="stat-card" data-aos="zoom-in" data-aos-delay="500">
            <h3>Joined On</h3>
            <p className="stat-value" style={{ fontSize: '1.8em' }}> 
              {new Date(profile.createdAt).toLocaleDateString('en-US', { year: '2-digit', month: '2-digit', day: '2-digit' })}
            </p>
          </div>

          <div className="stat-card" data-aos="zoom-in" data-aos-delay="600">
            <h3>Games Played</h3>
            <p className="stat-value">{results.length}</p>
          </div>

        </div>
      </div>

      <div className="profile-results-wrapper">
        <h2 className="results-title" data-aos="fade-up" data-aos-delay="700">
          Recent Results
        </h2>

        {results.length === 0 ? (
          <p className="no-results" data-aos="fade-up">
            No quiz history yet.
          </p>
        ) : (
          <table style={{ 
              width: '100%', 
              maxWidth: '900px', 
              borderCollapse: 'collapse', 
              boxShadow: '0 5px 20px rgba(0, 0, 0, 0.5), 0 0 10px rgba(233, 30, 99, 0.2)',
              borderRadius: '10px',
              overflow: 'hidden',
              margin: '20px auto' 
            }}
          >
            <thead>
              <tr style={{ backgroundColor: 'rgba(6, 193, 240, 0.4)', color: '#fff', fontWeight: 700, textTransform: 'uppercase' }}>
                <th style={{ padding: '15px 20px', textAlign: 'left', width: '40%' }}>Quiz Type</th>
                <th style={{ padding: '15px 20px', textAlign: 'center', width: '25%' }}>Score</th>
                <th style={{ padding: '15px 20px', textAlign: 'right', width: '35%' }}>Date Completed</th>
              </tr>
            </thead>
            <tbody>
              {results.slice(0, 10).map((r, index) => { 
                const color = getResultColor(r.score);
                const hoverShadow = `0 0 10px ${color}`; 
                const hoverBg = 'rgba(40, 40, 70, 0.9)';

                return (
                  <tr
                    key={index}
                    style={getRowStyle()}
                    onMouseEnter={(e) => { 
                      e.currentTarget.style.backgroundColor = hoverBg; 
                      e.currentTarget.style.boxShadow = hoverShadow;
                      e.currentTarget.style.transform = 'scale(1.01)';
                    }}
                    onMouseLeave={(e) => { 
                      e.currentTarget.style.backgroundColor = 'rgba(26, 26, 46, 0.9)';
                      e.currentTarget.style.boxShadow = 'none';
                      e.currentTarget.style.transform = 'scale(1)';
                    }}
                  >
                    <td style={{ padding: '12px 20px', textAlign: 'left', fontWeight: 600, color: '#FFFFFF', width: '40%' }}>
                      {r.quizType}
                    </td>
                    <td style={{ padding: '12px 20px', textAlign: 'center', fontWeight: 900, fontSize: '1.2em', color: color, width: '25%', whiteSpace: 'nowrap' }}>
                      {r.score} pts
                    </td>
                    <td style={{ padding: '12px 20px', textAlign: 'right', color: '#a0a0a0', fontSize: '0.9em', width: '35%' }}>
                      {new Date(r.createdAt).toLocaleString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })}
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default UserProfilePage;