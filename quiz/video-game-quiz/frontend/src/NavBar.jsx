import React from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom'; 
import './NavBar.css'; 

function NavBar({ isLoggedIn, username, onLogout }) { 
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    onLogout(); 
    navigate('/'); 
  };

  const isActive = (path) => location.pathname === path;
  const isProfileActive = () => location.pathname.startsWith('/profile/');

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">
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

          <li className="navbar-nav-item">
            <Link to="/regulations" className={isActive('/regulations') ? 'active' : ''}>
              <span className="icon">📜</span> 
              <span>Regulations</span>
            </Link>
          </li>
        </ul>
      ) : ( 
        <div className="navbar-nav-placeholder"></div> 
      )}
      
      <div className="navbar-actions">
        {isLoggedIn ? (
          <>
            <Link to={`/profile/${username}`} className={`username-display ${isProfileActive() ? 'active' : ''}`}>
              <span className="icon">👾</span> {username}
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