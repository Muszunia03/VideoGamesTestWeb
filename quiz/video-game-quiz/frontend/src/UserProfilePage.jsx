
import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import './UserProfilePage.css';
import AOS from 'aos';
import 'aos/dist/aos.css';

function UserProfilePage() {
  const { username } = useParams();

  useEffect(() => {
    AOS.init({ duration: 1000, once: false });
    AOS.refresh();
  }, []);

  const dummyUserData = {
    username: username, 
    totalScore: 12500,
    joinDate: '2023-01-15',
  };

  return (
    <div className="profile-page-container">
      <h1 className="profile-main-title" data-aos="fade-down">
        {dummyUserData.username}'s Profile
      </h1>
      <p className="profile-description" data-aos="fade-up" data-aos-delay="200">
        User statistics and account details.
      </p>

      <div className="profile-stats-grid-wrapper">
        <div className="profile-stats-grid">
          
          <div className="stat-card" data-aos="zoom-in" data-aos-delay="400">
            <h3>Total Score</h3>
            <p className="stat-value">{dummyUserData.totalScore}</p>
          </div>
          
          <div className="stat-card" data-aos="zoom-in" data-aos-delay="500">
            <h3>Joined On</h3>
            <p className="stat-value">{dummyUserData.joinDate}</p>
          </div>

        </div>
      </div>

      <p className="profile-footer-note" data-aos="fade-up" data-aos-delay="900">
        Thank you for being a part of our community!
      </p>
    </div>
  );
}

export default UserProfilePage;