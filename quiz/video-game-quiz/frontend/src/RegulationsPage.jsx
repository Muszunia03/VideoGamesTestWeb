import React from 'react';
import './RegulationsPage.css';

function RegulationsPage() {
  return (
    <div className="regulations-page-container">
      <h1 className="regulations-main-title">
        GameQuiz Central – Regulations
      </h1>
      <p className="regulations-description">
        Please read the following rules to ensure a fun and fair experience for all players.
      </p>

      <div className="regulations-content-wrapper">
        <section className="regulations-section">
          <h2>1. General Provisions</h2>
          <p>1.1. Using GameQuiz Central means you accept and follow this regulation.</p>
          <p>1.2. The platform is intended for educational and entertainment purposes, specifically related to video game knowledge.</p>
          <p>1.3. Any usage against the law, good conduct, or these rules is strictly forbidden.</p>
        </section>

        <section className="regulations-section">
          <h2>2. Registration and User Accounts</h2>
          <p>2.1. Creating an account requires a unique email and password.</p>
          <p>2.2. Each user may only own one account. Sharing accounts is not allowed.</p>
          <p>2.3. You are fully responsible for actions taken under your account.</p>
          <p>2.4. If you suspect your account has been compromised, contact support immediately.</p>
        </section>

        <section className="regulations-section">
          <h2>3. Quiz Participation</h2>
          <p>3.1. Quizzes are meant to test knowledge about video games in a fair and fun way.</p>
          <p>3.2. Using external tools or modifying the code to cheat is strictly forbidden and will result in a ban.</p>
          <p>3.3. In time-limited quizzes (e.g. Speedrun), bypassing time restrictions will lead to account suspension.</p>
          <p>3.4. Daily quizzes may only be completed once per day.</p>
        </section>

        <section className="regulations-section">
          <h2>4. User Conduct and Content</h2>
          <p>4.1. Do not post offensive, illegal, or copyrighted material.</p>
          <p>4.2. All content in quizzes (titles, images, descriptions) is protected by intellectual property rights.</p>
          <p>4.3. Impersonating other users or staff members is strictly prohibited.</p>
        </section>

        <section className="regulations-section">
          <h2>5. Privacy and Personal Data</h2>
          <p>5.1. Your data is stored securely and used only for account and quiz personalization purposes.</p>
          <p>5.2. Administrators do not share your data with third parties, unless required by law.</p>
        </section>

        <section className="regulations-section">
          <h2>6. Responsibility and Downtime</h2>
          <p>6.1. The admin is not responsible for service interruptions caused by force majeure, technical errors, or maintenance.</p>
          <p>6.2. You use the service at your own risk.</p>
        </section>

        <section className="regulations-section">
          <h2>8. Final Provisions</h2>
          <p>8.1. The administrator may change these regulations at any time. Updates will be announced on the homepage.</p>
          <p>8.2. Continuing to use the platform after changes means you agree to them.</p>
        </section>

        <p className="regulations-footer-note">
          By using GameQuiz Central, you confirm that you’ve read, understood, and accepted these regulations.
        </p>
      </div>
    </div>
  );
}

export default RegulationsPage;