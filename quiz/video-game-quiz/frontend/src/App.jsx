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
import Footer from './Footer';

import RetroQuizPage from './RetroQuizPage';
import LatestQuizPage from './LatestQuizPage';
import ImagesQuizPage from './ImagesQuizPage';
import GenreQuizPage from './GenreQuizPage';
import DailyQuizPage from './DailyQuizPage';
import SpeedrunQuizPage from './DailyQuizPage';
import PlatformMatchQuizPage from './PlatformMatchQuizPage';
import RatingQuizPage from './RatingQuizPage';
import MultiFactQuizPage from './MultiFactQuizPage';

import RegulationsPage from './RegulationsPage';
import NotFoundPage from './NotFoundPage';
import AdminPanelPage from './AdminPanelPage';

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
      <div className="app-container">
        <NavBar
          isLoggedIn={isLoggedIn}
          username={username}
          onLogout={handleLogout}
        />
        <main className="content-wrapper">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route
              path="/login"
              element={isLoggedIn ? <Navigate to="/profile" /> : <LoginForm onLoginSuccess={handleLoginSuccess} />}
            />
            <Route path="/leaderboard" element={<LeaderboardPage />} />
            <Route path="/signup" element={<SignUpForm />} />
            <Route path="/quiz" element={<QuizPage />} />

            <Route path="/quiz/retro" element={<RetroQuizPage />} />
            <Route path="/quiz/latest" element={<LatestQuizPage />} />
            <Route path="/quiz/images" element={<ImagesQuizPage />} />
            <Route path="/quiz/genre" element={<GenreQuizPage />} />
            <Route path="/quiz/daily" element={<DailyQuizPage />} />
            <Route path="/quiz/speedrun" element={<SpeedrunQuizPage />} />
            <Route path="/quiz/platformmatch" element={<PlatformMatchQuizPage />} />
            <Route path="/quiz/rating" element={<RatingQuizPage />} />
            <Route path="/quiz/multifact" element={<MultiFactQuizPage />} />

            <Route
              path="/profile/:username"
              element={isLoggedIn ? <UserProfilePage /> : <Navigate to="/login" />}
            />
            <Route
              path="/profile"
              element={isLoggedIn ? <Navigate to={`/profile/${username}`} /> : <Navigate to="/login" />}
            />

            <Route path="/regulations" element={<RegulationsPage />} />
            <Route path="/admin" element={<AdminPanelPage />} />

            <Route path="*" element={<NotFoundPage />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </Router>
  );
}

export default App;