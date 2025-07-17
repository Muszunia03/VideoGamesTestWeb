// src/NavBar.jsx
import React from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom'; // Importujemy 'Link'
import './NavBar.css'; 

function NavBar({ isLoggedIn, username, onLogout }) { 
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    onLogout(); 
    navigate('/'); 
  };

  const isActive = (path) => location.pathname === path;
  
  // Funkcja do sprawdzania, czy aktualna ścieżka to '/profile/:username'
  const isProfileActive = () => location.pathname.startsWith('/profile/');

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand"> {/* Używamy Link zamiast a + onClick */}
        GameQuiz Central
      </Link>

      {isLoggedIn ? ( 
        <ul className="navbar-nav">
          <li className="navbar-nav-item">
            <Link to="/quiz" className={isActive('/quiz') ? 'active' : ''}>
              <span className="icon">🎮</span>
              <span>All Quizzes</span>
            </Link>
          </li>

          <li className="navbar-nav-item">
            <Link to="/leaderboard" className={isActive('/leaderboard') ? 'active' : ''}>
              <span className="icon">🏆</span>
              <span>Leaderboard</span>
            </Link>
          </li>
        </ul>
      ) : ( 
        <div className="navbar-nav-placeholder"></div> 
      )}
      
      <div className="navbar-actions">
        {isLoggedIn ? (
          <>
            {/* Tutaj zmieniamy span na Link, aby nick był klikalny */}
            {/* Używamy backticków dla stringa z zmienną: `/profile/${username}` */}
            <Link to={`/profile/${username}`} className={`username-display ${isProfileActive() ? 'active' : ''}`}>
              <span className="icon">👋</span> {username}
            </Link>
            <button className="logout-btn" onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            {location.pathname !== '/login' && (
              <button className="primary-btn" onClick={() => navigate('/login')}>Log In</button>
            )}
            {location.pathname !== '/signup' && (
              <button className="secondary-btn" onClick={() => navigate('/signup')}>Sign Up</button>
            )}
          </>
        )}
      </div>
    </nav>
  );
}

export default NavBar;