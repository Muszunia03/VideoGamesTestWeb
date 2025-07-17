import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import AOS from 'aos'; 
import 'aos/dist/aos.css';

import NavBar from './NavBar'; 
import LoginForm from './LoginForm';
import SignUpForm from './SignUpForm';
import QuizPage from './QuizPage'; 
import HomePage from './HomePage'; 
import LeaderboardPage from './LeaderboardPage';
import UserProfilePage from './UserProfilePage'; 

import './App.css'; 

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [username, setUsername] = useState('');

  useEffect(() => {
    AOS.init({
      duration: 1000,
      once: true, 
      mirror: false, 
    });
  }, []); 

  useEffect(() => {
    const storedUsername = localStorage.getItem('username');

    if (storedUsername && localStorage.getItem('isLoggedIn') === 'true') {
      setIsLoggedIn(true);
      setUsername(storedUsername);
    } else {
      setIsLoggedIn(false);
      setUsername('');
    }
  }, []);

  const handleLoginSuccess = (user) => {
    setIsLoggedIn(true);
    setUsername(user);
    localStorage.setItem('username', user); 
    localStorage.setItem('isLoggedIn', 'true'); 
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
    setUsername('');
    localStorage.removeItem('username'); 
    localStorage.removeItem('isLoggedIn');
  };

  return (
    <Router>
      <NavBar 
        isLoggedIn={isLoggedIn} 
        username={username} 
        onLogout={handleLogout} 
      />
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route 
            path="/login" 
            element={isLoggedIn ? <Navigate to="/profile" /> : <LoginForm onLoginSuccess={handleLoginSuccess} />} 
        />
        <Route path="/leaderboard" element={<LeaderboardPage />} /> 
        <Route path="/signup" element={<SignUpForm />} />
        <Route path="/quiz" element={<QuizPage />} />
        <Route 
          path="/profile/:username" 
          element={isLoggedIn ? <UserProfilePage /> : <Navigate to="/login" />} 
        />
        <Route 
          path="/profile" 
          element={isLoggedIn ? <Navigate to={`/profile/${username}`} /> : <Navigate to="/login" />} 
        />
      </Routes>
    </Router>
  );
}

export default App;