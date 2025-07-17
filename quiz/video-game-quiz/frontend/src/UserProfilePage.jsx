// src/UserProfilePage.jsx
import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './UserProfilePage.css'; // Tworzymy nowy plik CSS dla tej strony
import AOS from 'aos';
import 'aos/dist/aos.css';

function UserProfilePage() {
  const { username } = useParams(); // Pobieramy nazwę użytkownika z URL

  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  const dummyUserData = {
    username: username, 
    totalScore: 12500,
    quizzesCompleted: 50,
    rank: 1,
    favoriteGenre: 'RPG',
    joinDate: '2023-01-15',
  };

  // Tutaj możesz dodać logikę pobierania danych z API
  // useEffect(() => {
  //   const fetchUserData = async () => {
  //     try {
  //       const response = await fetch(`/api/users/${username}/profile`); // Przykład endpointu API
  //       const data = await response.json();
  //       // Ustawienie rzeczywistych danych zamiast dummyUserData
  //       // setUserData(data);
  //     } catch (error) {
  //       console.error("Error fetching user profile:", error);
  //     }
  //   };
  //   fetchUserData();
  // }, [username]);


  return (
    <div className="profile-page-container">
      <h1 className="profile-main-title" data-aos="fade-down">
        {username}'s Profile
      </h1>
      <p className="profile-description" data-aos="fade-up" data-aos-delay="200">
        Discover {username}'s stats and achievements!
      </p>

      <div className="profile-stats-grid-wrapper">
        <div className="profile-stats-grid">
          <div className="stat-card" data-aos="zoom-in" data-aos-delay="400">
            <h3>Total Score</h3>
            <p className="stat-value">{dummyUserData.totalScore}</p>
          </div>
          <div className="stat-card" data-aos="zoom-in" data-aos-delay="500">
            <h3>Quizzes Completed</h3>
            <p className="stat-value">{dummyUserData.quizzesCompleted}</p>
          </div>
          <div className="stat-card" data-aos="zoom-in" data-aos-delay="600">
            <h3>Global Rank</h3>
            <p className="stat-value">#{dummyUserData.rank}</p>
          </div>
          {/* Możesz dodać więcej statystyk tutaj */}
          <div className="stat-card" data-aos="zoom-in" data-aos-delay="700">
            <h3>Favorite Genre</h3>
            <p className="stat-value">{dummyUserData.favoriteGenre}</p>
          </div>
          <div className="stat-card" data-aos="zoom-in" data-aos-delay="800">
            <h3>Joined On</h3>
            <p className="stat-value">{dummyUserData.joinDate}</p>
          </div>
        </div>
      </div>

      <p className="profile-footer-note" data-aos="fade-up" data-aos-delay="900">
        Keep quizzing to improve your rank!
      </p>
    </div>
  );
}

export default UserProfilePage;