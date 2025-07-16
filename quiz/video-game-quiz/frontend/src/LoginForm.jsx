// src/LoginForm.jsx

import { useState } from 'react';
import './LoginForm.css'; // styl opcjonalny
import { useNavigate } from 'react-router-dom';

function LoginForm() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate(); // <---

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Username:', username);
    console.log('Password:', password);
    // tu później dodasz logikę do Spring Boot
  };

  return (
    <form className="login-form" onSubmit={handleSubmit}>
      <h2>Login</h2>
      <input
        type="text"
        placeholder="Username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <div className="button-group">
        <button type="submit">Log In</button>
        <button type="button" onClick={() => navigate('/signup')}>Sign Up</button>
      </div>
    </form>
  );
}


export default LoginForm;
